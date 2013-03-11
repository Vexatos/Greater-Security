package dark.locking;


import java.util.List;

public interface ISpecialAccess
{
	/**
	 * Gets the player's access level on the machine he is using
	 * 
	 * @return access level of the player, make sure to return no access if the player doesn't have
	 * any
	 */
	public AccessLevel getUserAccess(String username);

	/**
	 * gets the access list for the machine
	 */
	public List<UserAccess> getUsers();

	/**
	 * sets the players access level
	 */
	public boolean addUserAccess(String username, AccessLevel level, boolean save);

	/**
	 * Removes the user from the access list
	 */
	public boolean removeUserAccess(String username);

	/**
	 * Gets a list of users with the specified access level.
	 */
	public List<UserAccess> getUsersWithAcess(AccessLevel level);

}
