package hangcow.greatersecurity.common.fence.electro;

import hangcow.greatersecurity.common.GreaterSecurity;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.block.BlockAdvanced;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockElectroFence extends BlockAdvanced
{

	public BlockElectroFence(int Id)
	{
		super(Id, Material.iron);
		this.setCreativeTab(GreaterSecurity.tabGreaterSecurity);
		this.setUnlocalizedName("EltroFence");
	}

	@Override
	public void addCollisionBoxesToList(World par1World, int par2, int par3, int par4, AxisAlignedBB par5AxisAlignedBB, List par6List, Entity par7Entity)
	{
		boolean flag = this.canConnectFenceTo(par1World, par2, par3, par4 - 1);
		boolean flag1 = this.canConnectFenceTo(par1World, par2, par3, par4 + 1);
		boolean flag2 = this.canConnectFenceTo(par1World, par2 - 1, par3, par4);
		boolean flag3 = this.canConnectFenceTo(par1World, par2 + 1, par3, par4);
		float f = 0.375F;
		float f1 = 0.625F;
		float f2 = 0.375F;
		float f3 = 0.625F;

		if (flag)
		{
			f2 = 0.0F;
		}

		if (flag1)
		{
			f3 = 1.0F;
		}

		if (flag || flag1)
		{
			this.setBlockBounds(f, 0.0F, f2, f1, 1.5F, f3);
			super.addCollisionBoxesToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
		}

		f2 = 0.375F;
		f3 = 0.625F;

		if (flag2)
		{
			f = 0.0F;
		}

		if (flag3)
		{
			f1 = 1.0F;
		}

		if (flag2 || flag3 || !flag && !flag1)
		{
			this.setBlockBounds(f, 0.0F, f2, f1, 1.5F, f3);
			super.addCollisionBoxesToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
		}

		if (flag)
		{
			f2 = 0.0F;
		}

		if (flag1)
		{
			f3 = 1.0F;
		}

		this.setBlockBounds(f, 0.0F, f2, f1, 1.0F, f3);
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
	{
		boolean flag = this.canConnectFenceTo(par1IBlockAccess, par2, par3, par4 - 1);
		boolean flag1 = this.canConnectFenceTo(par1IBlockAccess, par2, par3, par4 + 1);
		boolean flag2 = this.canConnectFenceTo(par1IBlockAccess, par2 - 1, par3, par4);
		boolean flag3 = this.canConnectFenceTo(par1IBlockAccess, par2 + 1, par3, par4);
		float f = 0.375F;
		float f1 = 0.625F;
		float f2 = 0.375F;
		float f3 = 0.625F;

		if (flag)
		{
			f2 = 0.0F;
		}

		if (flag1)
		{
			f3 = 1.0F;
		}

		if (flag2)
		{
			f = 0.0F;
		}

		if (flag3)
		{
			f1 = 1.0F;
		}

		this.setBlockBounds(f, 0.0F, f2, f1, 1.0F, f3);
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
	public boolean getBlocksMovement(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
	{
		return false;
	}

	@Override
	public int getRenderType()
	{
		return -1;
	}

	public boolean canConnectFenceTo(IBlockAccess world, int x, int y, int z)
	{

		return this.canConnectFenceTo(world, new Vector3(x, y, z));
	}

	/**
	 * Returns true if the specified block can be connected by a fence
	 */
	public boolean canConnectFenceTo(IBlockAccess par1IBlockAccess, Vector3 vec)
	{
		int blockID = par1IBlockAccess.getBlockId(vec.intX(), vec.intY(), vec.intZ());

		if (blockID != this.blockID && blockID != Block.fenceGate.blockID && blockID != this.blockID)
		{
			Block block = Block.blocksList[blockID];
			return block != null && block.blockMaterial.isOpaque() && block.renderAsNormalBlock() ? block.blockMaterial != Material.pumpkin : false;
		}
		else
		{
			return true;
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
	{
		return true;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IconRegister par1IconRegister)
	{
		// this.blockIcon = par1IconRegister.registerIcon(this.textureName);
		this.blockIcon = Block.blockIron.getBlockTextureFromSide(0);
	}

	@Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity)
	{
		TileEntity ent = world.getBlockTileEntity(x, y, z);
		if (ent instanceof TileEntityElectroFence)
		{
			((TileEntityElectroFence) ent).shockEntity(entity);
		}
	}

	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return new TileEntityElectroFence();
	}

	@Override
	public boolean isLadder(World world, int x, int y, int z)
	{
		return true;
	}
}
