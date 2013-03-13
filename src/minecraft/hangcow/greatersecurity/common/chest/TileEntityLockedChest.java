package hangcow.greatersecurity.common.chest;

import java.util.List;

import hangcow.greatersecurity.common.GreaterSecurity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import dark.library.locking.AccessLevel;
import dark.library.locking.UserAccess;
import dark.library.locking.prefab.TileEntityLockable;

public class TileEntityLockedChest extends TileEntityLockable implements IInventory
{
	public ItemStack[] chestContents = new ItemStack[36];

	public boolean adjacentChestChecked = false;

	public TileEntityLockedChest[] chests = { null, null, null, null };

	public float prevLidAngle;
	public float lidAngle;

	/**
	 * Returns the number of slots in the inventory.
	 */
	@Override
	public int getSizeInventory()
	{
		return 27;
	}

	/**
	 * Returns the stack in slot i
	 */
	@Override
	public ItemStack getStackInSlot(int par1)
	{
		return this.chestContents[par1];
	}

	public void dropBlockAsItem_do(World par1World, int par2, int par3, int par4, ItemStack par5ItemStack)
	{
		if (!par1World.isRemote)
		{
			float var6 = 0.7F;
			double var7 = (double) (par1World.rand.nextFloat() * var6) + (double) (1.0F - var6) * 0.5D;
			double var9 = (double) (par1World.rand.nextFloat() * var6) + (double) (1.0F - var6) * 0.5D;
			double var11 = (double) (par1World.rand.nextFloat() * var6) + (double) (1.0F - var6) * 0.5D;
			EntityItem var13 = new EntityItem(par1World, (double) par2 + var7, (double) par3 + var9, (double) par4 + var11, par5ItemStack);
			var13.delayBeforeCanPickup = 10;
			par1World.spawnEntityInWorld(var13);
		}
	}

	/**
	 * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and
	 * returns them in a new stack.
	 */
	@Override
	public ItemStack decrStackSize(int par1, int par2)
	{
		if (this.chestContents[par1] != null)
		{
			ItemStack var3;

			if (this.chestContents[par1].stackSize <= par2)
			{
				var3 = this.chestContents[par1];
				this.chestContents[par1] = null;
				this.onInventoryChanged();
				return var3;
			}
			else
			{
				var3 = this.chestContents[par1].splitStack(par2);

				if (this.chestContents[par1].stackSize == 0)
				{
					this.chestContents[par1] = null;
				}

				this.onInventoryChanged();
				return var3;
			}
		}
		else
		{
			return null;
		}
	}

	/**
	 * When some containers are closed they call this on each slot, then drop whatever it returns as
	 * an EntityItem - like when you close a workbench GUI.
	 */
	@Override
	public ItemStack getStackInSlotOnClosing(int par1)
	{
		if (this.chestContents[par1] != null)
		{
			ItemStack var2 = this.chestContents[par1];
			this.chestContents[par1] = null;
			return var2;
		}
		else
		{
			return null;
		}
	}

	/**
	 * Sets the given item stack to the specified slot in the inventory (can be crafting or armor
	 * sections).
	 */
	@Override
	public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
	{
		this.chestContents[par1] = par2ItemStack;

		if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit())
		{
			par2ItemStack.stackSize = this.getInventoryStackLimit();
		}

		this.onInventoryChanged();
	}

	/**
	 * Returns the name of the inventory.
	 */
	@Override
	public String getInvName()
	{
		return "container.LockedChest";
	}

	/**
	 * Reads a tile entity from NBT.
	 */
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);

		if (nbt.hasKey("Owner"))
		{
			this.addUserAccess(nbt.getString("Owner"), AccessLevel.OWNER, true);
		}
		if (nbt.hasKey("users"))
		{
			int userSize = nbt.getInteger("users");
			for (int i = 0; i < userSize; i++)
			{
				String read = nbt.getString("user" + i);
				this.addUserAccess(read, AccessLevel.USER, true);
			}
		}
		// chest inv reading
		NBTTagList var2 = nbt.getTagList("Items");
		this.chestContents = new ItemStack[this.getSizeInventory()];
		for (int var3 = 0; var3 < var2.tagCount(); ++var3)
		{
			NBTTagCompound var4 = (NBTTagCompound) var2.tagAt(var3);
			int var5 = var4.getByte("Slot") & 255;

			if (var5 >= 0 && var5 < this.chestContents.length)
			{
				this.chestContents[var5] = ItemStack.loadItemStackFromNBT(var4);
			}
		}
	}

	/**
	 * Writes a tile entity to NBT.
	 */
	@Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.writeToNBT(par1NBTTagCompound);
		// chest inv write
		NBTTagList var2 = new NBTTagList();
		for (int var3 = 0; var3 < this.chestContents.length; ++var3)
		{
			if (this.chestContents[var3] != null)
			{
				NBTTagCompound var4 = new NBTTagCompound();
				var4.setByte("Slot", (byte) var3);
				this.chestContents[var3].writeToNBT(var4);
				var2.appendTag(var4);
			}
		}

		par1NBTTagCompound.setTag("Items", var2);
	}

	/**
	 * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be
	 * extended. *Isn't this more of a set than a get?*
	 */
	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	/**
	 * Do not make give this method the name canInteractWith because it clashes with Container
	 */
	@Override
	public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
	{
		return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : par1EntityPlayer.getDistanceSq((double) this.xCoord + 0.5D, (double) this.yCoord + 0.5D, (double) this.zCoord + 0.5D) <= 64.0D;
	}

	/**
	 * Causes the TileEntity to reset all it's cached values for it's container block, blockID,
	 * metaData and in the case of chests, the adjcacent chest check
	 */
	public void updateContainingBlockInfo()
	{
		super.updateContainingBlockInfo();
		this.adjacentChestChecked = false;
	}

	/**
	 * Performs the check for adjacent chests to determine if this chest is double or not.
	 */
	public void checkForAdjacentChests()
	{
		if (!this.adjacentChestChecked)
		{
			this.adjacentChestChecked = true;
			this.chests[0] = null;
			this.chests[1] = null;
			this.chests[2] = null;
			this.chests[3] = null;
			for (int i = 0; i < 4; i++)
			{
				int deltaX = 0;
				int deltaZ = 0;
				switch (i)
				{
					case 0:
						deltaX--;
						break;
					case 1:
						deltaX++;
						break;
					case 2:
						deltaZ--;
						break;
					case 3:
						deltaZ++;
						break;
				}
				TileEntity entity = this.worldObj.getBlockTileEntity(this.xCoord + deltaX, this.yCoord, this.zCoord + deltaZ);
				if (entity instanceof TileEntityLockedChest)
				{
					if (((TileEntityLockedChest) entity).getType() == this.getType())
					{
						this.chests[i] = (TileEntityLockedChest) entity;
					}
					if (this.chests[i] != null)
					{
						this.chests[i].updateContainingBlockInfo();
					}
				}
			}
		}
	}

	public TileEntityLockedChest getAdjacentChest()
	{
		for (int side = 0; side < 4; side++)
		{
			ForgeDirection dir = ForgeDirection.getOrientation(side + 2);

			TileEntity entity = worldObj.getBlockTileEntity(this.xCoord + dir.offsetX, this.yCoord, this.zCoord + dir.offsetZ);

			if (entity instanceof TileEntityLockedChest && ((TileEntityLockedChest) entity).getType() == this.getType())
			{
				return (TileEntityLockedChest) entity;
			}
		}
		return null;
	}

	@Override
	public void openChest()
	{
		++this.playersUsing;
	}

	@Override
	public void closeChest()
	{
		--this.playersUsing;
	}

	/**
	 * invalidates a tile entity
	 */
	@Override
	public void invalidate()
	{
		this.updateContainingBlockInfo();
		this.checkForAdjacentChests();
		super.invalidate();
	}

	public int getType()
	{
		int meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		int type = 0;
		if (meta < 4 && meta > 0)
		{
			type = 0;
		}
		else if (meta < 8 && meta > 3)
		{
			type = 1;
		}
		else if (meta < 12 && meta > 7)
		{
			type = 2;
		}
		else if (meta < 16 && meta > 11)
		{
			type = 3;
		}

		return type;
	}

	@Override
	public String getChannel()
	{
		return GreaterSecurity.CHANNEL;
	}

	@Override
	public boolean addUserAccess(String player, AccessLevel lvl, boolean save)
	{
		TileEntityLockedChest chest = this.getAdjacentChest();
		if (!worldObj.isRemote && chest != null && chest.getUserAccess(player) != lvl)
		{
			chest.addUserAccess(player, lvl, save);
		}
		return super.addUserAccess(player, lvl, save);
	}

	@Override
	public boolean removeUserAccess(String player)
	{
		TileEntityLockedChest chest = this.getAdjacentChest();
		if (!worldObj.isRemote && chest != null && chest.isOnList(player))
		{
			chest.removeUserAccess(player);
		}
		return super.removeUserAccess(player);
	}
}
