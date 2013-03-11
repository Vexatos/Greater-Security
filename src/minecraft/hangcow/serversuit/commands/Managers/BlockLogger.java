package hangcow.serversuit.commands.Managers;

import java.io.File;

import net.minecraft.command.ServerCommandManager;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;


@Mod(modid="BlockLogger",name="BlockLogger",version="A1")

public class BlockLogger 
{
	public static BlockLogger instance;
	public static MinecraftServer server;
	
	static Configuration config = new Configuration(new File(cpw.mods.fml.common.Loader.instance().getConfigDir().getParentFile(),"/BlockLogger/configMain.cfg"));
	private static String mystring =  ConfigurationProperties();
	public static String ConfigurationProperties() 
	{
		config.load();
		FileManager.fileCheck();
		config.save();
		return "Loaded";
	}
	
	@PreInit
	public void preInit(FMLPreInitializationEvent event)
	{
		
	}
	
	@PostInit
	public void postInit(FMLPostInitializationEvent event)
	{
	 	
	}
	
	
		
	public void regCommands(ServerCommandManager manager)
	{
		
	}

}