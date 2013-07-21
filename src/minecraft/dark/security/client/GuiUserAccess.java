package dark.security.client;


import java.awt.Color;
import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import dark.core.DarkMain;
import dark.core.api.ISpecialAccess;
import dark.library.access.AccessLevel;
import dark.library.access.UserAccess;
import dark.library.gui.ContainerFake;
import dark.library.gui.GuiButtonArrow;
import dark.library.machine.terminal.TileEntityTerminal;
import dark.security.common.CommonProxy;
import dark.security.common.GreaterSecurity;

public class GuiUserAccess extends GuiContainer
{
	private TileEntityTerminal tileEntity;
	private ISpecialAccess lock;
	private EntityPlayer player;

	private GuiTextField varType;

	private String texture = DarkMain.GUI_DIRECTORY + "gui_grey.png";

	// private IInventory chest;
	int currentPage = 0;
	int namesPerPage = 5;

	public GuiUserAccess(EntityPlayer player, TileEntityTerminal access, Boolean showBreakButton, String texture)
	{
		this(player, access, showBreakButton);
		this.texture = texture;
	}

	public GuiUserAccess(EntityPlayer player, TileEntityTerminal access, Boolean showBreakButton)
	{
		super(new ContainerFake(access));
		this.tileEntity = access;
		this.player = player;
		this.lock = access;
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
		int buttomWidth = 41;
		int buttonHight = 10;
		int tWid = 85;
		this.buttonList.add(new GuiButton(0, wid + tWid + 20, hig, 40, 12, "Add"));
		this.buttonList.add(new GuiButton(1, wid + tWid + 20, hig + buttonHight + 1, 40, 12, "Remove"));
		this.buttonList.add(new GuiButton(2, wid + 105, hig + 50, 40, 12, "Exit"));
		this.buttonList.add(new GuiButtonArrow(3, wid - 9, hig, true));
		this.buttonList.add(new GuiButtonArrow(4, wid + tWid + 1, hig, false));
		this.buttonList.add(new GuiButton(5, wid + tWid + 20, hig + buttonHight + buttonHight + buttonHight + 1, 40, 12, "Break"));
		// ---------

		this.varType = new GuiTextField(this.fontRenderer, wid, hig, tWid, buttonHight);
		this.varType.setMaxStringLength(30);
		this.varType.setText("username");
	}

	@Override
	public void updateScreen()
	{
		this.varType.updateCursorCounter();
	}

	/** Fired when a control is clicked. This is the equivalent of
	 * ActionListener.actionPerformed(ActionEvent e). */
	@Override
	protected void actionPerformed(GuiButton par1GuiButton)
	{
		String name = varType.getText();
		if (par1GuiButton.id == 0)// Add
		{
			if (!lock.getUsers().contains(name))
			{
				this.tileEntity.sendCommandToServer(this.player, "users add " + name);
				varType.setText("");
			}
		}
		if (par1GuiButton.id == 1)// Remove
		{
			if (lock.getUsers().contains(name))
			{
				this.tileEntity.sendCommandToServer(this.player, "users remove " + name);
				varType.setText("");
			}
		}
		if (par1GuiButton.id == 2)// Exit
		{
			this.mc.thePlayer.closeScreen();
		}
		if (par1GuiButton.id == 4)// next
		{
			this.page(1);
		}
		if (par1GuiButton.id == 3)// Back
		{
			this.page(-1);
		}
		if (par1GuiButton.id == 5)// remove TileEntity
		{
			player.openGui(GreaterSecurity.instance, CommonProxy.YES_NO_GUI, tileEntity.worldObj, tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
		}
		super.actionPerformed(par1GuiButton);
	}

	/** Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e). */
	@Override
	protected void keyTyped(char keycode, int par2)
	{
		if (keycode == Keyboard.KEY_ESCAPE)
		{
			this.mc.thePlayer.closeScreen();
		}
		else if (keycode == Keyboard.KEY_RIGHT)
		{
			this.page(-1);
		}
		else if (keycode == Keyboard.KEY_LEFT)
		{
			this.page(1);
		}
		this.varType.textboxKeyTyped(keycode, par2);
	}

	public void page(int amount)
	{
		this.setPage(this.currentPage + amount);
	}

	public void setPage(int length)
	{
		int dif = this.lock.getUsers().size() % 5;
		int pages = (this.lock.getUsers().size() + (5 - dif) / 5);
		this.currentPage = Math.max(Math.min(length, pages), 0);
	}

	/** Called when the mouse is clicked. */
	@Override
	protected void mouseClicked(int par1, int par2, int par3)
	{
		super.mouseClicked(par1, par2, par3);
		this.varType.mouseClicked(par1, par2, par3);
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
		this.fontRenderer.drawString("Page " + currentPage, 9, 17, 000000);
		DrawPage(0, 0, 10, 40, lock.getUsers(), namesPerPage);
	}

	/** Draw the background layer for the GuiContainer (everything behind the items) */
	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		this.mc.func_110434_K().func_110577_a(new ResourceLocation(this.texture));
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		int var5 = (this.width - this.xSize) / 2;
		int var6 = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);
		this.varType.drawTextBox();
	}

	public void DrawPage(int ScreenX, int ScreenY, int xAg, int yAg, List<UserAccess> list, int linePerPage)
	{
		int hight = ScreenY + yAg;
		int width = ScreenX + xAg;
		int spacing = 10;

		UserAccess[] display = new UserAccess[linePerPage];
		// writes the current page to display array

		for (int line = 0; line < linePerPage; line++)
		{
			int currentLine = ((int) line + ((int) currentPage * linePerPage));
			if (currentLine < list.size() && list.get(currentLine) != null)
			{
				display[line] = list.get(currentLine);
			}
			else
			{
				display[line] = new UserAccess("username" + (currentLine), AccessLevel.NONE, false);
			}
		}
		// draws display array to screen
		for (int i = 0; i < display.length; i++)
		{
			int color = 000000;
			if (display[i].level.ordinal() >= AccessLevel.ADMIN.ordinal())
			{
				color = Color.red.getRGB();
			}
			this.fontRenderer.drawString(display[i].username, width, spacing * i + hight, color);
		}
	}
}
