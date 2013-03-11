package hangcow.greatersecurity.common;

import hangcow.greatersecurity.client.gui.GuiDenyOpen;
import hangcow.greatersecurity.client.gui.GuiLChest;
import hangcow.greatersecurity.client.gui.GuiRemoveChest;
import hangcow.greatersecurity.client.gui.GuiWChestSettings;
import hangcow.greatersecurity.client.gui.GuiWDoorSettings;
import hangcow.greatersecurity.common.chest.ContainerLockedChest;
import hangcow.greatersecurity.common.chest.TileEntityLockedChest;
import hangcow.greatersecurity.common.door.ContainerFake;
import hangcow.greatersecurity.common.door.TileEntityLockedDoor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class CommonProxy implements IGuiHandler
{

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
                case 0:
                    return new ContainerLockedChest(player.inventory, (IInventory) GetCombinedInv(world, i, j, k), 1);
                case 1:
                    return new ContainerLockedChest(player.inventory, (IInventory) GetCombinedInv(world, i, j, k), 0);
                default:
                    return new ContainerFake(tileEntity);
            }
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world,
            int i, int j, int k)
    {
        TileEntity tileEntity = world.getBlockTileEntity(i, j, k);

        if (tileEntity != null)
        {
            switch (ID)
            {
                case 0:
                    return new GuiLChest(player, ((TileEntityLockedChest) tileEntity), (IInventory) GetCombinedInv(world, i, j, k));
                case 1:
                    return new GuiDenyOpen(player, ((TileEntityLockedChest) tileEntity));
                case 2:
                    return new GuiWChestSettings(player, ((TileEntityLockedChest) tileEntity), (IInventory) GetCombinedInv(world, i, j, k));
                case 3:
                    return new GuiWDoorSettings(player, (TileEntityLockedDoor) tileEntity);
                case 4:
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
