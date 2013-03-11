package hangcow.greatersecurity.common;

import hangcow.greatersecurity.common.chest.BlockLockedChest;
import hangcow.greatersecurity.common.chest.ItemLock;
import hangcow.greatersecurity.common.chest.ItemLockedChest;
import hangcow.greatersecurity.common.chest.TileEntityLockedChest;
import hangcow.greatersecurity.common.door.BlockLockedDoor;
import hangcow.greatersecurity.common.door.ItemDoor;
import hangcow.greatersecurity.common.door.TileEntityFake;
import hangcow.greatersecurity.common.door.TileEntityLockedDoor;
import hangcow.greatersecurity.common.network.LockPacketHandler;

import java.io.File;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
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
@Mod(modid = "GreaterSecurity", name = "GreaterSecurity", version = "0.1.1A")
@NetworkMod(channels = { "GreaterSecurity" }, clientSideRequired = true, serverSideRequired = false, packetHandler = LockPacketHandler.class)
public class GreaterSecurity
{

    static Configuration config = new Configuration(new File(Loader.instance().getConfigDir(), "GreaterSecurity.cfg"));

    @SidedProxy(clientSide = "hangcow.greatersecurity.client.ClientProxy", serverSide = "hangcow.greatersecurity.common.CommonProxy")
    public static CommonProxy proxy;
    public static GreaterSecurity instance;
    public static final String RESOURCE_PATH = "/hangcow/greatersecurity/resources/";
    public static final String ITEM_File_PATH = RESOURCE_PATH + "Items.png";
    public static final String GUI_File_PATH = RESOURCE_PATH + "gui/";
    // //Blocks /////
    public static Block BlockLChest;
    public static Block BlockLDoor;

    // //Items ////
    public static Item Lock;
    public static Item door;

    public static void LoadConfigs()
    {
        config.load();
        // //Items //// Configs ////
        Lock = new ItemLock(config.getItem(Configuration.CATEGORY_ITEM, "LockItemID", 30007).getInt());
        door = new ItemDoor(config.getItem(Configuration.CATEGORY_ITEM, "DoorItemID", 30008).getInt());
        // //Blocks //// Configs ////
        BlockLChest = new BlockLockedChest(config.getBlock(Configuration.CATEGORY_BLOCK, "LockedChestID", 1777).getInt());
        BlockLDoor = new BlockLockedDoor(config.getBlock(Configuration.CATEGORY_BLOCK, "LockedDoorID", 1714).getInt());
        config.save();
    }

    @PreInit
    public void preInit(FMLPreInitializationEvent event)
    {
        instance = this;
        NetworkRegistry.instance().registerGuiHandler(this, this.proxy);
        LoadConfigs();
        proxy.preInit();
        
        // // Registration ////
        GameRegistry.registerBlock(BlockLChest, ItemLockedChest.class, "gsChest");
        GameRegistry.registerBlock(BlockLDoor, "gsDoor");

    }

    @Init
    public void generalLoad(FMLInitializationEvent event)
    {
        proxy.init();
        GameRegistry.registerTileEntity(TileEntityLockedChest.class, "LChest");
        GameRegistry.registerTileEntity(TileEntityLockedDoor.class, "LDoor");
        
        // // Registration ////
        GameRegistry.registerTileEntity(TileEntityFake.class, "LTDoor");

        // // Block Names ////
        LanguageRegistry.addName(new ItemStack(GreaterSecurity.BlockLChest, 1, 0),
                "WoodenChest");
        LanguageRegistry
                .addName(new ItemStack(GreaterSecurity.BlockLChest, 1, 1), "StoneChest");
        LanguageRegistry.addName(new ItemStack(GreaterSecurity.BlockLChest, 1, 2), "IronChest");
        LanguageRegistry.addName(new ItemStack(GreaterSecurity.BlockLChest, 1, 3), "ObbyChest");
        LanguageRegistry.addName(new ItemStack(GreaterSecurity.door, 1, 0), "StoneDoor");

        // // Item Names ////
        LanguageRegistry.addName(new ItemStack(GreaterSecurity.Lock, 1), "KeyLock");

    }

    @PostInit
    public void postLoad(FMLPostInitializationEvent event)
    {
        proxy.postInit();
        // // Block Recipes ////

        // // Chest ////
        GameRegistry.addRecipe(new ItemStack(GreaterSecurity.BlockLChest, 1, 0), new Object[] {
                "WWW", "WLW", "WWW", 
                'W', Block.planks,
                'L', new ItemStack(GreaterSecurity.Lock, 1) });
        GameRegistry.addShapelessRecipe(new ItemStack(GreaterSecurity.BlockLChest, 1),
                new Object[] { new ItemStack(Block.chest),
                        new ItemStack(GreaterSecurity.Lock, 1) });

        // // ReinforcedChests ////
        GameRegistry.addRecipe(new ItemStack(GreaterSecurity.BlockLChest, 1, 1), new Object[] {
                "SSS", "SCS", "SSS", 
                'S', Block.stone,
                'C', GreaterSecurity.BlockLChest});
        GameRegistry.addRecipe(new ItemStack(GreaterSecurity.BlockLChest, 1, 2), new Object[] {
                "SSS", "SCS", "SSS", 
                'S', Item.ingotIron,
                'C', new ItemStack(GreaterSecurity.BlockLChest, 1) });
        GameRegistry.addRecipe(new ItemStack(GreaterSecurity.BlockLChest, 1, 3), new Object[] {
                "SSS", "SCS", "SSS", 
                'S', Block.obsidian,
                'C', new ItemStack(GreaterSecurity.BlockLChest, 1) });

        // // Door ////
        GameRegistry.addRecipe(new ItemStack(GreaterSecurity.door, 1),
                new Object[] { "WDW", "WLW", 
                'D', Item.doorWood, 
                'W', Block.stone,
                'L', new ItemStack(GreaterSecurity.Lock, 1) });

        // // Item Recipes ////
        GameRegistry.addRecipe(new ItemStack(GreaterSecurity.Lock, 2),
                new Object[] { "III", "I I", "WIW", 
                'W', Block.wood, 
                'I', Item.ingotIron });
    }
}
