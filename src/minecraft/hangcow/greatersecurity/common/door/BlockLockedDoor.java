package hangcow.greatersecurity.common.door;

import hangcow.greatersecurity.common.CommonProxy;
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
import dark.library.locking.AccessLevel;

public class BlockLockedDoor extends BlockMachine
{
	public BlockLockedDoor(int id)
	{
		super(id, Material.iron);
		this.blockIndexInTexture = 34;

		float width = 0.5F;
		float height = 1.0F;
		this.setBlockBounds(0.5F - width, 0.0F, 0.5F - width, 0.5F + width, height, 0.5F + width);
		this.setResistance(10f);
		this.setTextureFile(GreaterSecurity.BLOCK_File_PATH);
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

	public static boolean isDoorOpen(IBlockAccess world, int x, int y, int z)
	{
		return (getFullMetadata(world, x, y, z) & 4) != 0;
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

	public static TileEntity getTileEntityDoor(World world, int x, int y, int z)
	{
		TileEntity ent;
		int blockMeta = world.getBlockMetadata(x, y, z);

		if ((blockMeta & 8) == 0)
		{
			ent = world.getBlockTileEntity(x, y, z);
		}
		else
		{
			ent = world.getBlockTileEntity(x, y - 1, z);
		}
		return ent;

	}

	/**
	 * Called upon block activation (right click on the block.)
	 */
	@Override
	public boolean onMachineActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ)
	{
		TileEntity ent = getTileEntityDoor(world, x, y, z);
		if (world.isRemote)
		{
			return true;
		}
		if (ent instanceof TileEntityLockedDoor && ((TileEntityLockedDoor) ent).canAccess(player))
		{
			this.activateDoor((TileEntityLockedDoor) ent);
		}
		else
		{
			player.sendChatToPlayer("-=|[Locked]|=-");
		}
		return true;
	}

	public boolean onSneakMachineActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ)
	{
		TileEntity ent = getTileEntityDoor(world, x, y, z);
		if (world.isRemote)
		{
			return true;
		}
		if (ent instanceof TileEntityLockedDoor)
		{
			if (((TileEntityLockedDoor) ent).getUserAccess(player.username).ordinal() >= AccessLevel.ADMIN.ordinal())
			{
				player.openGui(GreaterSecurity.instance, CommonProxy.USERACCESS_GUI, ent.worldObj, ent.xCoord, ent.yCoord, ent.zCoord);
				return true;
			}
			else
			{
				player.sendChatToPlayer("-=|[Locked]|=-");
			}
		}
		else
		{
			player.sendChatToPlayer("-=|[Error]|=-");
		}

		return true;
	}

	public static void activateDoor(TileEntityLockedDoor door)
	{

		int fullMeta = getFullMetadata(door.worldObj, door.xCoord, door.yCoord, door.zCoord);
		int newMeta = fullMeta & 7;
		newMeta ^= 4;

		door.isOpen = !isDoorOpen(door.worldObj, door.xCoord, door.yCoord, door.zCoord);

		if ((fullMeta & 8) == 0)
		{
			door.worldObj.setBlockMetadataWithNotify(door.xCoord, door.yCoord, door.zCoord, newMeta);
			door.worldObj.markBlockRangeForRenderUpdate(door.xCoord, door.yCoord, door.zCoord, door.xCoord, door.yCoord, door.zCoord);
			door.worldObj.markBlockForUpdate(door.xCoord, door.yCoord, door.zCoord);
		}
		else
		{
			door.worldObj.setBlockMetadataWithNotify(door.xCoord, door.yCoord - 1, door.xCoord, newMeta);
			door.worldObj.markBlockRangeForRenderUpdate(door.xCoord, door.yCoord - 1, door.zCoord, door.xCoord, door.yCoord, door.zCoord);
			door.worldObj.markBlockForUpdate(door.xCoord, door.yCoord - 1, door.zCoord);
		}
	}

	/**
	 * Returns the ID of the items to drop on destruction.
	 */
	@Override
	public int idDropped(int meta, Random par2Random, int par3)
	{
		return (meta & 8) != 0 ? 0 : GreaterSecurity.itemLockedDoor.itemID;
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
	public static int getFullMetadata(IBlockAccess world, int x, int y, int z)
	{
		int blockMeta = world.getBlockMetadata(x, y, z);
		int bottomMeta;
		boolean var6 = (blockMeta & 8) != 0;
		int topMeta;

		if (var6)
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
		return bottomMeta & 7 | (var6 ? 8 : 0) | (var9 ? 16 : 0);
	}

	public void onNeighborBlockChange(World world, int x, int y, int z, int blockID)
	{
		int blockMeta = world.getBlockMetadata(x, y, z);

		if ((blockMeta & 8) == 0)
		{
			boolean shouldRemove = false;

			if (world.getBlockId(x, y + 1, z) != this.blockID)
			{
				world.setBlockWithNotify(x, y, z, 0);
				shouldRemove = true;
			}

			if (!world.doesBlockHaveSolidTopSurface(x, y - 1, z))
			{
				world.setBlockWithNotify(x, y, z, 0);
				shouldRemove = true;

				if (world.getBlockId(x, y + 1, z) == this.blockID)
				{
					world.setBlockWithNotify(x, y + 1, z, 0);
				}
			}

			if (shouldRemove)
			{
				if (!world.isRemote)
				{
					this.dropBlockAsItem(world, x, y, z, blockMeta, 0);
				}
			}
		}
		else
		{
			if (world.getBlockId(x, y - 1, z) != this.blockID)
			{
				world.setBlockWithNotify(x, y, z, 0);
			}

			if (blockID > 0 && blockID != this.blockID)
			{
				this.onNeighborBlockChange(world, x, y - 1, z, blockID);
			}
		}
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
	public void onBlockHarvested(World world, int x, int y, int z, int meta, EntityPlayer player)
	{
		if (player.capabilities.isCreativeMode && (meta & 8) != 0 && world.getBlockId(x, y - 1, z) == this.blockID)
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
	public TileEntity createNewTileEntity(World world, int meta)
	{
		if ((meta & 8) == 0)
		{
			return new TileEntityLockedDoor();
		}
		return createNewTileEntity(world);
	}

}
