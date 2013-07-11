package hangcow.greatersecurity.common.fence.electro;

import hangcow.greatersecurity.common.CommonProxy;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.prefab.CustomDamageSource;
import dark.library.machine.TileEntityRunnableMachine;

public class TileEntityElectroFence extends TileEntityRunnableMachine
{

	private static final float WATT_PER_SHOCK = 30;
	private static final float WATT_PER_TICK = 10;
	/** How many blocks should the player be knocked-back when shocked? */
	private static final int KNOCKBACK = 3;

	public TileEntityElectroFence()
	{
		super(10);
	}

	/** Shock an entity if there is power */
	public void shockEntity(Entity entity)
	{

		if (entity != null && this.getEnergyStored() >= this.WATT_PER_SHOCK)
		{
			int damage = 1; // getVoltage() always return the same #

			/* DAMAGE PER TICK OF COLLISION (20ticks a sec) */
			entity.attackEntityFrom(CustomDamageSource.electrocution.setDeathMessage("%1$s tried to climb an electric fence!"), damage);

			// TODO knock back entity and cause disabling potion effects
			this.setEnergyStored(this.getEnergyStored() - this.WATT_PER_SHOCK);

			// TODO Added push method to CommonPorxy for use in multiple classes.
			CommonProxy.entityPush(entity, this.KNOCKBACK, false);

			entity.motionY += this.KNOCKBACK;

		}

	}

	@Override
	public boolean canConnect(ForgeDirection direction)
	{
		return true;
	}

	@Override
	public AxisAlignedBB getRenderBoundingBox()
	{
		return INFINITE_EXTENT_AABB;
	}
}
