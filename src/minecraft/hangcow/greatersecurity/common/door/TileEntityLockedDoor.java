package hangcow.greatersecurity.common.door;

import hangcow.greatersecurity.common.network.IPacketReceiver;
import hangcow.greatersecurity.common.network.LockPacketHandler;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;

import com.google.common.io.ByteArrayDataInput;

public class TileEntityLockedDoor extends TileEntity implements IPacketReceiver
{
	public List<String> users = new ArrayList<String>();
	public String owner = "world";
	public int tickSnyc = 0;
	public boolean wasOpen = false;
	int timeOpen = 0;
	private int closeTime = 7; // TODO add gui setting for this
	

	public boolean canOpen(EntityPlayer player)
	{
		return canAccess(this, player);
	}

	@Override
	public void handlePacketData(INetworkManager network, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream)
	{
		try
		{
			int l = dataStream.readInt();
			int p = dataStream.readInt();

			if (worldObj.isRemote && p == 0)
			{

				this.owner = dataStream.readUTF().toString();
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
				}
			}
			else if (p == 1)
			{
				String name = dataStream.readUTF().toString();
				if (users.contains(name))
				{
					this.removeUser(name);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		this.wasOpen = nbt.getBoolean("opened");
		this.timeOpen = nbt.getInteger("timeOpen");
		this.owner = nbt.getString("Owner");
		// reads user array from map
		int userSize = nbt.getInteger("users");
		for (int i = 0; i < userSize; i++)
		{
			String read = nbt.getString("user" + i);
			this.users.add(i, read);
		}
	}

	/**
	 * Writes a tile entity to NBT.
	 */
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setString("Owner", this.owner);
		nbt.setBoolean("opened", this.wasOpen);
		nbt.setInteger("timeOpen", this.timeOpen);
		// writes user array to map
		nbt.setInteger("users", this.users.size());
		for (int i = 0; i < this.users.size(); i++)
		{
			nbt.setString("user" + i, this.users.get(i));
		}
	}

	public boolean canAccess(TileEntityLockedDoor tileEntityLDoor, EntityPlayer player)
	{
		if (tileEntityLDoor instanceof TileEntityLockedDoor)
		{
			EntityPlayer owner = worldObj.getPlayerEntityByName(tileEntityLDoor.owner);
			List<String> pString = tileEntityLDoor.users;
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

	public void updateUsers(List<String> users)
	{
		this.users = users;
	}

	public void removeUser(String user)
	{
		this.users.remove(user);
	}

	public void addUser(String user)
	{
		this.users.add(user);
	}

	/**
	 * Allows the entity to update its state. Overridden in most subclasses, e.g. the mob spawner
	 * uses this to count ticks and creates a new spawn inside its implementation.
	 */
	public void updateEntity()
	{
		super.updateEntity();
		if (++this.tickSnyc % 20 * 4 == 0)
		{
			++timeOpen;
			if (timeOpen >= this.closeTime)
			{
				int bMeta = worldObj.getBlockMetadata(xCoord, yCoord - 1, zCoord);
				if (bMeta > 3 && bMeta < 8)
				{
					BlockLockedDoor.activateDoor(worldObj, xCoord, yCoord, zCoord);
					worldObj.markBlockForUpdate(xCoord, yCoord - 1, zCoord);
					worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				}
				this.wasOpen = false;
				timeOpen = 0;
			}
			if (!worldObj.isRemote)
			{

				LockPacketHandler.sendDoorPacketPacket(this, 0, this.owner, users);

			}

		}
	}

	public void setOwner(EntityPlayer player)
	{
		setOwner(player.username);

	}

	public void setOwner(String player)
	{
		this.owner = player;
	}
}
