package hangcow.greatersecurity.common.door;

import hangcow.greatersecurity.common.GreaterSecurity;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class ItemDoor extends Item
{

	public ItemDoor(int par1)
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

	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
	{
		if (par7 != 1)
		{
			return false;
		}
		else
		{
			++par5;
			Block var11 = GreaterSecurity.BlockLDoor;

			if (par2EntityPlayer.canPlayerEdit(par4, par5, par6, par7, par1ItemStack) && par2EntityPlayer.canPlayerEdit(par4, par5 + 1, par6, par7, par1ItemStack))
			{
				if (!var11.canPlaceBlockAt(par3World, par4, par5, par6))
				{
					return false;
				}
				else
				{
					int var12 = MathHelper.floor_double((double) ((par2EntityPlayer.rotationYaw + 180.0F) * 4.0F / 360.0F) - 0.5D) & 3;
					GreaterSecurity.BlockLDoor.onBlockPlacedBy(par3World, par4, par5, par6, par2EntityPlayer);
					--par1ItemStack.stackSize;
					return true;
				}
			}
			else
			{
				return false;
			}
		}
	}
}