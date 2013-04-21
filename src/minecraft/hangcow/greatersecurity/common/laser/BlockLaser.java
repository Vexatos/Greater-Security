package hangcow.greatersecurity.common.laser;

import java.util.Random;

import universalelectricity.core.vector.Vector3;
import universalelectricity.core.vector.VectorHelper;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class BlockLaser extends Block
{

	public BlockLaser(int par1)
	{
		super(par1, Material.air);
		this.setTickRandomly(true);
	}

	@Override
	public int idDropped(int par1, Random par2Random, int par3)
	{
		return 0;
	}

	@Override
	public int quantityDropped(Random par1Random)
	{
		return 0;
	}

	@Override
	public int tickRate(World par1World)
	{
		return 1;
	}

	@Override
	public void randomDisplayTick(World world, int x, int y, int z, Random random)
	{
		this.updateTick(world, x, y, z, random);
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random random)
	{
		if (!world.isRemote)
		{
			Vector3 vec = new Vector3(x, y, z);
			int meta = vec.getBlockMetadata(world);
			ForgeDirection direction = ForgeDirection.UNKNOWN;
			switch (meta)
			{
				case 0:
				case 1:
					direction = ForgeDirection.UP;
					break;
				case 2:
				case 3:
					direction = ForgeDirection.NORTH;
					break;
				case 4:
				case 5:
					direction = ForgeDirection.EAST;
					break;
			}
			TileEntityLaserFence fence = null;
			if (VectorHelper.getTileEntityFromSide(world, vec, direction) instanceof TileEntityLaserFence)
			{
				fence = (TileEntityLaserFence) VectorHelper.getTileEntityFromSide(world, vec, direction);
			}
			else if (VectorHelper.getTileEntityFromSide(world, vec, direction.getOpposite()) instanceof TileEntityLaserFence)
			{
				fence = (TileEntityLaserFence) VectorHelper.getTileEntityFromSide(world, vec, direction.getOpposite());
			}
			else
			{
				for (int i = 1; i < TileEntityLaserFence.MAX_LASER_RANGE; i++)
				{
					Vector3 loc = vec.modifyPositionFromSide(direction, i);
					Vector3 loc2 = vec.modifyPositionFromSide(direction.getOpposite(), i);
					if (loc.getTileEntity(world) instanceof TileEntityLaserFence)
					{
						fence = (TileEntityLaserFence) loc.getTileEntity(world);
						break;
					}
					else if (loc2.getTileEntity(world) instanceof TileEntityLaserFence)
					{
						fence = (TileEntityLaserFence) loc2.getTileEntity(world);
						break;
					}
				}
			}
			if (fence == null)
			{
				world.setBlock(x, y, z, 0);
			}
		}

	}
}
