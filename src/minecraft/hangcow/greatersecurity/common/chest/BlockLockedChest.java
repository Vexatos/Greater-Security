package hangcow.greatersecurity.common.chest;

import static net.minecraftforge.common.ForgeDirection.DOWN;
import hangcow.greatersecurity.common.CommonProxy;
import hangcow.greatersecurity.common.GreaterSecurity;

import java.util.Iterator;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import universalelectricity.prefab.block.BlockAdvanced;
import dark.library.access.AccessLevel;

public class BlockLockedChest extends BlockAdvanced
{
	public BlockLockedChest(int par1)
	{
		super(par1, Material.wood);
		this.setCreativeTab(CreativeTabs.tabDecorations);
		this.setUnlocalizedName("LockedChest");
		this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
		this.blockResistance = 1000F;// TODO set this based on tier if possible
		if (!GreaterSecurity.breakChests)
		{
			this.setBlockUnbreakable();
		}
	}

	@Override
	public float getBlockHardness(World world, int x, int y, int z)
	{
		if (GreaterSecurity.breakChests)
		{
			float hardness = 1000F;
			TileEntity ent = world.getBlockTileEntity(x, y, z);
			if (ent instanceof TileEntityLockedChest)
			{
				int type = ((TileEntityLockedChest) ent).getType();

			}
			return hardness;
		}
		else
		{
			return Float.MAX_VALUE;
		}
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}

	@Override
	public int getRenderType()
	{
		return 22;
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
	{
		if (world.getBlockId(x, y, z - 1) == this.blockID)
		{
			this.setBlockBounds(0.0625F, 0.0F, 0.0F, 0.9375F, 0.875F, 0.9375F);
		}
		else if (world.getBlockId(x, y, z + 1) == this.blockID)
		{
			this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 1.0F);
		}
		else if (world.getBlockId(x - 1, y, z) == this.blockID)
		{
			this.setBlockBounds(0.0F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
		}
		else if (world.getBlockId(x + 1, y, z) == this.blockID)
		{
			this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 1.0F, 0.875F, 0.9375F);
		}
		else
		{
			this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
		}
	}

	@Override
	public void onBlockAdded(World world, int x, int y, int z)
	{
		super.onBlockAdded(world, x, y, z);
		this.unifyAdjacentChests(world, x, y, z);

		int westBlock = world.getBlockId(x, y, z - 1);
		int eastBlock = world.getBlockId(x, y, z + 1);
		int northBlock = world.getBlockId(x - 1, y, z);
		int southBlock = world.getBlockId(x + 1, y, z);

		if (westBlock == this.blockID)
		{
			this.unifyAdjacentChests(world, x, y, z - 1);
		}

		if (eastBlock == this.blockID)
		{
			this.unifyAdjacentChests(world, x, y, z + 1);
		}

		if (northBlock == this.blockID)
		{
			this.unifyAdjacentChests(world, x - 1, y, z);
		}

		if (southBlock == this.blockID)
		{
			this.unifyAdjacentChests(world, x + 1, y, z);
		}
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving entityLiving, ItemStack stack)
	{
		int westBlock = world.getBlockId(x, y, z - 1);
		int eastBlock = world.getBlockId(x, y, z + 1);
		int northBlock = world.getBlockId(x - 1, y, z);
		int southBlock = world.getBlockId(x + 1, y, z);
		byte placementMeta = 0;
		int angle = MathHelper.floor_double((double) (entityLiving.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

		if (angle == 0)
		{
			placementMeta = 2;
		}

		if (angle == 1)
		{
			placementMeta = 5;
		}

		if (angle == 2)
		{
			placementMeta = 3;
		}

		if (angle == 3)
		{
			placementMeta = 4;
		}

		if (westBlock != this.blockID && eastBlock != this.blockID && northBlock != this.blockID && southBlock != this.blockID)
		{
			world.setBlockMetadataWithNotify(x, y, z, placementMeta, 3);
		}
		else
		{
			if ((westBlock == this.blockID || eastBlock == this.blockID) && (placementMeta == 4 || placementMeta == 5))
			{
				if (westBlock == this.blockID)
				{
					world.setBlockMetadataWithNotify(x, y, z - 1, placementMeta, 3);
				}
				else
				{
					world.setBlockMetadataWithNotify(x, y, z + 1, placementMeta, 3);
				}

				world.setBlockMetadataWithNotify(x, y, z, placementMeta, 3);
			}

			if ((northBlock == this.blockID || southBlock == this.blockID) && (placementMeta == 2 || placementMeta == 3))
			{
				if (northBlock == this.blockID)
				{
					world.setBlockMetadataWithNotify(x - 1, y, z, placementMeta, 3);
				}
				else
				{
					world.setBlockMetadataWithNotify(x + 1, y, z, placementMeta, 3);
				}

				world.setBlockMetadataWithNotify(x, y, z, placementMeta, 3);
			}
		}
		TileEntity ent = world.getBlockTileEntity(x, y, z);
		if (entityLiving instanceof EntityPlayer && ent instanceof TileEntityLockedChest)
		{
			((TileEntityLockedChest) ent).addUserAccess(((EntityPlayer) entityLiving).username, AccessLevel.OWNER, true);
		}
	}

	/**
	 * Turns the adjacent chests to a double chest.
	 */
	public void unifyAdjacentChests(World world, int x, int y, int z)
	{
		if (!world.isRemote)
		{
			int westBlock = world.getBlockId(x, y, z - 1);
			int eastBlock = world.getBlockId(x, y, z + 1);
			int northBlock = world.getBlockId(x - 1, y, z);
			int southBlock = world.getBlockId(x + 1, y, z);

			boolean var9 = true;
			int var10;
			int var11;
			boolean var12;
			byte var13;
			int var14;

			if (westBlock != this.blockID && eastBlock != this.blockID)
			{
				if (northBlock != this.blockID && southBlock != this.blockID)
				{
					var13 = 3;

					if (Block.opaqueCubeLookup[westBlock] && !Block.opaqueCubeLookup[eastBlock])
					{
						var13 = 3;
					}

					if (Block.opaqueCubeLookup[eastBlock] && !Block.opaqueCubeLookup[westBlock])
					{
						var13 = 2;
					}

					if (Block.opaqueCubeLookup[northBlock] && !Block.opaqueCubeLookup[southBlock])
					{
						var13 = 5;
					}

					if (Block.opaqueCubeLookup[southBlock] && !Block.opaqueCubeLookup[northBlock])
					{
						var13 = 4;
					}
				}
				else
				{
					var10 = world.getBlockId(northBlock == this.blockID ? x - 1 : x + 1, y, z - 1);
					var11 = world.getBlockId(northBlock == this.blockID ? x - 1 : x + 1, y, z + 1);
					var13 = 3;
					var12 = true;

					if (northBlock == this.blockID)
					{
						var14 = world.getBlockMetadata(x - 1, y, z);
					}
					else
					{
						var14 = world.getBlockMetadata(x + 1, y, z);
					}

					if (var14 == 2)
					{
						var13 = 2;
					}

					if ((Block.opaqueCubeLookup[westBlock] || Block.opaqueCubeLookup[var10]) && !Block.opaqueCubeLookup[eastBlock] && !Block.opaqueCubeLookup[var11])
					{
						var13 = 3;
					}

					if ((Block.opaqueCubeLookup[eastBlock] || Block.opaqueCubeLookup[var11]) && !Block.opaqueCubeLookup[westBlock] && !Block.opaqueCubeLookup[var10])
					{
						var13 = 2;
					}
				}
			}
			else
			{
				var10 = world.getBlockId(x - 1, y, westBlock == this.blockID ? z - 1 : z + 1);
				var11 = world.getBlockId(x + 1, y, westBlock == this.blockID ? z - 1 : z + 1);
				var13 = 5;
				var12 = true;

				if (westBlock == this.blockID)
				{
					var14 = world.getBlockMetadata(x, y, z - 1);
				}
				else
				{
					var14 = world.getBlockMetadata(x, y, z + 1);
				}

				if (var14 == 4)
				{
					var13 = 4;
				}

				if ((Block.opaqueCubeLookup[northBlock] || Block.opaqueCubeLookup[var10]) && !Block.opaqueCubeLookup[southBlock] && !Block.opaqueCubeLookup[var11])
				{
					var13 = 5;
				}

				if ((Block.opaqueCubeLookup[southBlock] || Block.opaqueCubeLookup[var11]) && !Block.opaqueCubeLookup[northBlock] && !Block.opaqueCubeLookup[var10])
				{
					var13 = 4;
				}
			}

			world.setBlockMetadataWithNotify(x, y, z, var13, 3);
		}
	}

	@Override
	public boolean canPlaceBlockAt(World world, int x, int y, int z)
	{
		int chestCount = 0;

		if (world.getBlockId(x - 1, y, z) == this.blockID)
		{
			++chestCount;
		}

		if (world.getBlockId(x + 1, y, z) == this.blockID)
		{
			++chestCount;
		}

		if (world.getBlockId(x, y, z - 1) == this.blockID)
		{
			++chestCount;
		}

		if (world.getBlockId(x, y, z + 1) == this.blockID)
		{
			++chestCount;
		}

		return chestCount > 1 ? false : (this.isThereANeighborChest(world, x - 1, y, z) ? false : (this.isThereANeighborChest(world, x + 1, y, z) ? false : (this.isThereANeighborChest(world, x, y, z - 1) ? false : !this.isThereANeighborChest(world, x, y, z + 1))));
	}

	/**
	 * Checks the neighbor blocks to see if there is a chest there. Args: world, x, y, z
	 */
	private boolean isThereANeighborChest(World world, int x, int y, int z)
	{
		return world.getBlockId(x, y, z) != this.blockID ? false : (world.getBlockId(x - 1, y, z) == this.blockID ? true : (world.getBlockId(x + 1, y, z) == this.blockID ? true : (world.getBlockId(x, y, z - 1) == this.blockID ? true : world.getBlockId(x, y, z + 1) == this.blockID)));
	}

	/**
	 * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed
	 * (coordinates passed are their own) Args: x, y, z, neighbor blockID
	 */
	public void onNeighborBlockChange(World world, int x, int y, int z, int blockID)
	{
		super.onNeighborBlockChange(world, x, y, z, blockID);
		TileEntityLockedChest chest = (TileEntityLockedChest) world.getBlockTileEntity(x, y, z);

		if (chest != null)
		{
			chest.updateContainingBlockInfo();
		}
	}

	@Override
	public boolean onMachineActivated(World world, int x, int y, int z, EntityPlayer player, int side, float xHit, float yHit, float zHit)
	{
		Object var10 = world.getBlockTileEntity(x, y, z);

		if (world.isRemote)
		{
			return true;
		}
		else if (!(var10 instanceof TileEntityLockedChest))
		{
			return true;
		}
		else if (world.isBlockSolidOnSide(x, y + 1, z, DOWN))
		{
			return true;
		}
		else if (isOcelotBlockingChest(world, x, y, z))
		{
			return true;
		}
		else if (world.getBlockId(x - 1, y, z) == this.blockID && (world.isBlockSolidOnSide(x - 1, y + 1, z, DOWN) || isOcelotBlockingChest(world, x - 1, y, z)))
		{
			return true;
		}
		else if (world.getBlockId(x + 1, y, z) == this.blockID && (world.isBlockSolidOnSide(x + 1, y + 1, z, DOWN) || isOcelotBlockingChest(world, x + 1, y, z)))
		{
			return true;
		}
		else if (world.getBlockId(x, y, z - 1) == this.blockID && (world.isBlockSolidOnSide(x, y + 1, z - 1, DOWN) || isOcelotBlockingChest(world, x, y, z - 1)))
		{
			return true;
		}
		else if (world.getBlockId(x, y, z + 1) == this.blockID && (world.isBlockSolidOnSide(x, y + 1, z + 1, DOWN) || isOcelotBlockingChest(world, x, y, z + 1)))
		{
			return true;
		}
		else if (var10 instanceof TileEntityLockedChest && !((TileEntityLockedChest) var10).canUserAccess(player.username))
		{
			player.sendChatToPlayer("-=|[Locked]|=-");
			return true;
		}
		else
		{
			if (world.getBlockId(x - 1, y, z) == this.blockID)
			{
				var10 = new InventoryLargeChest("container.chestDouble", (IInventory) world.getBlockTileEntity(x - 1, y, z), (IInventory) var10);
			}

			if (world.getBlockId(x + 1, y, z) == this.blockID)
			{
				var10 = new InventoryLargeChest("container.chestDouble", (IInventory) var10, (IInventory) world.getBlockTileEntity(x + 1, y, z));
			}

			if (world.getBlockId(x, y, z - 1) == this.blockID)
			{
				var10 = new InventoryLargeChest("container.chestDouble", (IInventory) world.getBlockTileEntity(x, y, z - 1), (IInventory) var10);
			}

			if (world.getBlockId(x, y, z + 1) == this.blockID)
			{
				var10 = new InventoryLargeChest("container.chestDouble", (IInventory) var10, (IInventory) world.getBlockTileEntity(x, y, z + 1));
			}
			player.displayGUIChest((IInventory) var10);
			return true;
		}
	}

	@Override
	public boolean onSneakMachineActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ)
	{
		TileEntity ent = world.getBlockTileEntity(x, y, z);
		if (world.isRemote)
		{
			return true;
		}
		if (ent instanceof TileEntityLockedChest)
		{
			if (((TileEntityLockedChest) ent).getUserAccess(player.username).ordinal() >= AccessLevel.ADMIN.ordinal() || ((TileEntityLockedChest) ent).getUsersWithAcess(AccessLevel.ADMIN).size() <= 0)
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
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return new TileEntityLockedChest();
	}

	/**
	 * Looks for a sitting ocelot within certain bounds. Such an ocelot is considered to be blocking
	 * access to the chest.
	 */
	public static boolean isOcelotBlockingChest(World world, int x, int y, int z)
	{
		Iterator var4 = world.getEntitiesWithinAABB(EntityOcelot.class, AxisAlignedBB.getAABBPool().getAABB((double) x, (double) (y + 1), (double) z, (double) (x + 1), (double) (y + 2), (double) (z + 1))).iterator();
		EntityOcelot var6;

		do
		{
			if (!var4.hasNext())
			{
				return false;
			}

			EntityOcelot var5 = (EntityOcelot) var4.next();
			var6 = (EntityOcelot) var5;
		}
		while (!var6.isSitting());

		return true;
	}
}
