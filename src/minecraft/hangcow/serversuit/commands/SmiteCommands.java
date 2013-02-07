package hangcow.serversuit.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;

public class SmiteCommands extends CommandBase 
{
	@Override
	public String getCommandName() 
	{
		return "smite";		
	}
	
	@Override
	public String getCommandUsage(ICommandSender par1ICommandSender) 
	{
		return "/" + getCommandName() +" "+ "[Name]";
	}

	@Override
	public void processCommand(ICommandSender user,String[] Var2) 
	{
		EntityPlayer Sender = ((EntityPlayer)user);
		if (Sender instanceof EntityPlayer)
		{			 
			 if(Var2.length > 0)
			 {
				 EntityPlayer PlayerMp = Sender.worldObj.getPlayerEntityByName(Var2[0]);
				 if(PlayerMp instanceof EntityPlayer)
				 {
					PlayerMp.setEntityHealth(0);
					PlayerMp.sendChatToPlayer("Isn't Death Just So Magical?");
				 }
				 else 
				 {
					Sender.sendChatToPlayer("Player Not Found");
				 }
			 }		
		} 	   				
	}	
}
