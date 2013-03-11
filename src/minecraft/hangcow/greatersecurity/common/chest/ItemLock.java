package hangcow.greatersecurity.common.chest;

import hangcow.greatersecurity.common.GreaterSecurity;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * 
 * @author DarkGuardsman, TheCowGod
 * 
 */
public class ItemLock extends Item
{

	public ItemLock(int par1)
	{
		super(par1);
		this.setHasSubtypes(true);
		maxStackSize = 10;
		setMaxDamage(0);
		this.setCreativeTab(CreativeTabs.tabTools);
		this.setIconIndex(1);
	}

	@Override
	public String getTextureFile()
	{
		return GreaterSecurity.ITEM_File_PATH;

	}

	@Override
	public String getItemNameIS(ItemStack itemstack)
	{
		return "Lock";
	}

	@Override
	public int getIconFromDamage(int i)
	{
		return this.iconIndex + i;
	}

	@Override
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		par3List.add(new ItemStack(this, 1, 0));
	}
	/**
	 * public boolean tryPlaceIntoWorld(ItemStack itemstack, EntityPlayer entityplayer, World world,
	 * int i, int j, int k, int l, float par8, float par9, float par10) { if(entityplayer != null &&
	 * !world.isRemote) { TileEntity entity = world.getBlockTileEntity(i, j, k); if(entity
	 * instanceof TileEntityChest) { TileEntityChest chest = ((TileEntityChest)entity); int
	 * chestSize = chest.getSizeInventory(); ItemStack[] chestItems = new ItemStack[chestSize]; int
	 * lockSize = ((TileEntityLockedChest)entity).getSizeInventory(); for(int slot = 0; slot <
	 * chestSize && slot < lockSize;slot++) { chestItems[slot] = chest.getStackInSlot(slot); }
	 * world.setBlockAndMetadataWithUpdate(i, j, k, 0, 0, true); world.removeBlockTileEntity(i, j,
	 * k); world.setBlockAndMetadataWithUpdate(i, j, j,
	 * GreaterSecurityMain.BlockLockedChest.blockID, 0, true);
	 * GreaterSecurityMain.BlockLockedChest.onBlockPlacedBy(world, i, j, k, entityplayer);
	 * TileEntity entity2 = world.getBlockTileEntity(i, j, k); if(entity2 instanceof
	 * TileEntityLockedChest) { for(int b = 0; b< lockSize; b++) { ((TileEntityLockedChest)
	 * entity2).setInventorySlotContents(b, chestItems[b]); } }
	 * entityplayer.sendChatToPlayer("Chest Locked"); } } return true;
	 * 
	 * }
	 */
}
