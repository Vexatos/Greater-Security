package hangcow.greatersecurity.common.laser;

import hangcow.greatersecurity.common.GreaterSecurity;
import net.minecraftforge.common.ForgeDirection;
import dark.library.locking.ISpecialAccess;
import dark.library.locking.prefab.TileEntityElectricLockable;

public class TileEntityLaserFence extends TileEntityElectricLockable implements ISpecialAccess
{
	public static final int MAX_LASER_RANGE = 10;

	@Override
	public void updateEntity()
	{
		super.updateEntity();
		if (!worldObj.isRemote)
		{
			
		}
	}

	public ForgeDirection getFacingDirection()
	{
		int meta = 0;
		if (worldObj != null)
		{
			meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord) % 6;
		}
		return ForgeDirection.getOrientation(meta);
	}

	@Override
	public boolean canConnect(ForgeDirection direction)
	{
		return direction == getFacingDirection().getOpposite();
	}

	@Override
	public String getChannel()
	{
		return GreaterSecurity.CHANNEL;
	}

}
