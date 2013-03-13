package hangcow.greatersecurity.common;

import hangcow.greatersecurity.common.chest.TileEntityLockedChest;

import java.util.List;

import dark.library.locking.AccessLevel;
import dark.library.locking.UserAccess;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.World;

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

	@Override
	public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l, float par8, float par9, float par10)
	{
		if (entityplayer != null && !world.isRemote)
		{
			TileEntity entity = world.getBlockTileEntity(i, j, k);

			if (entity.getClass().equals(TileEntityChest.class))
			{
				// // Get current chest //
				TileEntityChest chest = ((TileEntityChest) entity);
				ItemStack[] chestItems = new ItemStack[chest.getSizeInventory()];
				// // Get current chest inventory //
				for (int slot = 0; slot < chest.getSizeInventory(); slot++)
				{
					chestItems[slot] = chest.getStackInSlot(slot);
					chest.setInventorySlotContents(slot, null);
				}
				// // replace chest with locked chest //
				if (world.setBlockAndMetadataWithUpdate(i, j, k, GreaterSecurity.blockLockedChest.blockID, 0, true))
				{

					TileEntity newChest = world.getBlockTileEntity(i, j, k);
					if (newChest instanceof TileEntityLockedChest)
					{
						TileEntityLockedChest lockedChest = (TileEntityLockedChest) newChest;
						TileEntityLockedChest connectedChest = lockedChest.getAdjacentChest();
						
						if (connectedChest != null)
						{
							for (UserAccess user : connectedChest.getUsers())
							{
								lockedChest.addUserAccess(user.username, user.level, user.shouldSave);
							}
						}
						else
						{
							lockedChest.addUserAccess(entityplayer.username, AccessLevel.OWNER, true);
						}
						// // add chest inv to new locked chest //
						for (int b = 0; b < lockedChest.getSizeInventory(); b++)
						{
							lockedChest.setInventorySlotContents(b, chestItems[b]);

						}
						entityplayer.sendChatToPlayer("Chest Locked");
					}

				}
				else
				{
					entityplayer.sendChatToPlayer("Failed to lock chest");
				}
			}
		}
		return true;

	}
}
