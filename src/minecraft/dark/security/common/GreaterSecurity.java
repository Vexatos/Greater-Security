package dark.security.common;

import java.io.File;
import java.util.Arrays;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import universalelectricity.prefab.TranslationHelper;
import universalelectricity.prefab.network.PacketManager;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.Metadata;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import dark.core.DarkMain;
import dark.core.ModPrefab;
import dark.core.terminal.CommandHelp;
import dark.core.terminal.CommandRegistry;
import dark.core.terminal.CommandUser;
import dark.security.common.chest.BlockLockedChest;
import dark.security.common.chest.TileEntityLockedChest;
import dark.security.common.cmd.CommandBreak;
import dark.security.common.detector.BlockDetector;
import dark.security.common.door.BlockLockedDoor;
import dark.security.common.door.ItemLockedDoor;
import dark.security.common.door.TileEntityLockedDoor;
import dark.security.common.fence.BlockElectroFence;
import dark.security.common.fence.BlockLaserFence;
import dark.security.common.fence.TileEntityElectroFence;
import dark.security.common.fence.TileEntityLaserFence;

/** @author CowGod, Darkguardsman */
@Mod(modid = GreaterSecurity.MOD_ID, name = GreaterSecurity.MOD_NAME, version = DarkMain.VERSION, dependencies = "after:DarkCore", useMetadata = true)
@NetworkMod(channels = { GreaterSecurity.CHANNEL }, clientSideRequired = true, serverSideRequired = false, packetHandler = PacketManager.class)
public class GreaterSecurity extends ModPrefab
{
	// @Mod
	public static final String MOD_ID = "GreaterSecurity";
	public static final String MOD_NAME = "Greater Security";

	@SidedProxy(clientSide = "dark.security.client.ClientProxy", serverSide = "dark.security.common.CommonProxy")
	public static CommonProxy proxy;

	public static final String CHANNEL = "GreaterSecurity";

	@Metadata(GreaterSecurity.MOD_ID)
	public static ModMetadata meta;

	/* SUPPORTED LANGS */
	private static final String[] LANGUAGES_SUPPORTED = new String[] { "en_US" };

	public static Configuration config = new Configuration(new File(Loader.instance().getConfigDir(), MOD_ID + ".cfg"));

	@Instance(MOD_NAME)
	public static GreaterSecurity instance;
	/* BLOCKS */
	public static Block blockLockedChest;
	public static Block blockLockedDoor;
	public static Block blockLaserFence;
	public static BlockElectroFence blockElectroFence;
	public static Block blockItemDetector;

	/* ITEMS */
	public static Item itemLock;
	public static Item itemLockedDoor;

	/* CONFIG VARS */
	public static Boolean breakChests;
	public static Boolean breakDoors;
	// TODO Added breakDetetcors field to be read form config
	public static Boolean breakDetectors;
	// CreativeTab GreaterSecurity.tabGreaterSecurity
	public static CreativeTabs tabGreaterSecurity = new CreativeTabs("GreaterSecurity")
	{

		public ItemStack getIconItemStack()
		{
			return new ItemStack(GreaterSecurity.itemLock, 1, 0);
		}
	};

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{

		instance = this;
		super.preInit(event);
		NetworkRegistry.instance().registerGuiHandler(this, GreaterSecurity.proxy);
		proxy.preInit();

	}

	@EventHandler
	public void generalLoad(FMLInitializationEvent event)
	{
		super.init(event);
		CommandRegistry.register(new CommandUser());
		CommandRegistry.register(new CommandHelp());
		CommandRegistry.register(new CommandBreak());

		// // Registration ////
		GameRegistry.registerBlock(blockLockedChest, "gsChest");
		GameRegistry.registerBlock(blockLockedDoor, "gsDoor");
		GameRegistry.registerBlock(blockLaserFence, "gsLaserFence");
		GameRegistry.registerBlock(blockElectroFence, "gsEltroFence");
		GameRegistry.registerBlock(blockItemDetector, "gsItemDetector");

		GameRegistry.registerTileEntity(TileEntityLockedChest.class, "LChest");
		GameRegistry.registerTileEntity(TileEntityLockedDoor.class, "LDoor");
		GameRegistry.registerTileEntity(TileEntityLaserFence.class, "LaserFence");
		GameRegistry.registerTileEntity(TileEntityElectroFence.class, "EltroFence");

		FMLLog.info(" Loaded: " + TranslationHelper.loadLanguages(LANGUAGE_PATH, LANGUAGES_SUPPORTED) + " Languages.");
		proxy.init();
	}

	@EventHandler
	public void postLoad(FMLPostInitializationEvent event)
	{
		super.postInit(event);
		proxy.postInit();

		// // Locked Chest ////
		GameRegistry.addRecipe(new ItemStack(GreaterSecurity.blockLockedChest, 1, 0), new Object[] { "WWW", "WLW", "WWW", 'W', Block.planks, 'L', new ItemStack(GreaterSecurity.itemLock, 1) });
		GameRegistry.addShapelessRecipe(new ItemStack(GreaterSecurity.blockLockedChest, 1), new Object[] { new ItemStack(Block.chest), new ItemStack(GreaterSecurity.itemLock, 1) });

		// // Laser Fence ////
		GameRegistry.addRecipe(new ItemStack(GreaterSecurity.blockLaserFence, 1), new Object[] { "GGG", "CRC", "WBW", 'G', Block.glass, 'C', new ItemStack(GreaterSecurity.blockLockedChest, 1) });

		// // Locked Door ////
		GameRegistry.addRecipe(new ItemStack(GreaterSecurity.itemLockedDoor, 1), new Object[] { "WDW", "WLW", 'D', Item.doorWood, 'W', Block.stone, 'L', new ItemStack(GreaterSecurity.itemLock, 1) });

		// // Item Lock ////
		GameRegistry.addRecipe(new ItemStack(GreaterSecurity.itemLock, 2), new Object[] { "III", "I I", "WIW", 'W', Block.wood, 'I', Item.ingotIron });

	}

	@Override
	public void loadConfig()
	{
		config.load();
		breakChests = config.get(Configuration.CATEGORY_GENERAL, "canBreakChests", true).getBoolean(true);
		breakChests = config.get(Configuration.CATEGORY_GENERAL, "canBreakDoors", true).getBoolean(true);
		// TODO Added breakDetectors config reading.
		breakDetectors = config.get(Configuration.CATEGORY_GENERAL, "canBreakDetectors", true).getBoolean(true);

		itemLock = new ItemLock(config.getItem("LockItemID", ITEM_ID_PREFIX++).getInt());
		itemLockedDoor = new ItemLockedDoor(config.getItem("DoorItemID", ITEM_ID_PREFIX++).getInt());

		blockLockedChest = new BlockLockedChest(getNextID());
		blockLockedDoor = new BlockLockedDoor(getNextID());
		blockLaserFence = new BlockLaserFence(getNextID());
		blockElectroFence = new BlockElectroFence(getNextID());
		blockItemDetector = new BlockDetector(getNextID());

		config.save();

	}

	@Override
	public void loadModMeta()
	{
		meta.modId = GreaterSecurity.MOD_ID;
		meta.name = GreaterSecurity.MOD_NAME;
		meta.description = "Helps lock your stuff up so it does not get stolen";
		meta.url = "http://www.universalelectricity.com/Greater-Protection";

		meta.logoFile = GreaterSecurity.TEXTURE_DIRECTORY + "GP_Banner.png";
		meta.version = DarkMain.VERSION;
		meta.authorList = Arrays.asList(new String[] { "DarkGuardsman", "TheCowGod" });
		meta.credits = "Please see the website.";
		meta.autogenerated = false;

	}

	@Override
	public String getDomain()
	{
		return "greatersecurity";
	}
}
