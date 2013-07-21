package dark.security.common;

import net.minecraft.block.material.Material;
import dark.library.machine.BlockMachine;

public abstract class BlockGS extends BlockMachine
{

	protected BlockGS(String name, int blockID, Material material)
	{
		super(name, GreaterSecurity.config, blockID, material);
		this.setCreativeTab(GreaterSecurity.tabGreaterSecurity);
	}

}
