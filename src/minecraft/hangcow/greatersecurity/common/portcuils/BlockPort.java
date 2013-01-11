package hangcow.greatersecurity.common.portcuils;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockPort extends BlockContainer {

	protected BlockPort(int par1) {
		super(par1, Material.rock);
	}

	@Override
	public TileEntity createNewTileEntity(World var1) 
	{
		return null;
	}
	@Override
	public TileEntity createNewTileEntity(World var1, int meta) 
	{
		return null;
	}

}
