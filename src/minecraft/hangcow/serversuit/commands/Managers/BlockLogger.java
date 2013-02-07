package hangcow.serversuit.commands.Managers;

import hangcow.serversuit.commands.GetDeathsCommands;

import java.io.File;
import java.io.IOException;

import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.ModLoader;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.Mod.ServerStarting;
import cpw.mods.fml.common.Mod.ServerStopping;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;


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