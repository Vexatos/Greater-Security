package hangcow.greatersecurity.common.chest;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;

public class InvChest implements ISidedInventory
{
	private int[] chestSlots;
	private TileEntityLockedChest hostChest;

	public InvChest(TileEntityLockedChest chest)
	{
		this.hostChest = chest;
	}

	@Override
	public int getSizeInventory()
	{
		return 27;
	}

	@Override
	public ItemStack getStackInSlot(int slot)
	{
		return this.hostChest.getContainingItems()[slot];
	}

	@Override
	public ItemStack decrStackSize(int slot, int ammount)
	{
		if (this.hostChest.getContainingItems()[slot] != null)
		{
			ItemStack var3;

			if (this.hostChest.getContainingItems()[slot].stackSize <= ammount)
			{
				var3 = this.hostChest.getContainingItems()[slot];
				this.hostChest.getContainingItems()[slot] = null;
				this.onInventoryChanged();
				return var3;
			}
			else
			{
				var3 = this.hostChest.getContainingItems()[slot].splitStack(ammount);

				if (this.hostChest.getContainingItems()[slot].stackSize == 0)
				{
					this.hostChest.getContainingItems()[slot] = null;
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

	@Override
	public ItemStack getStackInSlotOnClosing(int par1)
	{
		if (this.hostChest.getContainingItems()[par1] != null)
		{
			ItemStack var2 = this.hostChest.getContainingItems()[par1];
			this.hostChest.getContainingItems()[par1] = null;
			return var2;
		}
		else
		{
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
	{
		this.hostChest.getContainingItems()[par1] = par2ItemStack;

		if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit())
		{
			par2ItemStack.stackSize = this.getInventoryStackLimit();
		}

		this.onInventoryChanged();
	}

	@Override
	public String getInvName()
	{
		return "container.chest";
	}

	@Override
	public void openChest()
	{

	}

	@Override
	public void closeChest()
	{

	}

	@Override
	public boolean isInvNameLocalized()
	{
		return false;
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack)
	{
		// TODO maybe add a way to restrict item per slot
		return true;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int var1)
	{
		if (chestSlots == null)
		{
			this.chestSlots = new int[this.getSizeInventory()];
			for (int i = 0; i < this.chestSlots.length; i++)
			{
				chestSlots[i] = i;
			}
		}
		return this.chestSlots;
	}

	@Override
	public boolean canInsertItem(int i, ItemStack itemstack, int j)
	{
		return this.isItemValidForSlot(i, itemstack) && this.hostChest.canInsertItem(i, itemstack, j);
	}

	@Override
	public boolean canExtractItem(int i, ItemStack itemstack, int j)
	{
		return this.isItemValidForSlot(i, itemstack) && this.hostChest.canExtractItem(i, itemstack, j);
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	@Override
	public void onInventoryChanged()
	{
		this.hostChest.onInventoryChanged();
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
	{
		return this.hostChest.worldObj.getBlockTileEntity(this.hostChest.xCoord, this.hostChest.yCoord, this.hostChest.zCoord) != this.hostChest ? false : par1EntityPlayer.getDistanceSq((double) this.hostChest.xCoord + 0.5D, (double) this.hostChest.yCoord + 0.5D, (double) this.hostChest.zCoord + 0.5D) <= 64.0D;
	}

}
