package dark.locking;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import universalelectricity.prefab.flag.NBTFileLoader;

import net.minecraft.nbt.NBTTagCompound;

public class GlobalAccessList
{
	/** Name of the save file **/
	private static final String SAVE_NAME = "Global_Access_List";
	/** Hash map of loaded lists **/
	private static Map<String, List<UserAccess>> globalUserLists = new HashMap<String, List<UserAccess>>();
	/** Master save NBT that gets saved **/
	private static NBTTagCompound masterSaveNbt = new NBTTagCompound();
	/** Used to check to see if file is in the process of being saved **/
	private static boolean saving = false;
	/** Used to check to see if file is in the process of being loaded **/
	private static boolean loading = false;
	/** Used to check to see if file was loaded at least once **/
	public static boolean hasLoaded = false;
	/** Used to check to see if file was changed and needs saved **/
	public static boolean needsSaving = false;

	/**
	 * Gets or creates a userAccess list to be used for any reason
	 * 
	 * @param name - name of the access list being created or loaded
	 * @param owner - the player's name to be used to create a new list
	 * @return - UserAccess list
	 */
	public List<UserAccess> getOrCreateList(String name, String owner)
	{
		if (name.toCharArray().length < 5 || owner.isEmpty()) { return null; }
		List<UserAccess> list = getList(name);
		if (list == null)
		{
			/*** Creates a new List if one doesn't exist ***/
			list = new ArrayList<UserAccess>();
			list.add(new UserAccess(owner, AccessLevel.OWNER, true));

			globalUserLists.put(name, list);
			saveList(name, list);
			this.needsSaving = true;
		}
		return list;
	}

	/**
	 * Loads up a UserAccess List
	 * 
	 * @param name - name of the list
	 * @return - the list
	 */
	public List<UserAccess> getList(String name)
	{
		if (globalUserLists.containsKey(name))
		{
			/*** Get the list if its already loaded up ***/
			return globalUserLists.get(name);
		}
		else
		{
			/*** Loads the saved list if it exists ***/
			List<UserAccess> list = loadList(name);
			if (list != null)
			{
				globalUserLists.put(name, list);
			}
			return list;
		}
	}

	/**
	 * adds a user to the global list
	 * 
	 * @param listName - name of the list
	 * @param user - user being added as a UserAccess instance
	 * @return true if added
	 */
	public boolean addUser(String listName, UserAccess user)
	{
		if (user == null) { return false; }

		List<UserAccess> userList = this.getList(listName);

		if (userList != null)
		{
			if (userList.contains(user))
			{
				userList = UserAccess.removeUserAccess(user.username, userList);
			}
			if (userList.add(user))
			{
				globalUserLists.put(listName, userList);
				this.saveList(listName, userList);
				this.needsSaving = true;
				return true;
			}
		}
		return false;
	}

	/**
	 * Removes a user from the global list
	 * 
	 * @param listName - name of the list
	 * @param user - user being removed
	 * @return true if removed
	 */
	public boolean removeUser(String listName, UserAccess user)
	{
		if (user == null) { return false; }

		List<UserAccess> userList = this.getList(listName);

		if (userList != null)
		{
			if (userList.contains(user))
			{
				userList = UserAccess.removeUserAccess(user.username, userList);
				globalUserLists.put(listName, userList);
				this.saveList(listName, userList);
				this.needsSaving = true;
				return true;
			}
		}
		return false;
	}

	/**
	 * Loads a given Global user list from the master save
	 * 
	 * @param name - name given to the list for reference
	 * @return - the list of user access levels to be used
	 */
	private List<UserAccess> loadList(String name)
	{
		NBTTagCompound masterSave = getMasterSaveFile();
		if (masterSave != null && masterSave.hasKey(name))
		{
			NBTTagCompound accessSave = masterSave.getCompoundTag(name);
			return UserAccess.readListFromNBT(accessSave, "Users");
		}

		return null;
	}

	/**
	 * Saves a given Global user list into the master save
	 * 
	 * @param name - name to save the list as
	 * @param list - list to be saved
	 */
	private void saveList(String name, List<UserAccess> list)
	{
		NBTTagCompound masterSave = getMasterSaveFile();
		if (masterSave != null)
		{
			NBTTagCompound accessSave = masterSave.getCompoundTag(name);
			UserAccess.writeListToNBT(accessSave, list);
			// TODO finish this
		}
	}

	/**
	 * Loads the master save from the world folder
	 */
	public static NBTTagCompound getMasterSaveFile()
	{
		if (masterSaveNbt.hasNoTags())
		{
			if (!loading)
			{
				hasLoaded = true;
				loading = true;
				NBTFileLoader.loadData(SAVE_NAME);
				// TODO save the file
				loading = false;
			}
		}
		return masterSaveNbt;
	}

	public static void saveMasterSaveFile()
	{
		if (!saving && hasLoaded && needsSaving)
		{
			needsSaving = false;
			saving = true;
			NBTFileLoader.saveData(SAVE_NAME, masterSaveNbt);
			saving = false;
		}
	}
}
