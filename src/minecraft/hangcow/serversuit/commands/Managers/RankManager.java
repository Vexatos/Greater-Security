package hangcow.serversuit.commands.Managers;

import hangcow.serversuit.events.EventHandlerPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class RankManager
{

	public enum Ranks
	{
		DEFAULT, USER, DONER, DEV, MOD, ADMIN, OP;
	}

	/**
	 * sets the players ranks
	 * 
	 * @param rank
	 * @param player
	 */
	public static void setRank(Ranks rank, EntityPlayer player)
	{
		NBTTagCompound data = EventHandlerPlayer.getPlayerSave(player);
		if (data != null)
		{
			data.setInteger("GCRank", rank.ordinal());
			EventHandlerPlayer.saveToPlayerSave(player, data);
		}
	}

	/**
	 * gets the players ranks or sets it if none
	 * 
	 * @param player
	 * @return
	 */
	public static Ranks getRank(EntityPlayer player)
	{
		NBTTagCompound data = EventHandlerPlayer.getPlayerSave(player);
		if (data != null)
		{
			int rankID = data.getInteger("GCRank");
			if (rankID < Ranks.values().length)
			{
				return Ranks.values()[rankID];
			}
		}
		setRank(Ranks.DEFAULT, player);

		return Ranks.DEFAULT;

	}

	public static boolean canUseCommand(EntityPlayer player, Ranks rank)
	{
		if (getRank(player).ordinal() >= rank.ordinal())
		{
			return true;
		}
		return false;
	}
}
