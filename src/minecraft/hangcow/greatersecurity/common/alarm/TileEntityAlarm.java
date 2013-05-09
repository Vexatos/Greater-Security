package hangcow.greatersecurity.common.alarm;

import hangcow.greatersecurity.common.GreaterSecurity;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.vector.Vector3;
import dark.library.access.AccessLevel;
import dark.library.access.UserAccess;
import dark.library.terminal.TileEntityTerminal;

public class TileEntityAlarm extends TileEntityTerminal
{
	public ForgeDirection getFacing()
	{
		return null;

	}

	@Override
	public boolean canConnect(ForgeDirection direction)
	{
		return direction == getFacing().getOpposite();
	}

	public void triggerAlarmRemotes(AlarmEvents event, String msg)
	{
		List<UserAccess> users = this.getUsersWithAcess(AccessLevel.ADMIN);
		users.addAll(this.getUsersWithAcess(AccessLevel.OWNER));
		for (UserAccess user : users)
		{
			EntityPlayer player = worldObj.getPlayerEntityByName(user.username);
			if (player != null && player.worldObj == this.worldObj)
			{
				InventoryPlayer inv = player.inventory;
				if (inv != null)
				{
					for (int j = 0; j < 9; ++j)
					{
						if (inv.mainInventory[j] != null && Item.itemsList[inv.mainInventory[j].itemID] instanceof IAlarmItem && ((IAlarmItem) Item.itemsList[inv.mainInventory[j].itemID]).canGetEvent(this.worldObj, new Vector3(this), this, event))
						{
							player.sendChatToPlayer(msg);
							break;
						}
					}
				}
			}
		}
	}

	@Override
	public String getChannel()
	{
		return GreaterSecurity.CHANNEL;
	}
}
