package hangcow.serversuit.GPS;

import hangcow.serversuit.commands.FlyCommand;
import hangcow.serversuit.commands.GetDeathsCommands;
import hangcow.serversuit.commands.HomeCommands;
import hangcow.serversuit.commands.SmiteCommands;
import hangcow.serversuit.commands.Managers.Clock;
import hangcow.serversuit.commands.Managers.EventHandlerPlayer;
import hangcow.serversuit.commands.Managers.FileManager;
import hangcow.serversuit.commands.Managers.TeleportationManager;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.ModLoader;
import net.minecraft.world.GameRules;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Loader;
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

@Mod(modid = GreaterSeverSuit.NAME, name = GreaterSeverSuit.NAME, version = "0.0.1")
public class GreaterSeverSuit
{
    public static GreaterSeverSuit instance;
    public static MinecraftServer server;
    
    public static final String NAME = "GreaterSeverSuit";

    private static Configuration config = new Configuration(new File(Loader.instance().getConfigDir(), "/GSServerSuit/configMain.cfg"));
    
    public static int teleportTime;

    public static void ConfigurationProperties()
    {
        config.load();
        teleportTime = config.get(config.CATEGORY_GENERAL, "Teleport_Time", 5).getInt();
        FileManager.fileCheck();
        config.save();
    }
    Logger logger = Logger.getLogger(NAME);
    @PreInit
    public void preInit(FMLPreInitializationEvent event)
    {
        logger.setParent(FMLLog.getLogger());
        logger.info("Initializing...");
        
    }

    @PostInit
    public void postInit(FMLPostInitializationEvent event)
    {
        logger.info("Finilizing... ");
    }

    @ServerStarting
    public void serverStarting(FMLServerStartingEvent event)
    {
        this.ConfigurationProperties();
        MinecraftForge.EVENT_BUS.register(new EventHandlerPlayer());
        TickRegistry.registerTickHandler(new Clock(), Side.SERVER);
        
        server = ModLoader.getMinecraftServerInstance();
        
        server.worldServers[0].getGameRules().setOrCreateGameRule("mobGriefing", "false");
        server.worldServers[0].getGameRules().setOrCreateGameRule("doFireTick", "false");
        
        ICommandManager commandManager = server.getCommandManager();
        ServerCommandManager serverCommandManager = ((ServerCommandManager) commandManager);
        regCommands(serverCommandManager);
        TickRegistry.registerTickHandler(new TeleportationManager(), Side.SERVER);
    }

    @ServerStopping
    public void serverStopping(FMLServerStoppingEvent event)
    {
        try
        {
            FileManager.updateFiles();
        }
        catch (IOException e)
        {
            System.out.print("Failed to save logs before closing");
            e.printStackTrace();
        }
    }
    /**
     * Used to register the commands to the server
     * @param manager - server's command manager
     */
    public void regCommands(ServerCommandManager manager)
    {
        manager.registerCommand(new HomeCommands());
        manager.registerCommand(new SmiteCommands());
        manager.registerCommand(new FlyCommand());
        manager.registerCommand(new GetDeathsCommands());
    }

}