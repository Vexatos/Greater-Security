package dark.library.locking.prefab;

import hangcow.greatersecurity.common.GreaterSecurity;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.network.IPacketReceiver;
import universalelectricity.prefab.network.PacketManager;
import universalelectricity.prefab.tile.TileEntityAdvanced;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.network.PacketDispatcher;
import dark.library.locking.AccessLevel;
import dark.library.locking.ISpecialAccess;
import dark.library.locking.UserAccess;

public abstract class TileEntityLockable extends TileEntityAdvanced implements ISpecialAccess, IPacketReceiver
{
	public enum PacketType
	{
		DESCR_DATA, LIST_EDIT, SETTING_EDIT;
	}

	/**
	 * A list of user access data.
	 */
	private final List<UserAccess> users = new ArrayList<UserAccess>();

	/**
	 * The amount of players using the console.
	 */
	public int playersUsing = 0;

	@Override
	public void updateEntity()
	{
		super.updateEntity();

		if (!this.worldObj.isRemote)
		{
			if ((this.playersUsing > 0 && this.ticks % 5 == 0) || (this.playersUsing < 0 && this.ticks % 100 == 0))
			{
				PacketManager.sendPacketToClients(this.getDescriptionPacket(), this.worldObj, new Vector3(this), 12);
			}
		}
	}

	public abstract String getChannel();

	/**
	 * Packet Methods
	 */
	/**
	 * Sends all NBT data. Server -> Client
	 */
	@Override
	public Packet getDescriptionPacket()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		this.writeToNBT(nbt);
		return PacketManager.getPacket(GreaterSecurity.CHANNEL, this, PacketType.DESCR_DATA.ordinal(), nbt);
	}

	public void sendEditToServer(UserAccess player, boolean remove)
	{
		if (this.worldObj.isRemote && player != null)
		{
			Packet packet = PacketManager.getPacket(this.getChannel(), this, PacketType.LIST_EDIT.ordinal(), player.username, player.level.ordinal(), player.shouldSave, remove);
			PacketDispatcher.sendPacketToServer(packet);
		}
	}

	@Override
	public void handlePacketData(INetworkManager network, int packetID, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream)
	{
		try
		{
			PacketType packetType = PacketType.values()[dataStream.readInt()];

			switch (packetType)
			{
				case DESCR_DATA:
				{
					if (this.worldObj.isRemote)
					{
						short size = dataStream.readShort();

						if (size > 0)
						{
							byte[] byteCode = new byte[size];
							dataStream.readFully(byteCode);
							this.readFromNBT(CompressedStreamTools.decompress(byteCode));
						}
					}

					break;
				}
				case LIST_EDIT:
				{
					if (!this.worldObj.isRemote)
					{
						String name = dataStream.readUTF();
						AccessLevel level = AccessLevel.get(dataStream.readInt());
						Boolean shouldSave = dataStream.readBoolean();
						Boolean remove = dataStream.readBoolean();
						if (remove)
						{
							this.removeUserAccess(name);
						}
						else
						{
							this.addUserAccess(name, level, shouldSave);
						}

					}
					break;
				}
				case SETTING_EDIT:
				{
					break;
				}
			}

		}
		catch (Exception e)
		{
			FMLLog.severe("GS: Failed to handle packet for locked door.");
			e.printStackTrace();
		}
	}

	@Override
	public AccessLevel getUserAccess(String username)
	{
		for (int i = 0; i < this.users.size(); i++)
		{
			if (this.users.get(i).username.equalsIgnoreCase(username))
			{
				return this.users.get(i).level;
			}
		}
		return AccessLevel.NONE;
	}

	@Override
	public List<UserAccess> getUsers()
	{
		return this.users;
	}

	@Override
	public List<UserAccess> getUsersWithAcess(AccessLevel level)
	{
		List<UserAccess> players = new ArrayList<UserAccess>();

		for (int i = 0; i < this.users.size(); i++)
		{
			UserAccess ref = this.users.get(i);

			if (ref.level == level)
			{
				players.add(ref);
			}
		}
		return players;

	}

	@Override
	public boolean addUserAccess(String player, AccessLevel lvl, boolean save)
	{
		UserAccess access = new UserAccess(player, lvl, save);
		if (worldObj.isRemote)
		{
			this.sendEditToServer(access, false);
		}
		this.removeUserAccess(player);
		return this.users.add(access);
	}

	@Override
	public boolean removeUserAccess(String player)
	{

		if (worldObj.isRemote)
		{
			UserAccess access = new UserAccess(player, AccessLevel.BASIC, false);
			this.sendEditToServer(access, true);
		}
		List<UserAccess> list = UserAccess.removeUserAccess(player, this.users);
		if (list.size() < this.users.size())
		{
			this.users.clear();
			this.users.addAll(list);
			return true;
		}
		return false;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);

		// Read user list
		this.users.clear();
		this.users.addAll(UserAccess.readListFromNBT(nbt, "Users"));
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);

		// Write user list
		UserAccess.writeListToNBT(nbt, this.users);
	}
}
