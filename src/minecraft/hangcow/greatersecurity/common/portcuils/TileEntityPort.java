package hangcow.greatersecurity.common.portcuils;

import universalelectricity.core.vector.Vector3;
import hangcow.greatersecurity.api.IPorticuils;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class TileEntityPort extends TileEntity implements IPorticuils
{
	private boolean isExtended = false;
	private ForgeDirection facing = ForgeDirection.UP;

	public int getMaxSize()
	{
		return 6;
	}

	@Override
	public boolean extend(boolean doExtend)
	{
		if (isExtended())
		{
			return false;
		}
		if (doExtend)
		{
			this.isExtended = false;
			// TODO cause it to extend
		}
		return true;
	}

	@Override
	public boolean retract(boolean doRetract)
	{
		if (!isExtended())
		{
			return false;
		}
		if (doRetract)
		{
			this.isExtended = true;
			// TODO cause it to retract
		}
		return true;
	}

	@Override
	public boolean isExtended()
	{
		return isExtended;
	}

	/**
	 * checks to see if a length of block matches a single block:meta
	 * 
	 * @param world - world
	 * @param loc - vector3 location to start at
	 * @param facing - direction to search in
	 * @param length - length to search
	 * @param blockID - block id
	 * @param blockMeta - block meta
	 * @return - true if the length is all the same block:meta as given
	 */
	public boolean isLengthMatch(World world, Vector3 loc, ForgeDirection facing, int length, int blockID, int blockMeta)
	{
		for (int i = 0; i < length; i++)
		{
			int x = loc.intX() + (facing.offsetX * i);
			int y = loc.intY() + (facing.offsetY * i);
			int z = loc.intZ() + (facing.offsetZ * i);

			int id = world.getBlockId(x, y, z);
			int meta = world.getBlockMetadata(x, y, z);

			if (id == blockID && (blockMeta == meta || blockMeta == -1))
			{

			}
			else
			{
				return false;
			}

		}
		return true;
	}
}
