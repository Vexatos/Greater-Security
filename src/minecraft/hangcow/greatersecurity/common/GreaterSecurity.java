package hangcow.greatersecurity.common;

import hangcow.greatersecurity.common.chest.BlockLockedChest;
import hangcow.greatersecurity.common.chest.TileEntityLockedChest;
import hangcow.greatersecurity.common.door.BlockLockedDoor;
import hangcow.greatersecurity.common.door.ItemLockedDoor;
import hangcow.greatersecurity.common.door.TileEntityLockedDoor;

import java.io.File;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import universalelectricity.prefab.network.PacketManager;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

/**
 * 
 * @author CowGod, Darkguardsman
 * 
 */
@Mod(modid = GreaterSecurity.MOD_NAME, name = GreaterSecurity.MOD_NAME, version = "0.3.1")
// TODO update version #
@NetworkMod(channels = { GreaterSecurity.CHANNEL }, clientSideRequired = true, serverSideRequired = false, packetHandler = PacketManager.class)
public class GreaterSecurity
{
	

	@SidedProxy(clientSide = "hangcow.greatersecurity.client.ClientProxy", serverSide = "hangcow.greatersecurity.common.CommonProxy")
	public static CommonProxy proxy;

	public static final String MOD_NAME = "Greater Security";
	public static final String MOD_ID = "GreaterSecurity";
	public static final String CHANNEL = "GreaterSecurity";
	
	public static final String RESOURCE_PATH = "/mods/greatersecurity/";
	public static final String TEXTURE_PATH = RESOURCE_PATH + "textures/";
	public static final String ITEM_File_PATH = TEXTURE_PATH + "items/";
	public static final String BLOCK_File_PATH = TEXTURE_PATH + "blocks/";
	public static final String MODEL_File_PATH = TEXTURE_PATH + "models/";
	public static final String GUI_File_PATH = TEXTURE_PATH + "gui/";
	public static final String TEXTURE_NAME_PREFIX = "greatersecurity:";

	private static Configuration config = new Configuration(new File(Loader.instance().getConfigDir(), MOD_ID+".cfg"));

	@Instance(MOD_NAME)
	public static GreaterSecurity instance;
	/* BLOCKS */
	public static Block blockLockedChest;
	public static Block blockLockedDoor;

	/* ITEMS */
	public static Item itemLock;
	public static Item itemLockedDoor;
	
	/* CONFIG VARS */
	public static Boolean breakChests;
	public static Boolean breakDoors;

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

		blockLockedChest = new BlockLockedChest(config.getBlock(Configuration.CATEGORY_BLOCK, "LockedChestID", 1777).getInt());
		blockLockedDoor = new BlockLockedDoor(config.getBlock(Configuration.CATEGORY_BLOCK, "LockedDoorID", 1714).getInt());

		config.save();

		// // Registration ////
		GameRegistry.registerBlock(blockLockedChest, "gsChest");
		GameRegistry.registerBlock(blockLockedDoor, "gsDoor");

		proxy.preInit();

	}

	@Init
	public void generalLoad(FMLInitializationEvent event)
	{
		proxy.init();
		// // Register ////
		GameRegistry.registerTileEntity(TileEntityLockedChest.class, "LChest");
		GameRegistry.registerTileEntity(TileEntityLockedDoor.class, "LDoor");

		// // Block Names ////
		LanguageRegistry.addName(new ItemStack(GreaterSecurity.blockLockedChest, 1, 0), "WoodenChest");
		LanguageRegistry.addName(new ItemStack(GreaterSecurity.blockLockedChest, 1, 1), "StoneChest");
		LanguageRegistry.addName(new ItemStack(GreaterSecurity.blockLockedChest, 1, 2), "IronChest");
		LanguageRegistry.addName(new ItemStack(GreaterSecurity.blockLockedChest, 1, 3), "ObbyChest");
		LanguageRegistry.addName(new ItemStack(GreaterSecurity.itemLockedDoor, 1, 0), "StoneDoor");

		// // Item Names ////
		LanguageRegistry.addName(new ItemStack(GreaterSecurity.itemLock, 1), "KeyLock");

	}

	@PostInit
	public void postLoad(FMLPostInitializationEvent event)
	{
		proxy.postInit();

		// // Locked Chest ////
		GameRegistry.addRecipe(new ItemStack(GreaterSecurity.blockLockedChest, 1, 0), new Object[] { "WWW", "WLW", "WWW", 'W', Block.planks, 'L', new ItemStack(GreaterSecurity.itemLock, 1) });
		GameRegistry.addShapelessRecipe(new ItemStack(GreaterSecurity.blockLockedChest, 1), new Object[] { new ItemStack(Block.chest), new ItemStack(GreaterSecurity.itemLock, 1) });

		// // Stone Chest ////
		GameRegistry.addRecipe(new ItemStack(GreaterSecurity.blockLockedChest, 1, 1), new Object[] { "SSS", "SCS", "SSS", 'S', Block.stone, 'C', GreaterSecurity.blockLockedChest });
		// // Iron Chest ////
		GameRegistry.addRecipe(new ItemStack(GreaterSecurity.blockLockedChest, 1, 2), new Object[] { "SSS", "SCS", "SSS", 'S', Item.ingotIron, 'C', new ItemStack(GreaterSecurity.blockLockedChest, 1) });
		// // Obby Chest ////
		GameRegistry.addRecipe(new ItemStack(GreaterSecurity.blockLockedChest, 1, 3), new Object[] { "SSS", "SCS", "SSS", 'S', Block.obsidian, 'C', new ItemStack(GreaterSecurity.blockLockedChest, 1) });

		// // Locked Door ////
		GameRegistry.addRecipe(new ItemStack(GreaterSecurity.itemLockedDoor, 1), new Object[] { "WDW", "WLW", 'D', Item.doorWood, 'W', Block.stone, 'L', new ItemStack(GreaterSecurity.itemLock, 1) });

		// // Item Lock ////
		GameRegistry.addRecipe(new ItemStack(GreaterSecurity.itemLock, 2), new Object[] { "III", "I I", "WIW", 'W', Block.wood, 'I', Item.ingotIron });
	}
}
