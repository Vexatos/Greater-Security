package hangcow.greatersecurity.client.gui;

import hangcow.greatersecurity.common.CommonProxy;
import hangcow.greatersecurity.common.GreaterSecurity;
import hangcow.greatersecurity.common.chest.ContainerLockedChest;
import hangcow.greatersecurity.common.chest.TileEntityLockedChest;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.src.ModLoader;
import net.minecraft.util.StringTranslate;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import dark.library.gui.GuiButtonArrow;

/**
 * 
 * @author TheCowGod, Darkguardsman
 * 
 */
public class GuiLockedChest extends GuiContainer

{
	private EntityPlayer player;
	private TileEntityLockedChest lockedChest;
	private IInventory chest;
	// Window height is calculated with this values, the more rows, the heigher
	private int inventoryRows = 0;

	public GuiLockedChest(EntityPlayer par1IInventory, TileEntityLockedChest chest, IInventory object)
	{
		super(new ContainerLockedChest(par1IInventory.inventory, object, 1));
		this.player = par1IInventory;
		this.lockedChest = chest;
		this.chest = object;
		short var3 = 222;
		int var4 = var3 - 108;
		this.inventoryRows = object.getSizeInventory() / 9;
		this.ySize = var4 + this.inventoryRows * 18;
	}

	public void initGui()
	{
		super.initGui();
		StringTranslate var1 = StringTranslate.getInstance();
		Keyboard.enableRepeatEvents(true);
		this.controlList.clear();
		int wid = (this.width - this.xSize) / 2 + 8;
		int hig = (this.height - this.ySize) / 2 + 15;
		int bWid = 41;
		int bHig = 9;
		this.controlList.add(new GuiButtonArrow(0, wid + 150, hig - 8, false));
	}

	protected void actionPerformed(GuiButton par1GuiButton)
	{
		super.actionPerformed(par1GuiButton);
		if (par1GuiButton.id == 0)
		{
			//TODO player.openGui(GreaterSecurity.instance, CommonProxy.USERACCESS_GUI, lockedChest.worldObj, lockedChest.xCoord, lockedChest.yCoord, lockedChest.zCoord);
		}
	}

	public void onGuiClosed()
	{
		super.onGuiClosed();
		Keyboard.enableRepeatEvents(false);
	}

	// Draws the foreground layer for the GuiContainer
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		String ChestName = lockedChest.BlockOwner + "'s ";
		String invName = "";
		if (this.lockedChest.BlockOwner == "World" || this.lockedChest.BlockOwner == null)
		{
			ChestName = "";
		}
		if (this.player.username != null)
		{
			invName = this.player.username + "'s ";
		}
		this.fontRenderer.drawString(ChestName + "Chest", 8, 6, 16777200);
		this.fontRenderer.drawString(invName + "Inventory", 8, this.ySize - 96 + 3, 16777200);
	}

	// Draws the background layer for the GuiContainer
	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		int var4 = this.mc.renderEngine.getTexture(GreaterSecurity.GUI_File_PATH + "LockedWoodenChest.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(var4);
		int var5 = (this.width - this.xSize) / 2;
		int var6 = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.inventoryRows * 18 + 17);
		this.drawTexturedModalRect(var5, var6 + this.inventoryRows * 18 + 17, 0, 126, this.xSize, 96);
	}
}