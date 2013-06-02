package hangcow.greatersecurity.common.detector;

import hangcow.greatersecurity.common.GreaterSecurity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockDetector extends BlockDoor{
	
	public BlockDetector(int par1) {
		super(par1, Material.iron);
		setUnlocalizedName("ItemDetector");
		setCreativeTab(GreaterSecurity.tabGreaterSecurity);
		if (!GreaterSecurity.breakDetectors){
			
			setBlockUnbreakable();
			
		}else{
		
			setHardness(10F);
		
		}
		setResistance(100F);
	}
	
	@Override
	public boolean isOpaqueCube(){
		
		return false;
		
	}
	
	@Override
	public boolean renderAsNormalBlock(){
		
		return false;
		
	}
	
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving entityLiving, ItemStack itemStack){
		
		int angle = MathHelper.floor_double((entityLiving.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		int change = 0;
		
		if (angle == 0 || angle == 2){
			
			change = 0;
			
		}else{
			
			change = 1;
			
		}
		
		world.setBlockMetadataWithNotify(x, y, z, change, 3);
				
	}
	
	@Override
	public void onPoweredBlockChange(World par1World, int par2, int par3, int par4, boolean par5){
		
		TileEntityDetector tileEntity = (TileEntityDetector) par1World.getBlockTileEntity(par2, par3, par4);
		
		if (tileEntity.isDetectorPowered(tileEntity)){
			
			tileEntity.setDetectorPowered(tileEntity, false);
			
		}else{
			
			tileEntity.setDetectorPowered(tileEntity, true);
			
		}
		
	}
	
	// TODO Set block bounds, register icons and possible GUI display

}
