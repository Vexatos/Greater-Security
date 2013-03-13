package hangcow.greatersecurity.common.chest;

import hangcow.greatersecurity.common.GreaterSecurity;

import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.ForgeDirection;
import dark.library.locking.AccessLevel;
import dark.library.locking.prefab.TileEntityLockable;

public class TileEntityLockedChest extends TileEntityLockable implements IInventory
{
	private ItemStack[] chestContents = new ItemStack[36];

	/** Determines if the check for adjacent chests has taken place. */
	public boolean adjacentChestChecked = false;

	/** Contains the chest tile located adjacent to this one (if any) */
	public TileEntityLockedChest adjacentChestZNeg;

	/** Contains the chest tile located adjacent to this one (if any) */
	public TileEntityLockedChest adjacentChestXPos;

	/** Contains the chest tile located adjacent to this one (if any) */
	public TileEntityLockedChest adjacentChestXNeg;

	/** Contains the chest tile located adjacent to this one (if any) */
	public TileEntityLockedChest adjacentChestZPosition;

	/** The current angle of the lid (between 0 and 1) */
	public float lidAngle;

	/** The angle of the lid last tick */
	public float prevLidAngle;

	/** The number of players currently using this chest */
	public int numUsingPlayers;

	/**
	 * Returns the number of slots in the inventory.
	 */
	public int getSizeInventory()
	{
		return 27;
	}

	/**
	 * Returns the stack in slot i
	 */
	public ItemStack getStackInSlot(int slot)
	{
		return this.chestContents[slot];
	}

	/**
	 * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and
	 * returns them in a new stack.
	 */
	public ItemStack decrStackSize(int slot, int ammount)
	{
		if (this.chestContents[slot] != null)
		{
			ItemStack var3;

			if (this.chestContents[slot].stackSize <= ammount)
			{
				var3 = this.chestContents[slot];
				this.chestContents[slot] = null;
				this.onInventoryChanged();
				return var3;
			}
			else
			{
				var3 = this.chestContents[slot].splitStack(ammount);

				if (this.chestContents[slot].stackSize == 0)
				{
					this.chestContents[slot] = null;
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
	public String getInvName()
	{
		return "container.chest";
	}

	/**
	 * Reads a tile entity from NBT.
	 */
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		// // Check for old save list and convert //
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
		// // Read inventory //
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
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
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

		nbt.setTag("Items", var2);
	}

	/**
	 * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be
	 * extended. *Isn't this more of a set than a get?*
	 */
	public int getInventoryStackLimit()
	{
		return 64;
	}

	/**
	 * Do not make give this method the name canInteractWith because it clashes with Container
	 */
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

	private void hasConnectedChest(TileEntityLockedChest chest, int direction)
	{
		if (chest.isInvalid())
		{
			this.adjacentChestChecked = false;
		}
		else if (this.adjacentChestChecked)
		{
			switch (direction)
			{
				case 0:
					if (this.adjacentChestZPosition != chest)
					{
						this.adjacentChestChecked = false;
					}

					break;
				case 1:
					if (this.adjacentChestXNeg != chest)
					{
						this.adjacentChestChecked = false;
					}

					break;
				case 2:
					if (this.adjacentChestZNeg != chest)
					{
						this.adjacentChestChecked = false;
					}

					break;
				case 3:
					if (this.adjacentChestXPos != chest)
					{
						this.adjacentChestChecked = false;
					}
			}
		}
	}

	/**
	 * Performs the check for adjacent chests to determine if this chest is double or not.
	 */
	public void checkForAdjacentChests()
	{
		if (!this.adjacentChestChecked)
		{
			this.adjacentChestChecked = true;
			this.adjacentChestZNeg = null;
			this.adjacentChestXPos = null;
			this.adjacentChestXNeg = null;
			this.adjacentChestZPosition = null;

			if (this.worldObj.getBlockId(this.xCoord - 1, this.yCoord, this.zCoord) == Block.chest.blockID)
			{
				this.adjacentChestXNeg = (TileEntityLockedChest) this.worldObj.getBlockTileEntity(this.xCoord - 1, this.yCoord, this.zCoord);
			}

			if (this.worldObj.getBlockId(this.xCoord + 1, this.yCoord, this.zCoord) == Block.chest.blockID)
			{
				this.adjacentChestXPos = (TileEntityLockedChest) this.worldObj.getBlockTileEntity(this.xCoord + 1, this.yCoord, this.zCoord);
			}

			if (this.worldObj.getBlockId(this.xCoord, this.yCoord, this.zCoord - 1) == Block.chest.blockID)
			{
				this.adjacentChestZNeg = (TileEntityLockedChest) this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord - 1);
			}

			if (this.worldObj.getBlockId(this.xCoord, this.yCoord, this.zCoord + 1) == Block.chest.blockID)
			{
				this.adjacentChestZPosition = (TileEntityLockedChest) this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord + 1);
			}

			if (this.adjacentChestZNeg != null)
			{
				this.adjacentChestZNeg.hasConnectedChest(this, 0);
			}

			if (this.adjacentChestZPosition != null)
			{
				this.adjacentChestZPosition.hasConnectedChest(this, 2);
			}

			if (this.adjacentChestXPos != null)
			{
				this.adjacentChestXPos.hasConnectedChest(this, 1);
			}

			if (this.adjacentChestXNeg != null)
			{
				this.adjacentChestXNeg.hasConnectedChest(this, 3);
			}
		}
	}

	/**
	 * Allows the entity to update its state. Overridden in most subclasses, e.g. the mob spawner
	 * uses this to count ticks and creates a new spawn inside its implementation.
	 */
	public void updateEntity()
	{
		super.updateEntity();
		this.checkForAdjacentChests();
		float var1;

		if (!this.worldObj.isRemote && this.numUsingPlayers != 0 && (this.ticks + this.xCoord + this.yCoord + this.zCoord) % 200 == 0)
		{
			this.numUsingPlayers = 0;
			var1 = 5.0F;
			List var2 = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.getAABBPool().addOrModifyAABBInPool((double) ((float) this.xCoord - var1), (double) ((float) this.yCoord - var1), (double) ((float) this.zCoord - var1), (double) ((float) (this.xCoord + 1) + var1), (double) ((float) (this.yCoord + 1) + var1), (double) ((float) (this.zCoord + 1) + var1)));
			Iterator var3 = var2.iterator();

			while (var3.hasNext())
			{
				EntityPlayer var4 = (EntityPlayer) var3.next();

				if (var4.openContainer instanceof ContainerChest)
				{
					IInventory var5 = ((ContainerChest) var4.openContainer).getLowerChestInventory();

					if (var5 == this || var5 instanceof InventoryLargeChest && ((InventoryLargeChest) var5).isPartOfLargeChest(this))
					{
						++this.numUsingPlayers;
					}
				}
			}
		}

		this.prevLidAngle = this.lidAngle;
		var1 = 0.1F;
		double var11;

		if (this.numUsingPlayers > 0 && this.lidAngle == 0.0F && this.adjacentChestZNeg == null && this.adjacentChestXNeg == null)
		{
			double var8 = (double) this.xCoord + 0.5D;
			var11 = (double) this.zCoord + 0.5D;

			if (this.adjacentChestZPosition != null)
			{
				var11 += 0.5D;
			}

			if (this.adjacentChestXPos != null)
			{
				var8 += 0.5D;
			}

			this.worldObj.playSoundEffect(var8, (double) this.yCoord + 0.5D, var11, "random.chestopen", 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
		}

		if (this.numUsingPlayers == 0 && this.lidAngle > 0.0F || this.numUsingPlayers > 0 && this.lidAngle < 1.0F)
		{
			float var9 = this.lidAngle;

			if (this.numUsingPlayers > 0)
			{
				this.lidAngle += var1;
			}
			else
			{
				this.lidAngle -= var1;
			}

			if (this.lidAngle > 1.0F)
			{
				this.lidAngle = 1.0F;
			}

			float var10 = 0.5F;

			if (this.lidAngle < var10 && var9 >= var10 && this.adjacentChestZNeg == null && this.adjacentChestXNeg == null)
			{
				var11 = (double) this.xCoord + 0.5D;
				double var6 = (double) this.zCoord + 0.5D;

				if (this.adjacentChestZPosition != null)
				{
					var6 += 0.5D;
				}

				if (this.adjacentChestXPos != null)
				{
					var11 += 0.5D;
				}

				this.worldObj.playSoundEffect(var11, (double) this.yCoord + 0.5D, var6, "random.chestclosed", 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
			}

			if (this.lidAngle < 0.0F)
			{
				this.lidAngle = 0.0F;
			}
		}
	}

	/**
	 * Called when a client event is received with the event number and argument, see
	 * World.sendClientEvent
	 */
	public void receiveClientEvent(int par1, int par2)
	{
		if (par1 == 1)
		{
			this.numUsingPlayers = par2;
		}
	}

	public void openChest()
	{
		++this.numUsingPlayers;
		this.worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, Block.chest.blockID, 1, this.numUsingPlayers);
	}

	public void closeChest()
	{
		--this.numUsingPlayers;
		this.worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, Block.chest.blockID, 1, this.numUsingPlayers);
	}

	/**
	 * invalidates a tile entity
	 */
	public void invalidate()
	{
		super.invalidate();
		this.updateContainingBlockInfo();
		this.checkForAdjacentChests();
	}

	@Override
	public String getChannel()
	{
		return GreaterSecurity.CHANNEL;
	}

	public TileEntityLockedChest getAdjacentChest()
	{
		for (int side = 0; side < 4; side++)
		{
			ForgeDirection dir = ForgeDirection.getOrientation(side + 2);

			TileEntity entity = worldObj.getBlockTileEntity(this.xCoord + dir.offsetX, this.yCoord, this.zCoord + dir.offsetZ);

			if (entity instanceof TileEntityLockedChest)
			{
				return (TileEntityLockedChest) entity;
			}
		}
		return null;
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
