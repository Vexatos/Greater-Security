package hangcow.greatersecurity.common.WirelessRed;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockWRed extends BlockContainer{

	public BlockWRed(int par1) {
		super(par1, Material.rock);
		this.setCreativeTab(CreativeTabs.tabRedstone);
	}

	@Override
	public TileEntity createNewTileEntity(World var1) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
