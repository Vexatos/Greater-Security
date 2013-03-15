package hangcow.greatersecurity.common.door;

import hangcow.greatersecurity.common.GreaterSecurity;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import dark.library.locking.AccessLevel;
import dark.library.locking.UserAccess;

public class ItemLockedDoor extends Item
{

	public ItemLockedDoor(int par1)
	{
		super(par1);
		this.setMaxStackSize(3);
		this.setIconIndex(2);
		this.setCreativeTab(CreativeTabs.tabRedstone);
		this.setHasSubtypes(true);
	}

	@Override
	public String getTextureFile()
	{
		return GreaterSecurity.ITEM_File_PATH;

	}

	@Override
	public String getItemNameIS(ItemStack itemstack)
	{
		return "LockedDoor";
	}

	@Override
	public int getIconFromDamage(int i)
	{
		return this.iconIndex;
	}

	@Override
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		par3List.add(new ItemStack(this, 1, 0));
	}

	@Override
	public boolean onItemUse(ItemStack itemstack, EntityPlayer entityPlayer, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
	{
		if (side != 1)
		{
			return false;
		}
		else
		{
			++y;
			Block placedBlock = GreaterSecurity.blockLockedDoor;

			if (entityPlayer.canPlayerEdit(x, y, z, side, itemstack) && entityPlayer.canPlayerEdit(x, y + 1, z, side, itemstack))
			{
				if (!placedBlock.canPlaceBlockAt(world, x, y, z))
				{
					return false;
				}
				else
				{
					int angle = MathHelper.floor_double((double) ((entityPlayer.rotationYaw + 180.0F) * 4.0F / 360.0F) - 0.5D) & 3;
					placeDoorBlock(entityPlayer, world, x, y, z, angle, placedBlock);
					--itemstack.stackSize;
					return true;
				}
			}
			else
			{
				return false;
			}
		}
	}

	public static void placeDoorBlock(EntityPlayer player, World world, int x, int y, int z, int angle, Block placeBlock)
	{
		byte var6 = 0;
		byte var7 = 0;

		if (angle == 0)
		{
			var7 = 1;
		}

		if (angle == 1)
		{
			var6 = -1;
		}

		if (angle == 2)
		{
			var7 = -1;
		}

		if (angle == 3)
		{
			var6 = 1;
		}

		int var8 = (world.isBlockNormalCube(x - var6, y, z - var7) ? 1 : 0) + (world.isBlockNormalCube(x - var6, y + 1, z - var7) ? 1 : 0);
		int var9 = (world.isBlockNormalCube(x + var6, y, z + var7) ? 1 : 0) + (world.isBlockNormalCube(x + var6, y + 1, z + var7) ? 1 : 0);
		boolean var10 = world.getBlockId(x - var6, y, z - var7) == placeBlock.blockID || world.getBlockId(x - var6, y + 1, z - var7) == placeBlock.blockID;
		boolean var11 = world.getBlockId(x + var6, y, z + var7) == placeBlock.blockID || world.getBlockId(x + var6, y + 1, z + var7) == placeBlock.blockID;
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
		world.setBlockAndMetadataWithNotify(x, y, z, placeBlock.blockID, angle);
		world.setBlockAndMetadataWithNotify(x, y + 1, z, placeBlock.blockID, 8 | (var12 ? 1 : 0));
		world.editingBlocks = false;
		world.notifyBlocksOfNeighborChange(x, y, z, placeBlock.blockID);
		world.notifyBlocksOfNeighborChange(x, y + 1, z, placeBlock.blockID);

		TileEntity ent = BlockLockedDoor.getTileEntityDoor(world, x, y, z);
		if (ent instanceof TileEntityLockedDoor)
		{
			((TileEntityLockedDoor) ent).addUserAccess(new UserAccess(player.username, AccessLevel.OWNER, true), true);
		}
	}
}
