package hangcow.serversuit.commands;

import hangcow.serversuit.commands.Managers.RankManager;
import hangcow.serversuit.commands.Managers.RankManager.Ranks;
import hangcow.serversuit.events.EventHandlerPlayer;
import hangcow.serversuit.teleportation.TeleportationManager;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import universalelectricity.core.vector.Vector3;

public class HomeCommands extends CommandBase
{

    @Override
    public String getCommandName()
    {
        return "home";
    }

    public String getCommandUsage(ICommandSender par1ICommandSender)
    {
        return "/" + getCommandName() + " [Action]";
    }

    @Override
    public void processCommand(ICommandSender user, String[] var2)
    {
        EntityPlayer player = (EntityPlayer) user;
        if (player instanceof EntityPlayerMP && var2.length > 0 && var2[0].equalsIgnoreCase("set"))
        {
            NBTTagCompound data = EventHandlerPlayer.getPlayerSave(player);
            int dim = player.dimension;

            if (data != null)
            {
                data.setInteger("DimGP", dim);
                if (dim != 0)
                {
                    player.sendChatToPlayer("Can't set home here");
                    return;// returns to prevent setting home in other dims
                }

                data.setDouble("HomeGPX", player.posX);
                data.setDouble("HomeGPY", player.posY);
                data.setDouble("HomeGPZ", player.posZ);
                EventHandlerPlayer.saveToPlayerSave(player, data);
            }
            player.sendChatToPlayer("Set To : " + (int) player.posX + "x " + (int) player.posY + "y " + (int) player.posZ + "z ");
        }
        else if (player instanceof EntityPlayerMP && var2.length == 0)
        {
            NBTTagCompound data = EventHandlerPlayer.getPlayerSave(player);
            int dim = player.dimension;

            if (data != null)
            {
                try
                {
                    int dimD = data.getInteger("DimGP");

                    if (dim != dimD)
                    {
                        player.sendChatToPlayer("Need to be in overworld");
                        return;
                    }

                    double xx = data.getDouble("HomeGPX");
                    double yy = data.getDouble("HomeGPY");
                    double zz = data.getDouble("HomeGPZ");
                    player.sendChatToPlayer("Teleporting To : " + (int) xx + "x " + (int) yy + "y " + (int) zz + "z ");
                    TeleportationManager.addTeleport((EntityPlayerMP) player, new Vector3(xx, yy, zz));
                }

                catch (Exception e)
                {
                    e.printStackTrace();
                    player.sendChatToPlayer("Failed to find home");
                }
            }
        }
    }

    public boolean canCommandSenderUseCommand(ICommandSender var1)
    {
        if (var1 instanceof EntityPlayer)
        {
            RankManager.canUseCommand((EntityPlayer) var1, Ranks.DEFAULT);
        }
        return true;
    }
}