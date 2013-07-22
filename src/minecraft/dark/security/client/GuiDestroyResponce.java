package dark.security.client;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import dark.core.DarkMain;
import dark.library.gui.ContainerFake;
import dark.library.machine.terminal.TileEntityTerminal;
import dark.security.GreaterSecurity;
import dark.security.common.CommonProxy;

public class GuiDestroyResponce extends GuiContainer
{
	private TileEntity tileEntity;
	private TileEntityTerminal term;
	private EntityPlayer player;
	private IInventory iInventory;
	private IInventory chest;
	int page = 0;

	public GuiDestroyResponce(EntityPlayer invPlayer, TileEntityTerminal tile)
	{
		super(new ContainerFake((TileEntity) tile));
		this.tileEntity = (TileEntity) tile;
		this.term = tile;
		this.player = invPlayer;
	}

	@Override
	public void initGui()
	{
		super.initGui();
		//StringTranslate var1 = StringTranslate.getInstance();
		Keyboard.enableRepeatEvents(true);
		this.buttonList.clear();
		int wid = (this.width - this.xSize) / 2 + 13;
		int hig = (this.height - this.ySize) / 2 + 25;
		int bWid = 41;
		int bHig = 10;
		int tWid = 85;
		this.buttonList.add(new GuiButton(0, wid + tWid + 20, hig, 40, 12, "Yes"));
		this.buttonList.add(new GuiButton(1, wid + tWid + 20, hig + bHig + 1, 40, 12, "No"));
	}

	@Override
	public void updateScreen()
	{
	}

	/** Fired when a control is clicked. This is the equivalent of
	 * ActionListener.actionPerformed(ActionEvent e). */
	@Override
	protected void actionPerformed(GuiButton par1GuiButton)
	{
		if (par1GuiButton.id == 0)// boom
		{
			this.term.sendCommandToServer(this.player, "break");
			this.mc.thePlayer.closeScreen();
		}
		if (par1GuiButton.id == 1)// Exit
		{
			player.openGui(GreaterSecurity.instance, CommonProxy.USERACCESS_GUI, tileEntity.worldObj, tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);

		}

		super.actionPerformed(par1GuiButton);
	}

	/** Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e). */
	@Override
	protected void keyTyped(char par1, int par2)
	{
	}

	/** Called when the mouse is clicked. */
	@Override
	protected void mouseClicked(int par1, int par2, int par3)
	{
		super.mouseClicked(par1, par2, par3);
	}

	/** Called when the screen is unloaded. Used to disable keyboard repeat events */
	@Override
	public void onGuiClosed()
	{
		super.onGuiClosed();
		Keyboard.enableRepeatEvents(false);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		this.fontRenderer.drawString("Are you Sure?", 7, 17, 000000);
	}

	/** Draw the background layer for the GuiContainer (everything behind the items) */
	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		this.mc.func_110434_K().func_110577_a(new ResourceLocation(DarkMain.GUI_DIRECTORY + "GuiGrey.png"));
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		int var5 = (this.width - this.xSize) / 2;
		int var6 = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);
	}
}
