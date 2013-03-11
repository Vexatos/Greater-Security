package hangcow.greatersecurity.common.portcuils;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.prefab.BlockMachine;

public class BlockPort extends BlockMachine
{

	protected BlockPort(int par1)
	{
		super(par1, Material.rock);
		this.setCreativeTab(CreativeTabs.tabRedstone);
		this.setBlockName("port");
	}

	@Override
	public boolean onMachineActivated(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer, int side, float hitX, float hitY, float hitZ)
	{
		// TODO open gui
		return false;
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving entity)
	{
		int angle = MathHelper.floor_double((entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		int change = 2;

		switch (angle)
		{
			case 0:
				change = ForgeDirection.NORTH.ordinal();
				break;
			case 1:
				change = ForgeDirection.EAST.ordinal();
				break;
			case 2:
				change = ForgeDirection.SOUTH.ordinal();
				break;
			case 3:
				change = ForgeDirection.WEST.ordinal();
				break;
		}

		if (entity.rotationPitch < -70f) // up
		{
			change = ForgeDirection.DOWN.ordinal();
		}
		if (entity.rotationPitch > 70f) // down
		{
			change = ForgeDirection.UP.ordinal();
		}

		world.setBlockMetadataWithNotify(x, y, z, change);
	}

	@Override
	public int getBlockTexture(IBlockAccess iBlockAccess, int x, int y, int z, int side)
	{
		TileEntity tileEntity = iBlockAccess.getBlockTileEntity(x, y, z);
		if (tileEntity instanceof TileEntityPort)
		{
			if (side == ForgeDirection.getOrientation(iBlockAccess.getBlockMetadata(x, y, z)).ordinal())
			{
				return this.blockIndexInTexture + 1;
			}
		}

		return this.blockIndexInTexture;
	}

	@Override
	public boolean onUseWrench(World world, int x, int y, int z, EntityPlayer par5EntityPlayer, int side, float hitX, float hitY, float hitZ)
	{
		world.setBlockMetadataWithNotify(x, y, z, side);
		return true;
	}

	@Override
	public int getBlockTextureFromSideAndMetadata(int side, int metadata)
	{
		if (side == ForgeDirection.DOWN.ordinal())
		{
			return this.blockIndexInTexture + 1;
		}

		return this.blockIndexInTexture;
	}

	@Override
	public TileEntity createNewTileEntity(World var1)
	{
		return null;
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int meta)
	{
		return new TileEntityPort();
	}

}
