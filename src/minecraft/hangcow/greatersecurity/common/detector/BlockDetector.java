package hangcow.greatersecurity.common.detector;

import hangcow.greatersecurity.common.CommonProxy;
import hangcow.greatersecurity.common.GreaterSecurity;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import universalelectricity.prefab.block.BlockAdvanced;

public class BlockDetector extends BlockAdvanced
{

	public BlockDetector(int par1)
	{
		super(par1, Material.iron);
		setUnlocalizedName("ItemDetector");
		setCreativeTab(GreaterSecurity.tabGreaterSecurity);
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
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving entityLiving, ItemStack itemStack)
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

		int requiredItem = tile.getRequiredItem();

		if (entity instanceof EntityPlayer)
		{

			if (((EntityPlayer) entity).inventory.hasItem(requiredItem) || ((EntityPlayer) entity).capabilities.isCreativeMode)
			{

				CommonProxy.entityPush(entity, 1, true);

			}
			else
			{

				CommonProxy.entityPush(entity, 1, false);
				// TODO Alarm sounding code.

			}

		}

	}

	// TODO Set block bounds, register icons and possible GUI display

}
