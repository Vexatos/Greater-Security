package hangcow.greatersecurity.common.chest;

import hangcow.greatersecurity.common.GreaterSecurity;

import java.util.List;

import dark.library.locking.AccessLevel;
import dark.library.locking.UserAccess;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class ItemLockedChest extends ItemBlock
{

	private int blockID = GreaterSecurity.blockLockedChest.blockID;

	public ItemLockedChest(int par1)
	{
		super(par1);
		this.setIconIndex(0);
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
		int meta = itemstack.getItemDamage();
		switch (meta)
		{
			case 0:
				return "WoodenChest";
			case 1:
				return "StoneChest";
			case 2:
				return "IronChest";
			case 3:
				return "ObbyChest";
		}
		return "LockChest";
	}

	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int i, int j, int k, int side, float hitX, float hitY, float hitZ, int metadata)
	{
		for (int s = 0; s < 4; s++)
		{
			int deltaX = 0;
			int deltaZ = 0;

			switch (s)
			{
				case 0:
					deltaZ = -1;
					break;
				case 1:
					deltaZ = 1;
					break;
				case 2:
					deltaX = -1;
					break;
				case 3:
					deltaX = 1;
					break;
			}
			TileEntity e = world.getBlockTileEntity(i + deltaX, j, k + deltaZ);
			if (e instanceof TileEntityLockedChest)
			{
				int bType = ((TileEntityLockedChest) e).getType();
				if (world.getBlockId(i + deltaX, j, k + deltaZ) == this.blockID && bType != stack.getItemDamage())
				{
					return false;
				}
			}
		}
		int meta = stack.getItemDamage();
		int angle = MathHelper.floor_double((double) (player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		int sCount = 0;

		world.setBlockAndMetadataWithUpdate(i, j, k, this.blockID, (angle + (meta * 4)), true);

		TileEntityLockedChest blockEntity = (TileEntityLockedChest) world.getBlockTileEntity(i, j, k);

		if (!world.isRemote && player != null && blockEntity != null)
		{

			boolean spawnNormal = false;

			for (int s = 0; s < 4; s++)
			{
				int x = 0;
				int z = 0;
				switch (s)
				{
					case 0:
						z--;
						break;
					case 1:
						z++;
						break;
					case 2:
						x--;
						break;
					case 3:
						x++;
						break;
				}
				TileEntity C = world.getBlockTileEntity(i + x, j, k + z);
				if (C instanceof TileEntityLockedChest)
				{
					if (((TileEntityLockedChest) C).getType() == stack.getItemDamage())
					{
						TileEntityLockedChest LC = (TileEntityLockedChest) world.getBlockTileEntity(i + x, j, k + z);
						for (UserAccess user : LC.getUsers())
						{
							((TileEntityLockedChest) blockEntity).addUserAccess(user.username, user.level, user.shouldSave);
						}
						world.setBlockMetadataWithNotify(i + x, j, k + z, (stack.getItemDamage() * 4) + angle);
					}
					else
					{
						sCount++;
					}
				}
				else
				{
					sCount++;
					if (sCount >= 4)
					{
						spawnNormal = true;
						sCount = 0;
					}
				}
				if (spawnNormal)
				{
					world.setBlockMetadata(i, j, k, (angle + (meta * 4)));
					((TileEntityLockedChest) blockEntity).addUserAccess(player.username, AccessLevel.OWNER, true);
				}

			}
			sCount = 0;
			// player.sendChatToPlayer("Owner set to " + blockEntity.getOwner());

			boolean debug = false;
			if (debug)
			{
				ItemStack[] chestInv = ((TileEntityLockedChest) blockEntity).chestContents;
				for (int ss = 0; ss < chestInv.length; ss++)
				{
					ItemStack stone = new ItemStack(Block.stone, 64);
					chestInv[ss] = stone;
				}

			}
		}
		Block var12 = Block.blocksList[this.blockID];
		world.playSoundEffect((double) ((float) i + 0.5F), (double) ((float) j + 0.5F), (double) ((float) k + 0.5F), var12.stepSound.getStepSound(), (var12.stepSound.getVolume() + 1.0F) / 2.0F, var12.stepSound.getPitch() * 0.8F);

		return true;

	}

	@Override
	public int getIconFromDamage(int i)
	{
		switch (i)
		{
			case 0:
				return 0;
			case 1:
				return 3;
			case 2:
				return 4;
			case 3:
				return 5;
		}
		return this.iconIndex;
	}

	@Override
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		par3List.add(new ItemStack(this, 1, 0));
		par3List.add(new ItemStack(this, 1, 1));
		par3List.add(new ItemStack(this, 1, 2));
		par3List.add(new ItemStack(this, 1, 3));
	}

}
