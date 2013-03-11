package hangcow.greatersecurity.common.door;

import hangcow.greatersecurity.common.GreaterSecurity;

import java.util.Random;

import universalelectricity.prefab.BlockMachine;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockLockedDoor2 extends BlockMachine
{
	protected BlockLockedDoor2(int id)
	{
		super(id, Material.iron);
		this.blockIndexInTexture = 97;

		float width = 0.5F;
		float height = 1.0F;
		this.setBlockBounds(0.5F - width, 0.0F, 0.5F - width, 0.5F + width, height, 0.5F + width);
	}

	@SideOnly(Side.CLIENT)
	/**
	 * Retrieves the block texture to use based on the display side. Args: iBlockAccess, x, y, z, side
	 */
	@Override
	public int getBlockTexture(IBlockAccess world, int x, int y, int z, int side)
	{
		if (side != 0 && side != 1)
		{
			int fullMeta = this.getFullMetadata(world, x, y, z);
			int textureID = this.blockIndexInTexture;

			if ((fullMeta & 8) != 0)
			{
				textureID -= 16;
			}

			int var8 = fullMeta & 3;
			boolean var9 = (fullMeta & 4) != 0;

			if (var9)
			{
				if (var8 == 0 && side == 2)
				{
					textureID = -textureID;
				}
				else if (var8 == 1 && side == 5)
				{
					textureID = -textureID;
				}
				else if (var8 == 2 && side == 3)
				{
					textureID = -textureID;
				}
				else if (var8 == 3 && side == 4)
				{
					textureID = -textureID;
				}
			}
			else
			{
				if (var8 == 0 && side == 5)
				{
					textureID = -textureID;
				}
				else if (var8 == 1 && side == 3)
				{
					textureID = -textureID;
				}
				else if (var8 == 2 && side == 4)
				{
					textureID = -textureID;
				}
				else if (var8 == 3 && side == 2)
				{
					textureID = -textureID;
				}

				if ((fullMeta & 16) != 0)
				{
					textureID = -textureID;
				}
			}

			return textureID;
		}
		else
		{
			return this.blockIndexInTexture;
		}
	}

	/**
	 * Is this block (a) opaque and (b) a full 1m cube? This determines whether or not to render the
	 * shared face of two adjacent blocks and also whether the player can attach torches, redstone
	 * wire, etc to this block.
	 */
	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	public boolean getBlocksMovement(IBlockAccess par1IBlockAccess, int x, int y, int z)
	{
		int var5 = this.getFullMetadata(par1IBlockAccess, x, y, z);
		return (var5 & 4) != 0;
	}

	/**
	 * If this block doesn't render as an ordinary block it will return False (examples: signs,
	 * buttons, stairs, etc)
	 */
	@Override
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
		return 7;
	}

	@SideOnly(Side.CLIENT)
	/**
	 * Returns the bounding box of the wired rectangular prism to render.
	 */
	@Override
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z)
	{
		this.setBlockBoundsBasedOnState(world, x, y, z);
		return super.getSelectedBoundingBoxFromPool(world, x, y, z);
	}

	/**
	 * Returns a bounding box from the pool of bounding boxes (this means this box can change after
	 * the pool has been cleared to be reused)
	 */
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
	{
		this.setBlockBoundsBasedOnState(par1World, par2, par3, par4);
		return super.getCollisionBoundingBoxFromPool(par1World, par2, par3, par4);
	}

	/**
	 * Updates the blocks bounds based on its current state. Args: world, x, y, z
	 */
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
	{
		this.setDoorRotation(this.getFullMetadata(par1IBlockAccess, par2, par3, par4));
	}

	/**
	 * Returns 0, 1, 2 or 3 depending on where the hinge is.
	 */
	public int getDoorOrientation(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
	{
		return this.getFullMetadata(par1IBlockAccess, par2, par3, par4) & 3;
	}

	public boolean isDoorOpen(IBlockAccess world, int x, int y, int z)
	{
		return (this.getFullMetadata(world, x, y, z) & 4) != 0;
	}

	private void setDoorRotation(int angle)
	{
		float var2 = 0.1875F;
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F);
		int var3 = angle & 3;
		boolean var4 = (angle & 4) != 0;
		boolean var5 = (angle & 16) != 0;

		if (var3 == 0)
		{
			if (var4)
			{
				if (!var5)
				{
					this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, var2);
				}
				else
				{
					this.setBlockBounds(0.0F, 0.0F, 1.0F - var2, 1.0F, 1.0F, 1.0F);
				}
			}
			else
			{
				this.setBlockBounds(0.0F, 0.0F, 0.0F, var2, 1.0F, 1.0F);
			}
		}
		else if (var3 == 1)
		{
			if (var4)
			{
				if (!var5)
				{
					this.setBlockBounds(1.0F - var2, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
				}
				else
				{
					this.setBlockBounds(0.0F, 0.0F, 0.0F, var2, 1.0F, 1.0F);
				}
			}
			else
			{
				this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, var2);
			}
		}
		else if (var3 == 2)
		{
			if (var4)
			{
				if (!var5)
				{
					this.setBlockBounds(0.0F, 0.0F, 1.0F - var2, 1.0F, 1.0F, 1.0F);
				}
				else
				{
					this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, var2);
				}
			}
			else
			{
				this.setBlockBounds(1.0F - var2, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
			}
		}
		else if (var3 == 3)
		{
			if (var4)
			{
				if (!var5)
				{
					this.setBlockBounds(0.0F, 0.0F, 0.0F, var2, 1.0F, 1.0F);
				}
				else
				{
					this.setBlockBounds(1.0F - var2, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
				}
			}
			else
			{
				this.setBlockBounds(0.0F, 0.0F, 1.0F - var2, 1.0F, 1.0F, 1.0F);
			}
		}
	}

	/**
	 * Called upon block activation (right click on the block.)
	 */
	@Override
	public boolean onMachineActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ)
	{
		TileEntity ent = world.getBlockTileEntity(x, y, z);
		if (!world.isRemote && ent instanceof TileEntityLockedDoor)
		{
			if (((TileEntityLockedDoor) ent).canOpen(player))
			{
				int fullMeta = this.getFullMetadata(world, x, y, z);
				int newMeta = fullMeta & 7;
				newMeta ^= 4;

				if ((fullMeta & 8) == 0)
				{
					world.setBlockMetadataWithNotify(x, y, z, newMeta);
					world.markBlockRangeForRenderUpdate(x, y, z, x, y, z);
				}
				else
				{
					world.setBlockMetadataWithNotify(x, y - 1, z, newMeta);
					world.markBlockRangeForRenderUpdate(x, y - 1, z, x, y, z);
				}
			}
		}
		return true;
	}

	/**
	 * Returns the ID of the items to drop on destruction.
	 */
	@Override
	public int idDropped(int par1, Random par2Random, int par3)
	{
		return (par1 & 8) != 0 ? 0 : GreaterSecurity.itemLockedDoor.itemID;
	}

	/**
	 * Ray traces through the blocks collision from start vector to end vector returning a ray trace
	 * hit. Args: world, x, y, z, startVec, endVec
	 */
	@Override
	public MovingObjectPosition collisionRayTrace(World world, int par2, int par3, int par4, Vec3 par5Vec3, Vec3 par6Vec3)
	{
		this.setBlockBoundsBasedOnState(world, par2, par3, par4);
		return super.collisionRayTrace(world, par2, par3, par4, par5Vec3, par6Vec3);
	}

	/**
	 * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y,
	 * z
	 */
	@Override
	public boolean canPlaceBlockAt(World world, int x, int y, int z)
	{
		return y >= 255 ? false : world.doesBlockHaveSolidTopSurface(x, y - 1, z) && super.canPlaceBlockAt(world, x, y, z) && super.canPlaceBlockAt(world, x, y + 1, z);
	}

	/**
	 * Returns the mobility information of the block, 0 = free, 1 = can't push but can move over, 2
	 * = total immobility and stop pistons
	 */
	@Override
	public int getMobilityFlag()
	{
		return 1;
	}

	/**
	 * Returns the full metadata value created by combining the metadata of both blocks the door
	 * takes up.
	 */
	public int getFullMetadata(IBlockAccess world, int x, int y, int z)
	{
		int blockMeta = world.getBlockMetadata(x, y, z);
		boolean isTop = (blockMeta & 8) != 0;
		int bottomMeta;
		int topMeta;

		if (isTop)
		{
			bottomMeta = world.getBlockMetadata(x, y - 1, z);
			topMeta = blockMeta;
		}
		else
		{
			bottomMeta = blockMeta;
			topMeta = world.getBlockMetadata(x, y + 1, z);
		}

		boolean var9 = (topMeta & 1) != 0;
		return bottomMeta & 7 | (isTop ? 8 : 0) | (var9 ? 16 : 0);
	}

	@SideOnly(Side.CLIENT)
	/**
	 * only called by clickMiddleMouseButton , and passed to inventory.setCurrentItem (along with isCreative)
	 */
	@Override
	public int idPicked(World world, int x, int y, int z)
	{
		return GreaterSecurity.itemLockedDoor.itemID;
	}

	/**
	 * Called when the block is attempted to be harvested
	 */
	@Override
	public void onBlockHarvested(World world, int x, int y, int z, int par5, EntityPlayer player)
	{
		if (player.capabilities.isCreativeMode && (par5 & 8) != 0 && world.getBlockId(x, y - 1, z) == this.blockID)
		{
			world.setBlockWithNotify(x, y - 1, z, 0);
		}
	}

	@Override
	public TileEntity createNewTileEntity(World var1)
	{
		return null;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata)
	{
		if ((metadata & 8) != 0)
		{
			return new TileEntityLockedDoor();
		}
		return createNewTileEntity(world);
	}

}
