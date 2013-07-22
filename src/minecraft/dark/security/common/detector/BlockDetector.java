package dark.security.common.detector;


import java.util.List;

import dark.security.GreaterSecurity;
import dark.security.common.BlockGS;
import dark.security.common.CommonProxy;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockDetector extends BlockGS
{

	public BlockDetector(int par1)
	{
		super("ItemLock", par1, Material.iron);
		if (!GreaterSecurity.breakDetectors)
		{

			setBlockUnbreakable();

		}
		else
		{

			setHardness(10F);
			setResistance(100F); // TODO Test and improve, this is just an experimental number.

		}
	}

	@Override
	public boolean isOpaqueCube()
	{

		return false;

	}

	@Override
	public boolean renderAsNormalBlock()
	{

		return false;

	}

	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return new TileEntityDetector();
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack)
	{

		int angle = MathHelper.floor_double((entityLiving.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		int change = 0;

		if (angle == 0 || angle == 2)
		{

			change = 0;

		}
		else
		{

			change = 1;

		}

		world.setBlockMetadataWithNotify(x, y, z, change, 3);

	}

	@Override
	public void addCollisionBoxesToList(World par1World, int par2, int par3, int par4, AxisAlignedBB par5AxisAlignedBB, List par6List, Entity par7Entity)
	{
		// TODO Add sufficient collision boxes.
	}

	@Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity)
	{

		TileEntityDetector tile = (TileEntityDetector) world.getBlockTileEntity(x, y, z);

		int requiredItem = tile.getRequiredItem(tile);

		if (entity instanceof EntityPlayer)
		{

			if (((EntityPlayer) entity).inventory.hasItem(requiredItem) || ((EntityPlayer) entity).capabilities.isCreativeMode)
			{

				passEntityThroughDetector(entity, world, x, y, z);

			}
			else
			{

				CommonProxy.entityPush(entity, 1, false);

			}
		}

	}

	public int getEntityFacing(Entity entity)
	{

		int facing = MathHelper.floor_double((entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		return facing;

	}

	public String getEntityFacingString(int facing)
	{

		switch (facing)
		{

			case 0:
				return "south";

			case 1:
				return "west";

			case 2:
				return "north";

			default:
				return "east";

		}

	}

	public void passEntityThroughDetector(Entity entity, World world, int x, int y, int z)
	{

		int facing = getEntityFacing(entity);

		switch (getEntityFacing(entity))
		{

			case 0:
				boolean aIsAir = world.isAirBlock(x, y, z + 1);
				boolean aIsAir2 = world.isAirBlock(x, y - 1, z + 1);

				if (aIsAir && aIsAir2)
				{

					CommonProxy.entityPush(entity, 1, true);

				}

				break;

			case 1:
				boolean bIsAir = world.isAirBlock(x - 1, y, z);
				boolean bIsAir2 = world.isAirBlock(x - 1, y - 1, z);

				if (bIsAir && bIsAir2)
				{

					CommonProxy.entityPush(entity, 1, true);

				}

				break;

			case 2:
				boolean cIsAir = world.isAirBlock(x, y, z - 1);
				boolean cIsAir2 = world.isAirBlock(x, y - 1, z - 1);

				if (cIsAir && cIsAir2)
				{

					CommonProxy.entityPush(entity, 1, true);

				}

				break;

			case 3:
				boolean dIsAir = world.isAirBlock(x + 1, y, z);
				boolean dIsAir2 = world.isAirBlock(x + 1, y - 1, z);

				if (dIsAir && dIsAir2)
				{

					CommonProxy.entityPush(entity, 1, true);

				}

		}

	}

	// TODO Set block bounds, register icons and possible GUI display

}