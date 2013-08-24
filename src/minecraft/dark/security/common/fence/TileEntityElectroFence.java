package dark.security.common.fence;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.CustomDamageSource;
import dark.api.INetworkEnergyPart;
import dark.api.PowerSystems;
import dark.core.blocks.TileEntityMachine;
import dark.core.tile.network.NetworkSharedPower;
import dark.core.tile.network.NetworkTileEntities;
import dark.security.common.CommonProxy;
import dark.security.common.GreaterSecurity;

public class TileEntityElectroFence extends TileEntityMachine implements INetworkEnergyPart
{
    public static final float WATT_PER_SHOCK = 30;

    protected List<TileEntity> connections = new ArrayList<TileEntity>();

    protected NetworkSharedPower powerNetwork;
    /** How many blocks should the player be knocked-back when shocked? */
    private static final int KNOCKBACK = 3;

    public TileEntityElectroFence()
    {
        super(1, (WATT_PER_SHOCK * 10) + 20);
    }

    /** Called by the block on collision to apply shock damage if it has power */
    public void shockEntity(Entity entity)
    {
        if (entity != null && this.canRun() && this.consumePower(WATT_PER_SHOCK, true))
        {
            /* DAMAGE PER TICK OF COLLISION (20ticks a sec) */
            entity.attackEntityFrom(CustomDamageSource.electrocution.setDeathMessage("%1$s tried to climb an electric fence!"), 1);

            //TODO change to angle of entity compared to fence
            CommonProxy.entityPush(entity, KNOCKBACK, false);
            entity.motionY += KNOCKBACK;

        }
    }

    @Override
    public boolean consumePower(float watts, boolean doDrain)
    {
        return ((NetworkSharedPower) this.getTileNetwork()).drainPower(this, watts, doDrain);
    }

    @Override
    public float getRequest(ForgeDirection direction)
    {
        return this.WATTS_PER_TICK;
    }

    @Override
    public void togglePowerMode()
    {
        ((NetworkSharedPower) this.getTileNetwork()).setPowerLess(this.runPowerLess());
    }

    @Override
    public float getEnergyStored()
    {
        return ((NetworkSharedPower) this.getTileNetwork()).getEnergyStored();
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox()
    {
        return INFINITE_EXTENT_AABB;
    }

    @Override
    public boolean canConnect(ForgeDirection direction)
    {
        return true;
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
        if (!(this.powerNetwork instanceof NetworkSharedPower))
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

    @Override
    public boolean mergeDamage(String result)
    {
        return false;
    }

    @Override
    public float getPartEnergy()
    {
        return this.energyStored;
    }

    @Override
    public float getPartMaxEnergy()
    {
        return this.MAX_WATTS;
    }

    @Override
    public void setPartEnergy(float energy)
    {
        this.energyStored = energy;
    }

    @Override
    public String getChannel()
    {
        return GreaterSecurity.CHANNEL;
    }
}
