package dark.library.locking.prefab;


import java.awt.Color;
import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StringTranslate;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import dark.library.gui.ContainerFake;
import dark.library.gui.GuiButtonArrow;
import dark.library.locking.AccessLevel;
import dark.library.locking.ISpecialAccess;
import dark.library.locking.UserAccess;

public class GuiUserAccess extends GuiContainer
{
	private TileEntity tileEntity;
	private ISpecialAccess lock;
	private GuiTextField varType;
	private EntityPlayer player;
	private String texture = "/dark/library/resources/textures/gui/userAccessGui.png";

	// private IInventory chest;
	int currentPage = 0;
	int namesPerPage = 5;

	public GuiUserAccess(TileEntity tileEntity, EntityPlayer player, ISpecialAccess access, String texture)
	{
		super(new ContainerFake(tileEntity));
		this.tileEntity = tileEntity;
		this.player = player;
		this.lock = access;
		this.texture = texture;
	}

	@Override
	public void initGui()
	{
		super.initGui();
		StringTranslate var1 = StringTranslate.getInstance();
		Keyboard.enableRepeatEvents(true);
		this.controlList.clear();
		int wid = (this.width - this.xSize) / 2 + 13;
		int hig = (this.height - this.ySize) / 2 + 25;
		int bWid = 41;
		int bHig = 10;
		int tWid = 85;
		this.controlList.add(new GuiButton(0, wid + tWid + 20, hig, 40, 12, var1.translateKey("Add")));
		this.controlList.add(new GuiButton(1, wid + tWid + 20, hig + bHig + 1, 40, 12, var1.translateKey("Remove")));
		this.controlList.add(new GuiButton(2, wid + 105, hig + 50, 40, 12, var1.translateKey("Exit")));
		this.controlList.add(new GuiButtonArrow(3, wid - 9, hig, true));
		this.controlList.add(new GuiButtonArrow(4, wid + tWid + 1, hig, false));
		// ---------

		this.varType = new GuiTextField(this.fontRenderer, wid, hig, tWid, bHig);
		this.varType.setMaxStringLength(30);
		this.varType.setText("username");
	}

	@Override
	public void updateScreen()
	{
		this.varType.updateCursorCounter();
	}

	/**
	 * Fired when a control is clicked. This is the equivalent of
	 * ActionListener.actionPerformed(ActionEvent e).
	 */
	@Override
	protected void actionPerformed(GuiButton par1GuiButton)
	{
		String name = varType.getText();
		if (par1GuiButton.id == 0)// Add
		{
			if (!lock.getUsers().contains(name))
			{
				lock.addUserAccess(name, AccessLevel.USER, true);
			}
		}
		if (par1GuiButton.id == 1)// Remove
		{
			if (lock.getUsers().contains(name))
			{
				lock.removeUserAccess(name);
			}
		}
		if (par1GuiButton.id == 4)// next
		{
			this.page(1);
		}
		if (par1GuiButton.id == 3)// Back
		{
			this.page(-1);
		}
		super.actionPerformed(par1GuiButton);
	}

	/**
	 * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
	 */
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

	/**
	 * Called when the mouse is clicked.
	 */
	@Override
	protected void mouseClicked(int par1, int par2, int par3)
	{
		super.mouseClicked(par1, par2, par3);
		this.varType.mouseClicked(par1, par2, par3);
	}

	/**
	 * Called when the screen is unloaded. Used to disable keyboard repeat events
	 */
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

	/**
	 * Draw the background layer for the GuiContainer (everything behind the items)
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		int var4 = this.mc.renderEngine.getTexture(this.texture);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(var4);
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
