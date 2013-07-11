package hangcow.greatersecurity.common.chest;

import hangcow.greatersecurity.common.GreaterSecurity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
import dark.library.access.AccessLevel;
import dark.library.machine.terminal.TileEntityTerminal;

public class TileEntityLockedChest extends TileEntityTerminal
{

	private ItemStack[] chestContents = new ItemStack[36];
	private List<ItemStack> inputItems = new ArrayList<ItemStack>();
	private List<ItemStack> outputItems = new ArrayList<ItemStack>();
	/** Determines if the check for adjacent chests has taken place. */
	public boolean adjacentChestChecked = false;

	/** Contains the chest tile located adjacent to this one (if any) */
	public TileEntityLockedChest adjacentChestZNeg;
	public TileEntityLockedChest adjacentChestXPos;
	public TileEntityLockedChest adjacentChestXNeg;
	public TileEntityLockedChest adjacentChestZPosition;

	/** The current angle of the lid (between 0 and 1) */
	public float lidAngle;
	public float prevLidAngle;

	/** The type of material the chest is made from */
	private HardnessTiers hardnessType = HardnessTiers.WOOD;

	public InvChest chestInv = new InvChest(this);

	public TileEntityLockedChest()
	{
		super(0);
	}

	/** Reads a tile entity from NBT. */
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		this.hardnessType = HardnessTiers.get(nbt.getInteger("chestType"));
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
		// chest inv reading
		NBTTagList var2 = nbt.getTagList("Items");
		this.chestContents = new ItemStack[this.chestInv.getSizeInventory()];

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

	/** Writes a tile entity to NBT. */
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setInteger("chestType", this.hardnessType.ordinal());

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

	/** Causes the TileEntity to reset all it's cached values for it's container block, blockID,
	 * metaData and in the case of chests, the adjcacent chest check */
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

	/** Performs the check for adjacent chests to determine if this chest is double or not. */
	public void checkForAdjacentChests()
	{
		if (!this.adjacentChestChecked)
		{
			this.adjacentChestChecked = true;
			this.adjacentChestZNeg = null;
			this.adjacentChestXPos = null;
			this.adjacentChestXNeg = null;
			this.adjacentChestZPosition = null;

			if (this.worldObj.getBlockId(this.xCoord - 1, this.yCoord, this.zCoord) == GreaterSecurity.blockLockedChest.blockID)
			{
				this.adjacentChestXNeg = (TileEntityLockedChest) this.worldObj.getBlockTileEntity(this.xCoord - 1, this.yCoord, this.zCoord);
			}

			if (this.worldObj.getBlockId(this.xCoord + 1, this.yCoord, this.zCoord) == GreaterSecurity.blockLockedChest.blockID)
			{
				this.adjacentChestXPos = (TileEntityLockedChest) this.worldObj.getBlockTileEntity(this.xCoord + 1, this.yCoord, this.zCoord);
			}

			if (this.worldObj.getBlockId(this.xCoord, this.yCoord, this.zCoord - 1) == GreaterSecurity.blockLockedChest.blockID)
			{
				this.adjacentChestZNeg = (TileEntityLockedChest) this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord - 1);
			}

			if (this.worldObj.getBlockId(this.xCoord, this.yCoord, this.zCoord + 1) == GreaterSecurity.blockLockedChest.blockID)
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

	/** Allows the entity to update its state. Overridden in most subclasses, e.g. the mob spawner
	 * uses this to count ticks and creates a new spawn inside its implementation. */
	public void updateEntity()
	{
		super.updateEntity();
		this.checkForAdjacentChests();
		float f;

		if (!this.worldObj.isRemote && this.playersUsing.size() != 0 && (this.ticks + this.xCoord + this.yCoord + this.zCoord) % 200 == 0)
		{
			this.playersUsing.clear();
			f = 5.0F;
			List list = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.getAABBPool().getAABB((double) ((float) this.xCoord - f), (double) ((float) this.yCoord - f), (double) ((float) this.zCoord - f), (double) ((float) (this.xCoord + 1) + f), (double) ((float) (this.yCoord + 1) + f), (double) ((float) (this.zCoord + 1) + f)));
			Iterator var3 = list.iterator();

			while (var3.hasNext())
			{
				EntityPlayer player = (EntityPlayer) var3.next();

				if (player.openContainer instanceof ContainerChest)
				{
					IInventory playerInv = ((ContainerChest) player.openContainer).getLowerChestInventory();

					if (playerInv == this || playerInv instanceof InventoryLargeChest && ((InventoryLargeChest) playerInv).isPartOfLargeChest(this.chestInv))
					{
						this.playersUsing.add(player);
					}
				}
			}
		}

		this.prevLidAngle = this.lidAngle;
		f = 0.1F;
		double var11;

		if (this.playersUsing.size() > 0 && this.lidAngle == 0.0F && this.adjacentChestZNeg == null && this.adjacentChestXNeg == null)
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

		if (this.playersUsing.size() == 0 && this.lidAngle > 0.0F || this.playersUsing.size() > 0 && this.lidAngle < 1.0F)
		{
			float var9 = this.lidAngle;

			if (this.playersUsing.size() > 0)
			{
				this.lidAngle += f;
			}
			else
			{
				this.lidAngle -= f;
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

	@Override
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
		if (this.worldObj == null)
		{
			return null;
		}
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
	public boolean addUserAccess(String username, AccessLevel level, boolean shouldSave)
	{
		boolean added = super.addUserAccess(username, level, shouldSave);
		try
		{
			if (worldObj != null)
			{
				TileEntityLockedChest chest = this.getAdjacentChest();

				if (added && !worldObj.isRemote && chest != null && chest.getUserAccess(username) != AccessLevel.NONE)
				{
					chest.addUserAccess(username, level, shouldSave);
				}
			}
		}
		catch (Exception e)
		{
			System.out.println("LockedChest>>>AddUser>>>Failed");
			e.printStackTrace();
		}
		return added;
	}

	@Override
	public boolean removeUserAccess(String player)
	{
		boolean removed = super.removeUserAccess(player);
		try
		{
			if (worldObj != null)
			{
				TileEntityLockedChest chest = this.getAdjacentChest();

				if (removed && !worldObj.isRemote && chest != null && chest.getUserAccess(player) != AccessLevel.NONE)
				{
					chest.removeUserAccess(player);
				}
			}
		}
		catch (Exception e)
		{
			System.out.println("LockedChest>>>RemoveUser>>Failed");
			e.printStackTrace();
		}
		return removed;
	}

	public ItemStack[] getContainingItems()
	{
		return this.chestContents;
	}

	public boolean canInsertItem(int i, ItemStack itemstack, int j)
	{
		return this.inputItems.contains(itemstack);
	}

	public boolean canExtractItem(int i, ItemStack itemstack, int j)
	{
		return this.outputItems.contains(itemstack);
	}

	public HardnessTiers getType()
	{
		return this.hardnessType;
	}

	@Override
	public boolean canConnect(ForgeDirection direction)
	{
		return false;
	}

	@Override
	public float getRequest(ForgeDirection side)
	{
		return 0;
	}
}
