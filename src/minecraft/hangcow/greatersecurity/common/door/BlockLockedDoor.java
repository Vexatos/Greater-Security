package hangcow.greatersecurity.common.door;

import hangcow.greatersecurity.common.GreaterSecurity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockLockedDoor extends BlockContainer
{
	public BlockLockedDoor(int par1)
	{
		super(par1, Material.wood);
		this.setHardness(10f);
		float var3 = 0.5F;
		float var4 = 1.0F;
		this.setBlockBounds(0, 0, 0, .5f, .4f, .2f);
		this.setBlockBoundsForItemRender();
		this.setResistance(10f);
		this.setTextureFile(GreaterSecurity.RESOURCE_PATH + "blocks.png");

	}

	public void setBlockBoundsForItemRender()
	{
		this.setBlockBounds(0, 0, 0, 1, 1f, .2f);
	}

	/**
	 * Returns the bounding box of the wired rectangular prism to render.
	 */
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
	{
		this.setBlockBoundsBasedOnState(par1World, par2, par3, par4);
		return super.getSelectedBoundingBoxFromPool(par1World, par2, par3, par4);
	}

	public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
	{
		return true;

	}

	public int getBlockTexture(IBlockAccess world, int x, int y, int z, int side)
	{
		int meta = world.getBlockMetadata(x, y, z);
		int aMeta = 14;
		if (meta < 8)
		{
			aMeta = world.getBlockMetadata(x, y + 1, z);
			switch (meta)
			{
				case 0:
				case 1:
				case 2:
				case 3:
			}
		}

		return 1;
	}

	/**
	 * Returns a bounding box from the pool of bounding boxes (this means this box can change after
	 * the pool has been cleared to be reused)
	 */
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
	{
		this.setBlockBoundsBasedOnState(par1World, par2, par3, par4);
		return super.getCollisionBoundingBoxFromPool(par1World, par2, par3, par4);
	}

	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
	{
		int meta = world.getBlockMetadata(x, y, z);
		int aboveMeta = 14;
		TileEntity entity = world.getBlockTileEntity(x, y, z);
		if (meta < 8)
		{
			aboveMeta = world.getBlockMetadata(x, y + 1, z);
		}

		if (meta < 4)
		{
			switch (meta)
			{
				case 2:
					setBlockBounds(0, 0, 0, 1, 2, .2f);
					break;
				case 3:
					setBlockBounds(.8f, 0, 0, 1, 2, 1);
					break;
				case 0:
					setBlockBounds(0, 0, .8f, 1, 2, 1);
					break;
				case 1:
					setBlockBounds(0, 0, 0, .2f, 2, 1);
					break;
			}
		}
		else if (meta > 3 && meta < 8)
		{
			if (aboveMeta == 14)
			{
				switch (meta)
				{
					case 5:
						setBlockBounds(0, 0, 0, 1, 2, .2f);
						break;
					case 6:
						setBlockBounds(.8f, 0, 0, 1, 2, 1);
						break;
					case 7:
						setBlockBounds(0, 0, .8f, 1, 2, 1);
						break;
					case 4:
						setBlockBounds(0, 0, 0, .2f, 2, 1);
						break;
				}
			}
			else
			{
				switch (meta)
				{
					case 7:
						setBlockBounds(0, 0, 0, 1, 2, .2f);
						break;
					case 4:
						setBlockBounds(.8f, 0, 0, 1, 2, 1);
						break;
					case 5:
						setBlockBounds(0, 0, .8f, 1, 2, 1);
						break;
					case 6:
						setBlockBounds(0, 0, 0, .2f, 2, 1);
						break;
				}

			}
		}
		else if (meta > 13)
		{
			setBlockBounds(0, .9f, 0, 0, 1f, 0);
		}
		else
		{
			setBlockBounds(0, 0, 0, 1, 2, 1);
		}
	}

	/**
	 * Is this block (a) opaque and (b) a full 1m cube? This determines whether or not to render the
	 * shared face of two adjacent blocks and also whether the player can attach torches, redstone
	 * wire, etc to this block.
	 */
	public boolean isOpaqueCube()
	{
		return false;
	}

	/**
	 * If this block doesn't render as an ordinary block it will return False (examples: signs,
	 * buttons, stairs, etc)
	 */
	public boolean renderAsNormalBlock()
	{
		return false;
	}

	/**
	 * The type of render function that is called for this block
	 */
	@Override
	public int getRenderType()
	{
		return 0;
	}

	/**
	 * Checks if block can be placed
	 */
	public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4)
	{
		return par3 >= 255 ? false : par1World.doesBlockHaveSolidTopSurface(par2, par3 - 1, par4) && super.canPlaceBlockAt(par1World, par2, par3, par4) && super.canPlaceBlockAt(par1World, par2, par3 + 1, par4);
	}

	public void breakBlock(World world, int x, int y, int z, int par5, int par6)
	{
		super.breakBlock(world, x, y, z, par5, par6);
		int meta = world.getBlockMetadata(x, y, z);
		if (meta < 8)
		{
			if (world.getBlockId(x, y + 1, z) == this.blockID && world.getBlockMetadata(x, y + 1, z) > 13)
			{
				world.setBlockAndMetadataWithUpdate(x, y, z, 0, 0, true);
			}
		}
		else if (meta > 13)
		{
			if (world.getBlockId(x, y - 1, z) == this.blockID && world.getBlockMetadata(x, y - 1, z) < 8)
			{
				world.setBlockAndMetadataWithUpdate(x, y, z, 0, 0, true);
			}
		}
	}

	/**
	 * Called when the block is clicked by a player. Args: x, y, z, entityPlayer
	 */
	public void onBlockClicked(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer)
	{
		this.onBlockActivated(par1World, par2, par3, par4, par5EntityPlayer, 0, 0.0F, 0.0F, 0.0F);
	}

	/**
	 * Called upon block activation (right click on the block.)
	 */
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
	{
		int meta = world.getBlockMetadata(x, y, z);
		int angle = MathHelper.floor_double((double) (player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		TileEntity te = world.getBlockTileEntity(x, y, z);
		if (world.isRemote)
		{
			return true;
		}
		else if (meta > 13 && te instanceof TileEntityLockedDoor)
		{
			boolean canOpen = ((TileEntityLockedDoor) te).canOpen(player);
			if (canOpen && !player.isSneaking())
			{
				activateDoor(world, x, y, z);
				return true;
			}
			else if (canOpen && player.isSneaking())
			{
				player.openGui(GreaterSecurity.instance, 3, world, x, y, z);
				return true;
			}
			else
			{
				player.sendChatToPlayer("Door is Locked");
			}
		}
		else
		{
			if (world.getBlockTileEntity(x, y + 1, z) instanceof TileEntityLockedDoor)
			{
				onBlockActivated(world, x, y + 1, z, player, par6, par7, par8, par9);
				return true;
			}

		}

		return false;
	}

	public static void activateDoor(World world, int x, int y, int z)
	{
		int metaB = world.getBlockMetadata(x, y - 1, z);
		if (metaB < 8)
		{
			if (metaB < 4)
			{
				world.setBlockMetadataWithNotify(x, y - 1, z, metaB + 4);
			}
			else
			{
				world.setBlockMetadataWithNotify(x, y - 1, z, metaB - 4);
			}
		}

		world.markBlockForUpdate(x, y, z);
		world.playAuxSFX(1003, x, y, z, 0);
	}

	public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
	{
		int meta = par1World.getBlockMetadata(par2, par3, par4);
		if (meta < 8)
		{
			if (par1World.getBlockId(par2, par3 + 1, par4) != this.blockID)
			{
				par1World.setBlockWithNotify(par2, par3, par4, 0);
			}
		}
		else if (meta > 13)
		{
			if (par1World.getBlockId(par2, par3 - 1, par4) != this.blockID)
			{
				par1World.setBlockWithNotify(par2, par3, par4, 0);
			}
		}

	}

	public void onBlockPlacedBy(World par1World, int i, int j, int k, EntityLiving par7E)
	{
		int angle = MathHelper.floor_double((double) (par7E.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		placeDoorBlock(par1World, par7E, i, j, k, angle, this);
		TileEntity te = par1World.getBlockTileEntity(i, j + 1, k);
		if (te instanceof TileEntityLockedDoor && par7E instanceof EntityPlayer)
		{
			((TileEntityLockedDoor) te).setOwner((EntityPlayer) par7E);
		}
	}

	public void placeDoorBlock(World world, EntityLiving entity, int x, int y, int z, int angle, Block par5Block)
	{
		byte var6 = 0;
		byte var7 = 0;

		if (angle == 3)
		{
			var7 = 1;
		}

		if (angle == 0)
		{
			var6 = -1;
		}

		if (angle == 1)
		{
			var7 = -1;
		}

		if (angle == 2)
		{
			var6 = 1;
		}

		int var8 = (world.isBlockNormalCube(x - var6, y, z - var7) ? 1 : 0) + (world.isBlockNormalCube(x - var6, y + 1, z - var7) ? 1 : 0);
		int var9 = (world.isBlockNormalCube(x + var6, y, z + var7) ? 1 : 0) + (world.isBlockNormalCube(x + var6, y + 1, z + var7) ? 1 : 0);
		boolean var10 = world.getBlockId(x - var6, y, z - var7) == this.blockID || world.getBlockId(x - var6, y + 1, z - var7) == this.blockID;
		boolean var11 = world.getBlockId(x + var6, y, z + var7) == this.blockID || world.getBlockId(x + var6, y + 1, z + var7) == this.blockID;
		boolean var12 = false;
		if (var10 && !var11)
		{
			var12 = true;
		}
		else if (var9 > var8)
		{
			var12 = true;
		}

		world.editingBlocks = true;
		world.setBlockAndMetadataWithNotify(x, y, z, par5Block.blockID, angle);
		world.setBlockAndMetadataWithNotify(x, y + 1, z, par5Block.blockID, var12 ? 14 : 15);
		world.editingBlocks = false;
		world.notifyBlocksOfNeighborChange(x, y, z, par5Block.blockID);
		world.notifyBlocksOfNeighborChange(x, y + 1, z, par5Block.blockID);
		TileEntity te = world.getBlockTileEntity(x, y, z);
	}

	public TileEntity createNewTileEntity(World par1World, int meta)
	{
		if (meta >= 0 && meta < 8)
		{
			return new TileEntityFake();
		}
		else if (meta > 13)
		{
			return new TileEntityLockedDoor();
		}
		return null;
	}

	@Override
	public TileEntity createNewTileEntity(World var1)
	{
		return null;
	}
}