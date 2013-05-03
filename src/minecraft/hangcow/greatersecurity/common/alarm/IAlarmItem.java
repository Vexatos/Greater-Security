package hangcow.greatersecurity.common.alarm;

import net.minecraft.world.World;
import universalelectricity.core.vector.Vector3;

public interface IAlarmItem
{
	/**
	 * Can this item get the event
	 * 
	 * @param from - what sent the event (TileEntity, NPC, CLass)
	 * @param location - where the alarm came from
	 * @param event - The type of event that was triggered
	 */
	public boolean canGetEvent(World world, Vector3 location, Object from, AlarmEvents event);
}
