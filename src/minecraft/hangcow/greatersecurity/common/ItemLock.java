package hangcow.greatersecurity.common;

import hangcow.greatersecurity.common.chest.TileEntityLockedChest;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import dark.library.locking.AccessLevel;
import dark.library.locking.UserAccess;

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
		this.setUnlocalizedName("Lock");
		maxStackSize = 10;
		setMaxDamage(0);
		this.setCreativeTab(CreativeTabs.tabTools);
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
				if (world.setBlock(i, j, k, GreaterSecurity.blockLockedChest.blockID, 0, 3))
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
								lockedChest.addUserAccess(new UserAccess(user.username, user.level, user.shouldSave), true);
							}
						}
						else
						{
							lockedChest.addUserAccess(new UserAccess(entityplayer.username, AccessLevel.OWNER, true), true);
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
