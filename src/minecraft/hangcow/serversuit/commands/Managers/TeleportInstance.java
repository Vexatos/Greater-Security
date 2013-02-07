package hangcow.serversuit.commands.Managers;

import net.minecraft.entity.player.EntityPlayerMP;

public class TeleportInstance 
{
	public EntityPlayerMP player;
	public int Time;
	public Loc pLoc;
	public Loc teleport;
	public TeleportInstance(EntityPlayerMP player, int Time, Loc prevLoc,Loc teleport)
	{
		this.player = player;
		this.Time = Time;
		this.pLoc = prevLoc;
		this.teleport = teleport;
	}
}
