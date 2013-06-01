package hangcow.greatersecurity.common.fence.eltro;

import dark.library.machine.TileEntityRunnableMachine;
import net.minecraft.entity.Entity;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.prefab.CustomDamageSource;

public class TileEntityEltroFence extends TileEntityRunnableMachine
{

	private static final double WATT_PER_SHOCK = 30;
	private static final double WATT_PER_TICK = 10;
	private boolean canShock = true;

	/**
	 * Shock an entity if there is power
	 */
	public void shockEntity(Entity entity)
	{

		if (this.canShock && entity != null)
		{
			int damage = (int) (this.getVoltage() % 60);

			entity.attackEntityFrom(CustomDamageSource.electrocution, damage);
			// TODO knock back entity and cause disabling potion effects
			this.wattsReceived -= this.WATT_PER_SHOCK;
		}
	}

	@Override
	public double getWattBuffer()
	{
		return this.getRequest().getWatts() * 5;
	}

	@Override
	public ElectricityPack getRequest()
	{
		return new ElectricityPack(120, Math.max(this.WATT_PER_TICK / 120, 0));
	}

	@Override
	public double getVoltage()
	{
		/* THIS IS NOT RUNNING VOLTAGE BUT MAX */
		return 10000;
	}
}
