package dark.library.locking.prefab;

import hangcow.greatersecurity.common.CommonProxy;
import hangcow.greatersecurity.common.GreaterSecurity;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StringTranslate;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import dark.library.gui.ContainerFake;

public class GuiDestroyResponce extends GuiContainer
{
	private TileEntity tileEntity;
	private EntityPlayer player;
	private IInventory iInventory;
	private IInventory chest;
	int page = 0;

	public GuiDestroyResponce(EntityPlayer invPlayer, TileEntity tile)
	{
		super(new ContainerFake(tile));
		this.tileEntity = tile;
		this.player = invPlayer;
	}

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
		this.controlList.add(new GuiButton(0, wid + tWid + 20, hig, 40, 12, var1.translateKey("Yes")));
		this.controlList.add(new GuiButton(1, wid + tWid + 20, hig + bHig + 1, 40, 12, var1.translateKey("No")));
	}

	public void updateScreen()
	{
	}

	/**
	 * Fired when a control is clicked. This is the equivalent of
	 * ActionListener.actionPerformed(ActionEvent e).
	 */
	protected void actionPerformed(GuiButton par1GuiButton)
	{
		if (par1GuiButton.id == 0)// boom
		{
			int id = tileEntity.worldObj.getBlockId(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
			int meta = tileEntity.worldObj.getBlockMetadata(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
			Block block = Block.blocksList[id];
			if(block != null)
			{
				block.dropBlockAsItem(tileEntity.worldObj, tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord, meta, 0);
				block.breakBlock(tileEntity.worldObj, tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord, meta, 0);
			}
			tileEntity.worldObj.setBlock(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord, 0);			
			this.mc.thePlayer.closeScreen();
		}
		if (par1GuiButton.id == 1)// Exit
		{
			player.openGui(GreaterSecurity.instance, CommonProxy.USERACCESS_GUI, tileEntity.worldObj, tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);

		}

		super.actionPerformed(par1GuiButton);
	}

	/**
	 * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
	 */
	protected void keyTyped(char par1, int par2)
	{
	}

	/**
	 * Called when the mouse is clicked.
	 */
	protected void mouseClicked(int par1, int par2, int par3)
	{
		super.mouseClicked(par1, par2, par3);
	}

	/**
	 * Called when the screen is unloaded. Used to disable keyboard repeat events
	 */
	public void onGuiClosed()
	{
		super.onGuiClosed();
		Keyboard.enableRepeatEvents(false);
	}

	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		this.fontRenderer.drawString("Are you Sure?", 7, 17, 000000);
	}

	/**
	 * Draw the background layer for the GuiContainer (everything behind the items)
	 */
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		int var4 = this.mc.renderEngine.getTexture("/dark/library/resources/textures/gui/GuiGrey.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(var4);
		int var5 = (this.width - this.xSize) / 2;
		int var6 = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);
	}
}
