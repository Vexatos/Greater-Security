package hangcow.greatersecurity.common;

import hangcow.greatersecurity.client.gui.GuiLockedChest;
import hangcow.greatersecurity.client.gui.GuiRemoveChest;
import hangcow.greatersecurity.common.chest.ContainerLockedChest;
import hangcow.greatersecurity.common.chest.TileEntityLockedChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;
import dark.library.gui.ContainerFake;
import dark.library.locking.ISpecialAccess;
import dark.library.locking.prefab.GuiUserAccess;

public class CommonProxy implements IGuiHandler
{
	public static final int CHEST_GUI = 0;
	public static final int USERACCESS_GUI = 1;
	public static final int YES_NO_GUI = 2;

	public Object GetCombinedInv(World par1World, int i, int j, int k)
	{
		IInventory var10 = (TileEntityLockedChest) par1World.getBlockTileEntity(i, j, k);
		for (int bb = 0; bb < 4; bb++)
		{
			int deltaI = 0;
			int deltaK = 0;
			switch (bb)
			{
				case 0:
					deltaI--;
					break;
				case 1:
					deltaI++;
					break;
				case 2:
					deltaK--;
					break;
				case 3:
					deltaK++;
					break;
			}
			if (par1World.getBlockTileEntity(i + deltaI, j, k + deltaK) instanceof TileEntityLockedChest)
			{
				TileEntityLockedChest cc = (TileEntityLockedChest) par1World.getBlockTileEntity(i, j, k);
				// convert both seperate inv into open joined inv
				var10 = new InventoryLargeChest("container.chestLDouble", (TileEntityLockedChest) par1World.getBlockTileEntity(i + deltaI, j, k + deltaK), cc);
			}
		}
		return var10;
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int i, int j, int k)
	{
		TileEntity tileEntity = world.getBlockTileEntity(i, j, k);

		if (tileEntity != null)
		{
			switch (ID)
			{
				case CHEST_GUI:
					return new ContainerLockedChest(player.inventory, (IInventory) GetCombinedInv(world, i, j, k), 1);
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

		if (tileEntity != null)
		{
			switch (ID)
			{
				case CHEST_GUI:
					return new GuiLockedChest(player, ((TileEntityLockedChest) tileEntity), (IInventory) GetCombinedInv(world, i, j, k));
				case USERACCESS_GUI:
					return new GuiUserAccess((TileEntity) tileEntity, player, (ISpecialAccess) tileEntity);
				case YES_NO_GUI:
					return new GuiRemoveChest(player, (TileEntityLockedChest) tileEntity);
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
