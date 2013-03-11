package hangcow.serversuit.commands;

import hangcow.serversuit.events.EnumDeathTypes;
import hangcow.serversuit.events.EventHandlerPlayer;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;

public class GetDeathsCommands extends CommandBase
{
    @Override
    public String getCommandName()
    {
        return "deaths";
    }

    @Override
    public void processCommand(ICommandSender user, String[] String)
    {
        EntityPlayer player = (EntityPlayer) user;
        if (player == null) { return; }
        if (String.length > 1)
        {
            if (EnumDeathTypes.getDeathByName(String[0]) != EnumDeathTypes.UNKOWN && player.worldObj.getPlayerEntityByName(String[1]) instanceof EntityPlayer)
            {
                EnumDeathTypes death = EnumDeathTypes.getDeathByName(String[0]);
                player.sendChatToPlayer("Player " + String[1] + " has died " + EventHandlerPlayer.getDeaths(player.worldObj.getPlayerEntityByName(String[1]), death) + "by: " + death.name + " ");
            }
        }
        else if (String.length == 1)
        {
            EntityPlayer nPlayer = player.worldObj.getPlayerEntityByName(String[0]);
            if (nPlayer instanceof EntityPlayer)
            {

                player.sendChatToPlayer("Player " + String[0] + "has died a total of " + this.getTotalDeaths(player.worldObj.getPlayerEntityByName(String[0])) + " times");
            }
            else if (EnumDeathTypes.getDeathByName(String[0]) != EnumDeathTypes.UNKOWN)
            {
                EnumDeathTypes death = EnumDeathTypes.getDeathByName(String[0]);
                player.sendChatToPlayer("Your deaths by: " + death.name + " " + EventHandlerPlayer.getDeaths(player, death));
            }
            else if (String[0].equalsIgnoreCase("list"))
            {
                for(int i = 0; i < EnumDeathTypes.values().length;i++)
                {
                    player.sendChatToPlayer("[DeathType]["+i+"] = "+ EnumDeathTypes.values()[i].name);
                }
            }
            else
            {
                player.sendChatToPlayer(this.getCommandUsage(user));
            }

        }
        else
        {
            player.sendChatToPlayer("You have died a total of " + this.getTotalDeaths(player) + " times");
        }
    }

    /**
     * gets the players total deaths of all types
     * 
     * @param player
     * @return
     */
    public int getTotalDeaths(EntityPlayer player)
    {
        int deaths = 0;
        for (int i = 0; i < EnumDeathTypes.values().length; i++)
        {
            deaths += EventHandlerPlayer.getDeaths(player, EnumDeathTypes.values()[i]);
        }
        return deaths;
    }

    public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender)
    {
        return true;
    }

    public String getCommandUsage(ICommandSender par1ICommandSender)
    {
        return "/" + this.getCommandName() + " Type " + "Username";
    }
}
