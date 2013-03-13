package hangcow.greatersecurity.common;

import hangcow.greatersecurity.common.chest.TileEntityLockedChest;
import hangcow.greatersecurity.common.door.TileEntityLockedDoor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;
import dark.library.gui.ContainerFake;
import dark.library.locking.ISpecialAccess;
import dark.library.locking.prefab.GuiDestroyResponce;
import dark.library.locking.prefab.GuiUserAccess;

public class CommonProxy implements IGuiHandler
{
	public static final int USERACCESS_GUI = 1;
	public static final int YES_NO_GUI = 2;

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
		Boolean showBreakButton = false;
		if (tileEntity instanceof TileEntityLockedChest && !GreaterSecurity.instance.breakChests)
		{
			showBreakButton = true;
		}
		if (tileEntity instanceof TileEntityLockedDoor && !GreaterSecurity.instance.breakDoors)
		{
			showBreakButton = true;
		}
		if (tileEntity != null)
		{
			switch (ID)
			{
				case USERACCESS_GUI:
					return new GuiUserAccess(tileEntity, player, (ISpecialAccess) tileEntity, showBreakButton);
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
