package hangcow.serversuit.commands.Managers;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;

public class ActionInstance 
{
	public enum action
	{
		OPENED("Opened"),LCLICKED("LClicked"),RCLICKED("RClicked"),DESTROYED("Destroyed"),PLACED("Placed");
		public String name;
		private action(String name)
		{
			this.name = name;
		}
	}
	public Loc loc;
	public Block block;
	public action act;
	public String time;
	public EntityPlayer player;
	public ActionInstance(Loc loc,Block block, action act, String time, EntityPlayer player)
	{
		this.loc = loc;
		this.block = block;
		this.act = act;
		this.time = time;
		this.player = player;
	}
	
	
}
