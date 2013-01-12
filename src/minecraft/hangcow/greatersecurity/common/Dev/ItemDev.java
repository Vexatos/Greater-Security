package hangcow.greatersecurity.common.Dev;

import hangcow.greatersecurity.common.GreaterSecurity;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemDev extends Item
{

    public ItemDev(int par1)
    {
        super(par1);
        this.setHasSubtypes(true);
        this.setMaxStackSize(1);
        this.setMaxDamage(0);
    }
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        par3List.add(new ItemStack(par1, 1, 0));
    }
    public String getTextureFile()
    {
        return GreaterSecurity.ITEM_File_PATH;
    }
    
    public boolean onItemUse(ItemStack itemstack, EntityPlayer player, World world, int x, int y, int z, int par7, float par8, float par9, float par10)
    {
        return false;
    }
    
    public int getIconFromDamage(int par1)
    {
        return this.iconIndex;
    }
}
