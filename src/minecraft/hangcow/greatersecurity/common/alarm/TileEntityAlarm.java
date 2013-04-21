package hangcow.greatersecurity.common.alarm;

import net.minecraftforge.common.ForgeDirection;
import universalelectricity.prefab.tile.TileEntityElectricityRunnable;

public class TileEntityAlarm extends TileEntityElectricityRunnable
{
	public ForgeDirection getFacing()
	{
		return null;
		
	}
	@Override
	public boolean canConnect(ForgeDirection direction)
	{
		return direction == getFacing().getOpposite();
	}

}
