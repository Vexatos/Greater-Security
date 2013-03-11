package hangcow.greatersecurity.client.gui;

import hangcow.greatersecurity.common.GreaterSecurity;
import hangcow.greatersecurity.common.chest.ContainerLockedChest;
import hangcow.greatersecurity.common.chest.TileEntityLockedChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

/**
 *
 * @author TheCowGod, Darkguardsman
 *
 */
public class GuiDenyOpen extends GuiContainer

{
	private EntityPlayer invPlayer;
    private TileEntity entityChest;

    //Window height is calculated with this values, the more rows, the heigher
    private int inventoryRows = 0;
    public GuiDenyOpen(EntityPlayer par1IInventory, TileEntity chest)
    {
        super(new ContainerLockedChest(par1IInventory.inventory, (IInventory) chest,0));
        this.invPlayer = par1IInventory;
        this.entityChest = chest;
    }

    //Draws the foreground layer for the GuiContainer
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
    	String ChestName = "";
    	String invName = "";
    	if(entityChest instanceof TileEntityLockedChest)
    	{
    		TileEntityLockedChest eChest = (TileEntityLockedChest) entityChest;
	    	ChestName = eChest.BlockOwner+"'s ";
	    	invName = "";
	    	if(eChest.BlockOwner == "World" ||eChest.BlockOwner == null)
	    	{
	    		ChestName = "";
	    	}
	    	if(this.invPlayer.username != null)
	    	{
	    		invName = this.invPlayer.username;
	    	}
	    	 this.fontRenderer.drawString(ChestName+" Chest", 38, 6, 16777200);
    	}
    	if(entityChest instanceof TileEntityLockedChest)
    	{
    		TileEntityLockedChest eChest = (TileEntityLockedChest) entityChest;
	    	ChestName = eChest.BlockOwner+"'s ";
	    	invName = "";
	    	if(eChest.BlockOwner == "World" ||eChest.BlockOwner == null)
	    	{
	    		ChestName = "";
	    	}
	    	if(this.invPlayer.username != null)
	    	{
	    		invName = this.invPlayer.username;
	    	}
	    	 this.fontRenderer.drawString(ChestName+" Chest", 38, 6, 16777200);
    	}
    	 // Not Need with current Gui
         //this.fontRenderer.drawString("Chest Is Locked", 28, this.ySize - 96 + 2, 4210752);
    }

    //Draws the background layer for the GuiContainer
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        int var4 = this.mc.renderEngine.getTexture(GreaterSecurity.GUI_File_PATH+"AccessDenied.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(var4);
        int containerWidth = (this.width - this.xSize) / 2;
        int containerHeight = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(containerWidth, containerHeight, 0, 0, this.xSize, this.ySize);
    }
}
