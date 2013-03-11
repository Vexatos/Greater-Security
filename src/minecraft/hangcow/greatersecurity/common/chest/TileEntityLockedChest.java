package hangcow.greatersecurity.common.chest;

import hangcow.greatersecurity.common.GreaterSecurity;
import hangcow.greatersecurity.common.network.IPacketReceiver;
import hangcow.greatersecurity.common.network.LockPacketHandler;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.google.common.io.ByteArrayDataInput;

public class TileEntityLockedChest extends TileEntity implements IInventory, IPacketReceiver
{
	public ItemStack[] chestContents = new ItemStack[36];

	public String BlockOwner = "World";

	public List<String> users = new ArrayList<String>();

	public boolean once = true;
	public boolean adjacentChestChecked = false;

	public TileEntityLockedChest[] chests = { null, null, null, null };

	private int tickCount = 10;
	public int numUsingPlayers;
	private int ticksSinceSync;

	public float prevLidAngle;
	public float lidAngle;

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
	public ItemStack getStackInSlot(int par1)
	{
		return this.chestContents[par1];
	}

	// use to set block owner from out side of class
	public void setOwner(EntityPlayer player)
	{
		this.BlockOwner = player.username;
	}

	public void setOwnerString(String username)
	{
		this.BlockOwner = username;
	}

	// cow do not touch, this reads the object array back into correct vars
	@Override
	public void handlePacketData(INetworkManager network, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream)
	{
		try
		{
			int l = dataStream.readInt();
			int p = dataStream.readInt();
			int pp = dataStream.readInt();

			if (worldObj.isRemote && p == 0)
			{
				this.numUsingPlayers = pp;
				this.BlockOwner = dataStream.readUTF().toString();
				for (int i = 0; i < l; i++)
				{
					String read = dataStream.readUTF().toString();
					if (!users.contains(read))
					{
						this.users.add(i, read);
					}
				}
			}
			else if (p == 2)
			{
				String name = dataStream.readUTF().toString();
				if (!users.contains(name))
				{
					this.addUser(name);
					syncChestData();
				}
			}
			else if (p == 1)
			{
				String name = dataStream.readUTF().toString();
				if (users.contains(name))
				{
					this.removeUser(name);
					syncChestData(2);
				}
			}
			else if (p == 5)
			{
				GreaterSecurity.blockLockedChest.breakBlock(worldObj, xCoord, yCoord, zCoord, 0, 0);
				int meta = GreaterSecurity.blockLockedChest.damageDropped(worldObj.getBlockMetadata(xCoord, yCoord, zCoord));
				this.dropBlockAsItem_do(worldObj, xCoord, yCoord, zCoord, new ItemStack(GreaterSecurity.blockLockedChest, 1, meta));
				worldObj.setBlock(xCoord, yCoord, zCoord, 0);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

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
		return "container.LChest";
	}

	/**
	 * Reads a tile entity from NBT.
	 */
	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readFromNBT(par1NBTTagCompound);

		this.BlockOwner = par1NBTTagCompound.getString("Owner");
		this.once = par1NBTTagCompound.getBoolean("Once");
		// reads user array from map
		int userSize = par1NBTTagCompound.getInteger("users");
		for (int i = 0; i < userSize; i++)
		{
			String read = par1NBTTagCompound.getString("user" + i);
			this.users.add(i, read);
		}
		// chest inv reading
		NBTTagList var2 = par1NBTTagCompound.getTagList("Items");
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
	public void writeToNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.writeToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setString("Owner", this.BlockOwner);
		par1NBTTagCompound.setBoolean("Once", this.once);
		// writes user array to map
		par1NBTTagCompound.setInteger("users", this.users.size());
		for (int i = 0; i < this.users.size(); i++)
		{
			par1NBTTagCompound.setString("user" + i, this.users.get(i));
		}
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
			TileEntity entity = worldObj.getBlockTileEntity(this.xCoord + deltaX, this.yCoord, this.zCoord + deltaZ);
			if (entity instanceof TileEntityLockedChest)
			{
				if (((TileEntityLockedChest) entity).getType() == this.getType())
				{
					return (TileEntityLockedChest) entity;
				}
			}
		}
		return null;

	}

	public boolean canAccess(TileEntityLockedChest chest, EntityPlayer player)
	{
		if (this.BlockOwner.equalsIgnoreCase("World"))
		{
			return true;
		}
		if (chest instanceof TileEntityLockedChest)
		{
			EntityPlayer owner = worldObj.getPlayerEntityByName(chest.BlockOwner);
			List<String> pString = chest.users;
			List<EntityPlayer> pEntity = new ArrayList<EntityPlayer>();
			for (int i = 0; i < pString.size(); i++)
			{
				EntityPlayer pp = worldObj.getPlayerEntityByName(pString.get(i));
				if (pp != null && pp instanceof EntityPlayer)
				{
					pEntity.add(pp);
				}
			}
			if (player == owner || pEntity.contains(player))
			{
				return true;
			}
		}
		return false;
	}

	public void syncChestData()
	{
		syncChestData(1);
	}

	public void syncChestData(int method)
	{
		TileEntityLockedChest aChest = this.getAdjacentChest();

		if (aChest instanceof TileEntityLockedChest)
		{
			List<String> tList = this.users;
			List<String> aList = aChest.users;

			if (method == 1) // Normal sync on add world or adding a user
			{
				// merge user lists

				for (int i = 0; i < tList.size(); i++)
				{
					if (tList.get(i) != null && !aList.contains(tList.get(i)))
					{
						aList.add(tList.get(i));
					}
				}
				for (int i = 0; i < aList.size(); i++)
				{
					if (aList.get(i) != null && !tList.contains(aList.get(i)))
					{
						tList.add(aList.get(i));
					}
				}
				this.updateUsers(aList);
				aChest.updateUsers(tList);

			}
			else if (method == 2)
			{
				aChest.updateUsers(tList);
			}
		}
	}

	public void updateUsers(List<String> users)
	{
		this.users = users;
	}

	public void removeUser(String user)
	{
		this.users.remove(user);
		TileEntityLockedChest lc = this.getAdjacentChest();
		if (lc instanceof TileEntityLockedChest)
		{
			if (lc.users.contains(user))
			{
				lc.removeUser(user);
			}
		}
	}

	public void addUser(String user)
	{
		this.users.add(user);
		TileEntityLockedChest lc = this.getAdjacentChest();
		if (lc instanceof TileEntityLockedChest)
		{
			if (!lc.users.contains(user))
			{
				lc.addUser(user);
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
		// TODO add lid opening animation
		if (++this.ticksSinceSync % 20 * 4 == 0)
		{
			this.checkForAdjacentChests();
			if (!worldObj.isRemote)
			{
				LockPacketHandler.sendChestPacketPacket(this, 0, this.BlockOwner, users);
			}

		}
	}

	public void openChest()
	{
		++this.numUsingPlayers;
		if (!worldObj.isRemote)
		{
			LockPacketHandler.sendChestPacketPacket(this, 0, this.BlockOwner, users);
		}
	}

	public void closeChest()
	{
		--this.numUsingPlayers;
		if (!worldObj.isRemote)
		{
			LockPacketHandler.sendChestPacketPacket(this, 0, this.BlockOwner, users);
		}
	}

	/**
	 * invalidates a tile entity
	 */
	public void invalidate()
	{
		this.updateContainingBlockInfo();
		this.checkForAdjacentChests();
		super.invalidate();
	}

	public String getOwner()
	{
		if (BlockOwner == null)
		{
			return "null";
		}
		return this.BlockOwner;
	}

	public List<String> getUsers()
	{
		// TODO Auto-generated method stub
		return this.users;
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
}
