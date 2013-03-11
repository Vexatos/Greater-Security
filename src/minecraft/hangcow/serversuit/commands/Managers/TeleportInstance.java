package hangcow.serversuit.commands.Managers;

import net.minecraft.entity.player.EntityPlayerMP;
import universalelectricity.core.vector.Vector3;

public class TeleportInstance
{
    public EntityPlayerMP player;
    public int tickCount;
    public Vector3 location;
    public Vector3 destination;

    public TeleportInstance(EntityPlayerMP player, int time, Vector3 location, Vector3 destination)
    {
        this.player = player;
        this.tickCount = time;
        this.location = location;
        this.destination = destination;
    }
}
