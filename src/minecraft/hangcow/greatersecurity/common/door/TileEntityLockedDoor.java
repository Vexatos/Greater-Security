package hangcow.greatersecurity.common.door;

import hangcow.greatersecurity.common.GreaterSecurity;
import net.minecraft.nbt.NBTTagCompound;
import dark.library.locking.AccessLevel;
import dark.library.locking.prefab.TileEntityLockable;

public class TileEntityLockedDoor extends TileEntityLockable
{
	int timeOpen = 0;
	public boolean isOpen = false;
	private int closeTime = 7; // TODO add gui setting for this

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
	}

	

	/**
	 * Allows the entity to update its state. Overridden in most subclasses, e.g. the mob spawner
	 * uses this to count ticks and creates a new spawn inside its implementation.
	 */
	public void updateEntity()
	{
		super.updateEntity();
		if (isOpen && this.ticks % 20 == 0 && timeOpen++ >= closeTime)
		{
			timeOpen = 0;
			this.isOpen = false;
			BlockLockedDoor.activateDoor(this);
		}
	}

	@Override
	public String getChannel()
	{
		return GreaterSecurity.CHANNEL;
	}
}
