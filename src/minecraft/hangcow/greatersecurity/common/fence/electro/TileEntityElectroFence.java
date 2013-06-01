package hangcow.greatersecurity.common.fence.electro;

import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.prefab.CustomDamageSource;
import dark.library.machine.TileEntityRunnableMachine;

public class TileEntityElectroFence extends TileEntityRunnableMachine
{

	private static final double WATT_PER_SHOCK = 30;
	private static final double WATT_PER_TICK = 10;
	/** How many blocks should the player be knocked-back when shocked? */
	private static final int KNOCKBACK = 3;

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
			
			int facing = MathHelper.floor_double((entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
			switch (facing){
			
			case 0:
				entity.motionZ -= this.KNOCKBACK;
				break;
				
			case 1:
				entity.motionX += this.KNOCKBACK;
				break;
				
			case 2:
				entity.motionZ += this.KNOCKBACK;
				break;
				
			case 3:
				entity.motionX -= this.KNOCKBACK;
				break;
				
			default:
				// TODO Possibly change, added for switch statement stability.
				entity.motionY += this.KNOCKBACK;
				
				// Facing source: http://www.minecraftwiki.net/wiki/Coordinates
			
			}
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
