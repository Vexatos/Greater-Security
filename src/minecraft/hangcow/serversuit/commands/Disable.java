package hangcow.serversuit.commands;

import hangcow.serversuit.commands.Managers.RankManager;
import hangcow.serversuit.commands.Managers.RankManager.Ranks;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.src.ModLoader;

public class Disable extends CommandBase
{
	@Override
	public String getCommandName()
	{
		return "disable";
	}

	@Override
	public String getCommandUsage(ICommandSender par1ICommandSender)
	{
		return "/" + getCommandName() + " " + "[Name]";
	}

	@Override
	public void processCommand(ICommandSender user, String[] args)
	{
		if (args.length > 1)
		{
			Boolean disable = false;
			if (args[1].equalsIgnoreCase("false"))
			{
				disable = true;
			}
			if (args[0].equalsIgnoreCase("fire"))
			{
				ModLoader.getMinecraftServerInstance().worldServers[0].getGameRules().setOrCreateGameRule("doFireTick", disable.toString());
				user.sendChatToPlayer(!disable ? "Fire Off" : "Fire On");
			}
			else if (args[0].equalsIgnoreCase("creepers"))
			{
				ModLoader.getMinecraftServerInstance().worldServers[0].getGameRules().setOrCreateGameRule("mobGriefing", disable.toString());
				user.sendChatToPlayer(!disable ? "turning off mob griefing" : "Creepers got to creep");
			}
			else if (args[0].equalsIgnoreCase("keepInv"))
			{
				ModLoader.getMinecraftServerInstance().worldServers[0].getGameRules().setOrCreateGameRule("keepInventory", disable.toString());
				user.sendChatToPlayer(!disable ? "Keep your Inv" : "You die you lose");
			}
		}
		else
		{
			user.sendChatToPlayer("disable [what] true/false");
		}
	}

	public boolean canCommandSenderUseCommand(ICommandSender var1)
	{
		if (var1.getCommandSenderName().equalsIgnoreCase("server"))
		{
			return true;
		}
		else if (var1 instanceof EntityPlayer)
		{
			RankManager.canUseCommand((EntityPlayer) var1, Ranks.MOD);
		}
		return true;
	}
}
