package hangcow.greatersecurity.common.laser;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import hangcow.greatersecurity.client.render.BlockRenderHelper;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.prefab.block.BlockAdvanced;
import dark.library.locking.AccessLevel;
import dark.library.locking.UserAccess;

public class BlockLaserFence extends BlockAdvanced
{

	public BlockLaserFence(int id)
	{
		super(id, Material.iron);
		this.setHardness(20f);
		this.setResistance(100f);
		this.setCreativeTab(CreativeTabs.tabRedstone);

	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
	{
		this.setBlockBoundsBasedOnState(par1World, par2, par3, par4);
		return super.getCollisionBoundingBoxFromPool(par1World, par2, par3, par4);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
	{
		this.setBlockBoundsBasedOnState(par1World, par2, par3, par4);
		return super.getSelectedBoundingBoxFromPool(par1World, par2, par3, par4);
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
	{
		int meta = world.getBlockMetadata(x, y, z);
		boolean rotated = meta > 5;
		ForgeDirection direction = ForgeDirection.getOrientation(meta % 6);
		if (direction == ForgeDirection.UP)
		{
			this.setBlockBounds(0.25f, 0f, 0f, 0.75f, 0.25f, 1f);
			if (rotated)
			{
				this.setBlockBounds(0f, 0f, 0.25f, 1f, 0.25f, 0.75f);
			}
			return;
		}
		else if (direction == ForgeDirection.DOWN)
		{
			this.setBlockBounds(0.25f, 0.75f, 0f, 0.75f, 1f, 1f);
			if (rotated)
			{
				this.setBlockBounds(0f, 0.75f, 0.25f, 1f, 1f, 0.75f);
			}
			return;
		}
		else if (direction == ForgeDirection.NORTH)
		{
			this.setBlockBounds(0.25f, 0, 0.75f, 0.75f, 1f, 1f);
			if (rotated)
			{
				this.setBlockBounds(0f, 0.25f, 0.75f, 1f, 0.75f, 1f);
			}
			return;
		}
		else if (direction == ForgeDirection.SOUTH)
		{
			this.setBlockBounds(0.25f, 0, 0f, 0.75f, 1f, 0.25f);
			if (rotated)
			{
				this.setBlockBounds(0f, 0.25f, 0f, 1f, 0.75f, 0.25f);
			}
			return;
		}
		else if (direction == ForgeDirection.EAST)
		{
			this.setBlockBounds(0f, 0, 0.25f, 0.25f, 1f, 0.75f);
			if (rotated)
			{
				this.setBlockBounds(0f, 0.25f, 0f, 0.25f, 0.75f, 1f);
			}
			return;
		}
		else if (direction == ForgeDirection.WEST)
		{
			this.setBlockBounds(0.75f, 0, 0.25f, 1f, 1f, 0.75f);
			if (rotated)
			{
				this.setBlockBounds(0.75f, 0.25f, 0f, 1f, 0.75f, 1f);
			}

			return;
		}
		else
		{
			this.setBlockBounds(0, 0, 0, 1, 0.3f, 1);
			return;
		}
	}

	@Override
	public boolean onUseWrench(World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float hitX, float hitY, float hitZ)
	{
		if (world.getBlockTileEntity(x, y, z) instanceof TileEntityLaserFence)
		{
			TileEntityLaserFence fence = (TileEntityLaserFence) world.getBlockTileEntity(x, y, z);
			if (fence.canAccess(entityPlayer))
			{
				int meta = world.getBlockMetadata(x, y, z);
				if (meta > 5)
				{
					meta = meta % 6;
				}
				else
				{
					meta += 6;
				}
				world.setBlockMetadataWithNotify(x, y, z, meta, 3);
			}

		}
		return false;
	}

	@Override
	public boolean onSneakUseWrench(World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float hitX, float hitY, float hitZ)
	{
		if (world.getBlockTileEntity(x, y, z) instanceof TileEntityLaserFence)
		{
			TileEntityLaserFence fence = (TileEntityLaserFence) world.getBlockTileEntity(x, y, z);
			if (fence.canAccess(entityPlayer))
			{
				int meta = world.getBlockMetadata(x, y, z);
				if (meta > 5)
				{
					meta = side + 6;
				}
				else
				{
					meta = side;
				}
				world.setBlockMetadataWithNotify(x, y, z, meta, 3);
			}
		}
		return this.onUseWrench(world, x, y, z, entityPlayer, side, hitX, hitY, hitZ);
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving entityLiving, ItemStack stack)
	{
		if (!world.isRemote)
		{
			if (world.getBlockTileEntity(x, y, z) instanceof TileEntityLaserFence && entityLiving instanceof EntityPlayer)
			{
				TileEntityLaserFence fence = (TileEntityLaserFence) world.getBlockTileEntity(x, y, z);
				fence.addUserAccess(new UserAccess(((EntityPlayer) entityLiving).username, AccessLevel.OWNER, true), !world.isRemote);
			}
		}
	}

	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return new TileEntityLaserFence();
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
	public int getRenderType()
	{
		return BlockRenderHelper.renderID;
	}
}
