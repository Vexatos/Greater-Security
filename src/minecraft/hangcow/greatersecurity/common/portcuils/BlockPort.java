package hangcow.greatersecurity.common.portcuils;

import universalelectricity.prefab.BlockMachine;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

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
	public boolean onSneakUseWrench(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer, int side, float hitX, float hitY, float hitZ)
	{
		// TODO rotate to face the player
		return false;
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
