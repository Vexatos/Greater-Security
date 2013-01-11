package hangcow.greatersecurity.common.chest;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerLockedChest extends Container
{
    private IInventory lowerChestInventory;
    private int numRows;
    private int slotOption = 1;

    public ContainerLockedChest(IInventory player, IInventory tileEntity,int SlotOP)
    {
        this.lowerChestInventory = tileEntity;
        this.numRows = tileEntity.getSizeInventory() / 9;
        int var3 = (this.numRows - 4) * 18;
        int var4;
        int var5;
        this.slotOption = SlotOP;
        
        if(slotOption != 0)
        {
	        if(slotOption ==1)
	        {
	        	//Draw 27 slot chest inv
		        for (var4 = 0; var4 < this.numRows; ++var4)
		        {
		            for (var5 = 0; var5 < 9; ++var5)
		            {
		                this.addSlotToContainer(new Slot(tileEntity, var5 + var4 * 9, 8 + var5 * 18, 18 + var4 * 18));
		            }
		        }
	        } 
	        //player inv
	        for (var4 = 0; var4 < 3; ++var4)
	        {
	            for (var5 = 0; var5 < 9; ++var5)
	            {
	                this.addSlotToContainer(new Slot(player, var5 + var4 * 9 + 9, 8 + var5 * 18, 104 + var4 * 18 + var3));
	            }
	        }
	        //player quick bar
	        for (var4 = 0; var4 < 9; ++var4)
	        {
	            this.addSlotToContainer(new Slot(player, var4, 8 + var4 * 18, 162 + var3));
	        }
        }
    }

    public boolean canInteractWith(EntityPlayer par1EntityPlayer)
    {
        return this.lowerChestInventory.isUseableByPlayer(par1EntityPlayer);
    }
    
    //Called to transfer a stack from one inventory to the other eg. when shift clicking.
    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
    {
        ItemStack var3 = null;
        Slot var4 = (Slot)this.inventorySlots.get(par2);

        if (var4 != null && var4.getHasStack())
        {
            ItemStack var5 = var4.getStack();
            var3 = var5.copy();

            if (par2 < this.numRows * 9)
            {
                if (!this.mergeItemStack(var5, this.numRows * 9, this.inventorySlots.size(), true))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(var5, 0, this.numRows * 9, false))
            {
                return null;
            }

            if (var5.stackSize == 0)
            {
                var4.putStack((ItemStack)null);
            }
            else
            {
                var4.onSlotChanged();
            }
        }

        return var3;
    }

    //Callback for when the crafting gui is closed.   
    public void onCraftGuiClosed(EntityPlayer par1EntityPlayer)
    {
        super.onCraftGuiClosed(par1EntityPlayer);
        this.lowerChestInventory.closeChest();
    }
    
    public IInventory func_85151_d()
    {
        return this.lowerChestInventory;
    }
}
