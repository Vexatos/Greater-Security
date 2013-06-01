package hangcow.greatersecurity.common.fence.eltro;

import net.minecraft.entity.Entity;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.prefab.CustomDamageSource;
import dark.library.machine.TileEntityRunnableMachine;

public class TileEntityEltroFence extends TileEntityRunnableMachine
{

	private static final double WATT_PER_SHOCK = 30;
	private static final double WATT_PER_TICK = 10;

	/**
	 * Shock an entity if there is power
	 */
	public void shockEntity(Entity entity)
	{

		if (entity != null && this.wattsReceived >= this.WATT_PER_SHOCK)
		{
			int damage = 1; // getVoltage() always return the same #

			/* DAMAGE PER TICK OF COLLISION (20ticks a sec) */
			entity.attackEntityFrom(CustomDamageSource.electrocution.setDeathMessage("%1$s tried to climb an electric fence!"), damage);

			// TODO knock back entity and cause disabling potion effects
			this.wattsReceived -= this.WATT_PER_SHOCK;
		}
	}

	@Override
	public double getWattBuffer()
	{
		return (this.WATT_PER_TICK + this.WATT_PER_SHOCK) * 5;
	}

	@Override
	public ElectricityPack getRequest()
	{
		return new ElectricityPack(120, Math.max((this.getWattBuffer() - this.wattsReceived) / 120, 0));
	}
}
