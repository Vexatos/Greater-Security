package hangcow.greatersecurity.common.detector;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

public class TileEntityDetector extends TileEntity{
	
	private int requiredItem;

	public int getRequiredItem() {
		
		return this.requiredItem;
		
	}
	
	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readFromNBT(par1NBTTagCompound);
		NBTTagCompound var4 = new NBTTagCompound();
		this.requiredItem = var4.getInteger("RequiredItem");
	}

	@Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.writeToNBT(par1NBTTagCompound);

		NBTTagCompound var4 = new NBTTagCompound();
		var4.setInteger("RequiredItem", (Integer) this.requiredItem);

	}

}