package dark.security.common.fence;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.CustomDamageSource;
import dark.api.INetworkPart;
import dark.api.PowerSystems;
import dark.core.tile.network.NetworkSharedPower;
import dark.core.tile.network.NetworkTileEntities;
import dark.prefab.machine.TileEntityMachine;
import dark.security.common.CommonProxy;

public class TileEntityElectroFence extends TileEntityMachine implements INetworkPart
{

	private static final float WATT_PER_SHOCK = 30;
	private static final float WATT_PER_TICK = 1;
	public List<TileEntity> connections = new ArrayList<TileEntity>();
	private NetworkSharedPower powerNetwork;
	/** How many blocks should the player be knocked-back when shocked? */
	private static final int KNOCKBACK = 3;

	@Override
	public void updateEntity()
	{
		super.updateEntity();
		if (!PowerSystems.runPowerLess(PowerSystems.UE_SUPPORTED_SYSTEMS) && !this.runWithOutPower && this.canRun())
		{
			((NetworkSharedPower) this.getTileNetwork()).drainPower(this, WATT_PER_TICK, true);
		}
	}

	/** Called by the block on collision to apply shock damage if it has power */
	public void shockEntity(Entity entity)
	{
		if (entity != null && this.canRun() && (PowerSystems.runPowerLess(PowerSystems.UE_SUPPORTED_SYSTEMS) || this.runWithOutPower || ((NetworkSharedPower) this.getTileNetwork()).drainPower(this, WATT_PER_SHOCK, false)))
		{
			((NetworkSharedPower) this.getTileNetwork()).drainPower(this, WATT_PER_SHOCK, true);

			/* DAMAGE PER TICK OF COLLISION (20ticks a sec) */
			entity.attackEntityFrom(CustomDamageSource.electrocution.setDeathMessage("%1$s tried to climb an electric fence!"), 1);

			CommonProxy.entityPush(entity, KNOCKBACK, false);
			entity.motionY += KNOCKBACK;

		}
	}

	@Override
	public boolean canRun()
	{
		return PowerSystems.runPowerLess(PowerSystems.UE_SUPPORTED_SYSTEMS) || this.runWithOutPower || ((NetworkSharedPower) this.getTileNetwork()).drainPower(this, WATT_PER_TICK, false);
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

	@Override
	public boolean canTileConnect(TileEntity entity, ForgeDirection dir)
	{
		return entity != null && entity.getClass().equals(TileEntityElectroFence.class);
	}

	@Override
	public List<TileEntity> getNetworkConnections()
	{
		return this.connections;
	}

	@Override
	public void refresh()
	{
		this.connections.clear();
		for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
		{
			TileEntity ent = new Vector3(this).modifyPositionFromSide(direction).getTileEntity(this.worldObj);
			if (ent != null && ent.getClass().equals(TileEntityElectroFence.class))
			{
				this.connections.add(ent);
			}
		}

	}

	@Override
	public NetworkTileEntities getTileNetwork()
	{
		if (this.powerNetwork == null)
		{
			this.powerNetwork = new NetworkSharedPower(this);
		}
		return this.powerNetwork;
	}

	@Override
	public void setTileNetwork(NetworkTileEntities net)
	{
		if (net instanceof NetworkSharedPower)
		{
			this.powerNetwork = (NetworkSharedPower) net;
		}

	}
}
