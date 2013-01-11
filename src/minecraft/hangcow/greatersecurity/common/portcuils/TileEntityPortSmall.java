package hangcow.greatersecurity.common.portcuils;

import hangcow.greatersecurity.api.IPorticuils;
import net.minecraft.tileentity.TileEntity;

public class TileEntityPortSmall extends TileEntity implements IPorticuils
{
	private boolean isExtended = false;
	 public int getMaxSize()
	 {
		 return 6;
	 }

	 public boolean extend(boolean doExtend)
	 {
		 if(isExtended()) return false;
		 if(doExtend)
		 {
			 //TODO cause it to extend
		 }
		 return false;
	 }

	@Override
	public boolean retract(boolean doRetract) {
		if(!isExtended()) return false;
		 if(doRetract)
		 {
			 //TODO cause it to retract
		 }
		return false;
	}

	@Override
	public boolean isExtended() {
		return isExtended;
	}

}
