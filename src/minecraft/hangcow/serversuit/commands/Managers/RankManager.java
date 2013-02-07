package hangcow.serversuit.commands.Managers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class RankManager 
{

	public enum Ranks
	{
		DEFAULT,USER,DONER,DEV,MOD,ADMIN,OP;
	}
	/**
	 * sets the players ranks 
	 * @param rank
	 * @param player
	 */
	public static void setRank(Ranks rank, EntityPlayer player)
	{
		NBTTagCompound data = player.getEntityData();
		if (data != null) 
		{
			data.setInteger("GCRank", rank.ordinal());
		}
	}
	/**
	 * gets the players ranks or sets it if none
	 * @param player
	 * @return
	 */
	public static Ranks getRank(EntityPlayer player)
	{
		NBTTagCompound data = player.getEntityData();
		if (data != null) 
		{
			int rankID = data.getInteger("GCRank");
			if(rankID < Ranks.values().length)
			{
				return Ranks.values()[rankID];
			}
		}
			data.setInteger("GCRank", Ranks.DEFAULT.ordinal());
			return Ranks.DEFAULT;
		
	}
	public static boolean canUseCommand(EntityPlayer player, Ranks rank)
	{
		if(getRank(player).ordinal() >= rank.ordinal())
		{
			return true;
		}
		return false;
	}
}
