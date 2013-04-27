package hangcow.greatersecurity.common;

import hangcow.greatersecurity.client.GuiDestroyResponce;
import hangcow.greatersecurity.client.GuiUserAccess;
import hangcow.greatersecurity.common.chest.TileEntityLockedChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;
import dark.library.gui.ContainerFake;
import dark.library.gui.GuiGlobalList;
import dark.library.terminal.TileEntityTerminal;

public class CommonProxy implements IGuiHandler
{
	public static final int USERACCESS_GUI = 1;
	public static final int USERGLOBAL_GUI = 2;
	public static final int YES_NO_GUI = 3;

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int i, int j, int k)
	{
		TileEntity tileEntity = world.getBlockTileEntity(i, j, k);

		if (tileEntity != null)
		{
			switch (ID)
			{
				default:
					return new ContainerFake(tileEntity);
			}
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int i, int j, int k)
	{
		TileEntity tileEntity = world.getBlockTileEntity(i, j, k);
		if(ID == USERGLOBAL_GUI)
		{
			player.sendChatToPlayer("Opening GUI");
			return new GuiGlobalList(player);
		}
		if (tileEntity != null)
		{
			switch (ID)
			{
				case USERACCESS_GUI:
					return new GuiUserAccess(player, (TileEntityTerminal)tileEntity, true);				
				case YES_NO_GUI:
					return new GuiDestroyResponce(player, (TileEntityLockedChest) tileEntity);
			}
		}
		return null;
	}

	public void preInit()
	{

	}

	public void postInit()
	{

	}

	public void init()
	{

	}
}
