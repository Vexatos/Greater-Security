package hangcow.greatersecurity.common.laser;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
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
				if(meta > 5)
				{
					meta = meta % 6;
				}else
				{
					meta += 6;
				}
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
				// open setting gui
			}
		}
		return this.onUseWrench(world, x, y, z, entityPlayer, side, hitX, hitY, hitZ);
	}
	
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving entityLiving, ItemStack stack)
	{
		
		if (world.getBlockTileEntity(x, y, z) instanceof TileEntityLaserFence && entityLiving instanceof EntityPlayer)
		{
			TileEntityLaserFence fence = (TileEntityLaserFence) world.getBlockTileEntity(x, y, z);
			fence.addUserAccess(new UserAccess(((EntityPlayer) entityLiving).username, AccessLevel.OWNER, true), !world.isRemote);
		}
	}

}
