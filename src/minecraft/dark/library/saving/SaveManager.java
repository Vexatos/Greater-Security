package dark.library.saving;

import java.util.ArrayList;
import java.util.List;

import universalelectricity.prefab.flag.NBTFileLoader;

import cpw.mods.fml.common.Mod.ServerStarting;
import cpw.mods.fml.common.Mod.ServerStopping;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import dark.library.locking.GlobalAccessList;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.WorldEvent;

public class SaveManager
{
	public static List<INbtSave> nbtSaveList = new ArrayList<INbtSave>();

	public static boolean isInitialized = false;

	public static SaveManager intance = new SaveManager();

	/**
	 * registers a class that uses INbtSave to save data to a file in the worldSave file
	 * 
	 * @param saveClass
	 */
	public void registerNbtSave(INbtSave saveClass)
	{
		if (!isInitialized)
		{
			MinecraftForge.EVENT_BUS.register(this);
			isInitialized = true;
		}

		if (saveClass != null && !nbtSaveList.contains(saveClass))
		{
			nbtSaveList.add(saveClass);
		}
	}

	@ForgeSubscribe
	public void onWorldSave(WorldEvent.Save event)
	{
		save(!event.world.isRemote);
	}

	@ServerStopping
	public void serverStopping(FMLServerStoppingEvent event)
	{
		save(true);
	}

	public void save(boolean isServer)
	{
		for (INbtSave save : nbtSaveList)
		{
			if (save.shouldSave(isServer))
			{
				NBTFileLoader.saveData(save.saveFileName(), save.getSaveData());
			}
		}
	}
}
