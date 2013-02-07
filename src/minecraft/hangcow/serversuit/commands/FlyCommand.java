package hangcow.serversuit.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.packet.Packet202PlayerAbilities;

public class FlyCommand extends CommandBase
{
	public String getCommandName() 
	{
		return "fly";
	}
	
	@Override
	public String getCommandUsage(ICommandSender par1ICommandSender) 
	{
		return "/" + getCommandName();
	}
	
	public void processCommand(ICommandSender user, String[] String) 
	{
		EntityPlayerMP Sender = ((EntityPlayerMP)user);
		if (Sender instanceof EntityPlayer)
		{
			EntityPlayer PlayerMp = Sender.worldObj.getPlayerEntityByName(String[1]);
			PlayerMp.capabilities.allowFlying = (PlayerMp.capabilities.allowFlying ? false : true);
			PlayerMp.sendChatToPlayer("Red Bull : Gives You Wings");			
			((EntityPlayerMP)Sender).playerNetServerHandler.sendPacketToPlayer(new Packet202PlayerAbilities(PlayerMp.capabilities));
		}
	}
}
