package dark.locking;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.WorldEvent;
import cpw.mods.fml.common.Mod.ServerStarting;
import cpw.mods.fml.common.Mod.ServerStopping;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;

public class UserAccessLoader
{
	public static boolean isInitialized = false;
	
	public static UserAccessLoader intance = new UserAccessLoader();
	
	public void initiate()
	{
		if (!isInitialized)
		{
			MinecraftForge.EVENT_BUS.register(this);
			isInitialized = true;
		}
	}

	@ForgeSubscribe
	public void onWorldSave(WorldEvent.Save event)
	{
		if (!event.world.isRemote && GlobalAccessList.hasLoaded)
		{
			GlobalAccessList.saveMasterSaveFile();
		}
	}

	@ServerStarting
	public void serverStarting(FMLServerStartingEvent event)
	{
		if (!GlobalAccessList.hasLoaded)
		{
			GlobalAccessList.getMasterSaveFile();
		}
	}

	@ServerStopping
	public void serverStopping(FMLServerStoppingEvent event)
	{
		if (GlobalAccessList.hasLoaded)
		{
			GlobalAccessList.saveMasterSaveFile();
		}
	}
}
