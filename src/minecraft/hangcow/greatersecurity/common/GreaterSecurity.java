package hangcow.greatersecurity.common;

import hangcow.greatersecurity.common.chest.BlockLockedChest;
import hangcow.greatersecurity.common.chest.TileEntityLockedChest;
import hangcow.greatersecurity.common.cmd.CommandBreak;
import hangcow.greatersecurity.common.door.BlockLockedDoor;
import hangcow.greatersecurity.common.door.ItemLockedDoor;
import hangcow.greatersecurity.common.door.TileEntityLockedDoor;
import hangcow.greatersecurity.common.fence.electro.BlockEletroFence;
import hangcow.greatersecurity.common.fence.electro.TileEntityEltroFence;
import hangcow.greatersecurity.common.fence.laser.BlockLaserFence;
import hangcow.greatersecurity.common.fence.laser.TileEntityLaserFence;

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
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.Metadata;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import dark.library.terminal.commands.CommandHelp;
import dark.library.terminal.commands.CommandRegistry;
import dark.library.terminal.commands.CommandUser;

/**
 * 
 * @author CowGod, Darkguardsman
 * 
 */
@Mod(modid = GreaterSecurity.MOD_ID, name = GreaterSecurity.MOD_NAME, version = GreaterSecurity.VERSION, useMetadata = true)
// TODO update version #
@NetworkMod(channels = { GreaterSecurity.CHANNEL }, clientSideRequired = true, serverSideRequired = false, packetHandler = PacketManager.class)
public class GreaterSecurity
{

	// @Mod Prerequisites
	public static final String MAJOR_VERSION = "@MAJOR@";
	public static final String MINOR_VERSION = "@MINOR@";
	public static final String REVIS_VERSION = "@REVIS@";
	public static final String BUILD_VERSION = "@BUILD@";

	// @Mod
	public static final String MOD_ID = "GreaterSecurity";
	public static final String MOD_NAME = "Greater Security";
	public static final String VERSION = MAJOR_VERSION + "." + MINOR_VERSION + "." + REVIS_VERSION + "." + BUILD_VERSION;

	@SidedProxy(clientSide = "hangcow.greatersecurity.client.ClientProxy", serverSide = "hangcow.greatersecurity.common.CommonProxy")
	public static CommonProxy proxy;

	public static final String CHANNEL = "GreaterSecurity";

	@Metadata(GreaterSecurity.MOD_ID)
	public static ModMetadata meta;

	public static final String RESOURCE_PATH = "/mods/greatersecurity/";
	public static final String TEXTURE_PATH = RESOURCE_PATH + "textures/";
	public static final String ITEM_File_PATH = TEXTURE_PATH + "items/";
	public static final String BLOCK_File_PATH = TEXTURE_PATH + "blocks/";
	public static final String MODEL_File_PATH = TEXTURE_PATH + "models/";
	public static final String GUI_File_PATH = TEXTURE_PATH + "gui/";
	public static final String TEXTURE_NAME_PREFIX = "greatersecurity:";
	public static final String LANGUAGE_PATH = RESOURCE_PATH + "languages/";

	/* SUPPORTED LANGS */
	private static final String[] LANGUAGES_SUPPORTED = new String[] { "en_US" };

	private static Configuration config = new Configuration(new File(Loader.instance().getConfigDir(), MOD_ID + ".cfg"));

	@Instance(MOD_NAME)
	public static GreaterSecurity instance;
	/* BLOCKS */
	public static Block blockLockedChest;
	public static Block blockLockedDoor;
	public static Block blockLaserFence;
	public static BlockEletroFence blockEltroFence;

	/* ITEMS */
	public static Item itemLock;
	public static Item itemLockedDoor;

	/* CONFIG VARS */
	public static Boolean breakChests;
	public static Boolean breakDoors;

	// CreativeTab GreaterSecurity.tabGreaterSecurity 
	public static CreativeTabs tabGreaterSecurity = new CreativeTabs("GreaterSecurity")
	{

		public ItemStack getIconItemStack()
		{
			return new ItemStack(GreaterSecurity.itemLock, 1, 0);
		}
	};

	@PreInit
	public void preInit(FMLPreInitializationEvent event)
	{
		instance = this;
		NetworkRegistry.instance().registerGuiHandler(this, this.proxy);

		// //Configs ////
		config.load();
		breakChests = config.get(Configuration.CATEGORY_GENERAL, "canBreakChests", true).getBoolean(true);
		breakChests = config.get(Configuration.CATEGORY_GENERAL, "canBreakDoors", true).getBoolean(true);

		itemLock = new ItemLock(config.getItem(Configuration.CATEGORY_ITEM, "LockItemID", 30007).getInt());
		itemLockedDoor = new ItemLockedDoor(config.getItem(Configuration.CATEGORY_ITEM, "DoorItemID", 30008).getInt());

		blockLockedChest = new BlockLockedChest(config.getBlock(Configuration.CATEGORY_BLOCK, "LockedChest", 1777).getInt());
		blockLockedDoor = new BlockLockedDoor(config.getBlock(Configuration.CATEGORY_BLOCK, "LockedDoor", 1714).getInt());
		blockLaserFence = new BlockLaserFence(config.getBlock(Configuration.CATEGORY_BLOCK, "LaserFence", 1715).getInt());
		blockEltroFence = new BlockEletroFence(config.getBlock(Configuration.CATEGORY_BLOCK, "EltroFence", 1716).getInt());

		config.save();

		// // Registration ////
		GameRegistry.registerBlock(blockLockedChest, "gsChest");
		GameRegistry.registerBlock(blockLockedDoor, "gsDoor");
		GameRegistry.registerBlock(blockLaserFence, "gsLaserFence");
		GameRegistry.registerBlock(blockEltroFence, "gsEltroFence");

		proxy.preInit();

	}

	@Init
	public void generalLoad(FMLInitializationEvent event)
	{
		proxy.init();
		CommandRegistry.register(new CommandUser());
		CommandRegistry.register(new CommandHelp());
		CommandRegistry.register(new CommandBreak());
		/* MCMOD.INFO FILE BUILDER? */
		meta.modId = GreaterSecurity.MOD_ID;
		meta.name = GreaterSecurity.MOD_NAME;
		meta.description = "Helps lock your stuff up so it doesn't get stolen";
		meta.url = "http://www.universalelectricity.com/Greater-Protection";

		meta.logoFile = GreaterSecurity.TEXTURE_PATH + "GP_Banner.png";
		meta.version = GreaterSecurity.VERSION;
		meta.authorList = Arrays.asList(new String[] { "DarkGuardsman", "TheCowGod" });
		meta.credits = "Please see the website.";
		meta.autogenerated = false;

		// // Register ////
		GameRegistry.registerTileEntity(TileEntityLockedChest.class, "LChest");
		GameRegistry.registerTileEntity(TileEntityLockedDoor.class, "LDoor");
		GameRegistry.registerTileEntity(TileEntityLaserFence.class, "LaserFence");
		GameRegistry.registerTileEntity(TileEntityEltroFence.class, "EltroFence");

		// TODO Added string localisation for creative tab
		LanguageRegistry.instance().addStringLocalization("itemGroup.tabGreaterSecurity", "en_US", this.MOD_NAME);

		FMLLog.info(" Loaded: " + TranslationHelper.loadLanguages(LANGUAGE_PATH, LANGUAGES_SUPPORTED) + " Languages.");

	}

	@PostInit
	public void postLoad(FMLPostInitializationEvent event)
	{
		proxy.postInit();

		// // Locked Chest ////
		GameRegistry.addRecipe(new ItemStack(GreaterSecurity.blockLockedChest, 1, 0), new Object[] { "WWW", "WLW", "WWW", 'W', Block.planks, 'L', new ItemStack(GreaterSecurity.itemLock, 1) });
		GameRegistry.addShapelessRecipe(new ItemStack(GreaterSecurity.blockLockedChest, 1), new Object[] { new ItemStack(Block.chest), new ItemStack(GreaterSecurity.itemLock, 1) });

		// // Stone Chest ////
		// GameRegistry.addRecipe(new ItemStack(GreaterSecurity.blockLockedChest, 1, 1), new
		// Object[] { "SSS", "SCS", "SSS", 'S', Block.stone, 'C', GreaterSecurity.blockLockedChest
		// });
		// // Iron Chest ////
		// GameRegistry.addRecipe(new ItemStack(GreaterSecurity.blockLockedChest, 1, 2), new
		// Object[] { "SSS", "SCS", "SSS", 'S', Item.ingotIron, 'C', new
		// ItemStack(GreaterSecurity.blockLockedChest, 1) });
		// // Obby Chest ////
		// GameRegistry.addRecipe(new ItemStack(GreaterSecurity.blockLockedChest, 1, 3), new
		// Object[] { "SSS", "SCS", "SSS", 'S', Block.obsidian, 'C', new
		// ItemStack(GreaterSecurity.blockLockedChest, 1) });
		// // Laser Fence ////
		GameRegistry.addRecipe(new ItemStack(GreaterSecurity.blockLaserFence, 1), new Object[] { "GGG", "CRC", "WBW", 'G', Block.glass, 'C', new ItemStack(GreaterSecurity.blockLockedChest, 1) });

		// // Locked Door ////
		GameRegistry.addRecipe(new ItemStack(GreaterSecurity.itemLockedDoor, 1), new Object[] { "WDW", "WLW", 'D', Item.doorWood, 'W', Block.stone, 'L', new ItemStack(GreaterSecurity.itemLock, 1) });

		// // Item Lock ////
		GameRegistry.addRecipe(new ItemStack(GreaterSecurity.itemLock, 2), new Object[] { "III", "I I", "WIW", 'W', Block.wood, 'I', Item.ingotIron });

	}
}
