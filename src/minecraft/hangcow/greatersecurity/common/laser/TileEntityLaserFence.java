package hangcow.greatersecurity.common.laser;

import universalelectricity.core.vector.Vector3;
import hangcow.greatersecurity.common.GreaterSecurity;
import net.minecraft.block.Block;
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
		if (!worldObj.isRemote) // TODO add power check
		{
			Vector3 vec = new Vector3(this);
			if (worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord))
			{
				/* GET LASER BLOCK META PLACEMENT */
				int laserMeta = 0;
				int laserRotation = 1;
				if (vec.getBlockMetadata(worldObj) > 5)
				{
					laserRotation = 2;
				}
				switch (this.getFacingDirection().ordinal())
				{
					case 0:
					case 1:
						laserMeta = 0 * laserRotation;
						break;
					case 2:
					case 3:
						laserMeta = 1 * laserRotation;
						break;
					case 4:
					case 5:
						laserMeta = 2 * laserRotation;
						break;
				}

				/* UPDATE OR CHECK LASER PATH */
				for (int i = 1; i < TileEntityLaserFence.MAX_LASER_RANGE; i++)
				{
					Vector3 loc = vec.modifyPositionFromSide(this.getFacingDirection(), i);
					int blockID = loc.getBlockID(this.worldObj);
					Block block = Block.blocksList[blockID];
					boolean place = false;
					if (blockID == 0)
					{
						place = true;
					}
					else if (blockID == GreaterSecurity.blockLaser.blockID && loc.getBlockMetadata(worldObj) != laserMeta)
					{
						place = true;
					}
					else if (block != null && block.isBlockReplaceable(worldObj, loc.intX(), loc.intY(), loc.intZ()))
					{
						place = true;
					}

					if (place)
					{
						loc.setBlock(worldObj, GreaterSecurity.blockLaser.blockID, laserMeta);
					}
				}
			}
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
