package hangcow.greatersecurity.client.gui;

import hangcow.greatersecurity.common.GreaterSecurity;
import hangcow.greatersecurity.common.chest.ContainerLockedChest;
import hangcow.greatersecurity.common.chest.TileEntityLChest;
import hangcow.greatersecurity.common.network.PacketManager;

import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.StringTranslate;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;


public class GuiWChestSettings extends GuiContainer
{
    private TileEntityLChest ChestLC;
    private GuiTextField varType;
    private EntityPlayer player;
    private String linkErrorA = "";
    private IInventory iInventory;
    private IInventory chest;
    int page = 0;

    public GuiWChestSettings(EntityPlayer invPlayer, TileEntityLChest TileEntityLC, IInventory chest)
    {
        super(new ContainerLockedChest(invPlayer.inventory, TileEntityLC, 0));
        this.ChestLC = TileEntityLC;
        this.player = invPlayer;
        this.chest = chest;
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
        this.controlList.add(new GuiButton(0, wid + tWid + 20, hig, 40, 12, var1.translateKey("Add")));
        this.controlList.add(new GuiButton(1, wid + tWid + 20, hig + bHig + 1, 40, 12, var1.translateKey("Remove")));
        this.controlList.add(new GuiButton(2, wid + 105, hig + 50, 40, 12, var1.translateKey("Exit")));
        this.controlList.add(new GuiButton(5, wid + 105, hig + 30, 40, 12, var1.translateKey("Break")));
        this.controlList.add(new GuiButtonArrow(3, wid - 9, hig, true));
        this.controlList.add(new GuiButtonArrow(4, wid + tWid + 1, hig, false));
        // ---------

        this.varType = new GuiTextField(this.fontRenderer, wid, hig, tWid, bHig);
        this.varType.setMaxStringLength(30);
        this.varType.setText("username");
    }

    public void updateScreen()
    {
        this.varType.updateCursorCounter();
    }

    /**
     * Fired when a control is clicked. This is the equivalent of
     * ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        String name = varType.getText();
        if (par1GuiButton.id == 2)// Exit
        {
            player.openGui(GreaterSecurity.instance, 0, ChestLC.worldObj, ChestLC.xCoord, ChestLC.yCoord, ChestLC.zCoord);
        }
        if (par1GuiButton.id == 0)// Add
        {
            if (!ChestLC.users.contains(name))
            {
                ChestLC.addUser(name);
                PacketManager.sendChestPacketServer(ChestLC, 2, name);
            }
        }
        if (par1GuiButton.id == 1)// Remove
        {
            if (ChestLC.users.contains(name))
            {
                ChestLC.removeUser(name);
                PacketManager.sendChestPacketServer(ChestLC, 1, name);
            }
        }
        if (par1GuiButton.id == 4)// next
        {
            if (ChestLC.users.size() > ((int) 5 * page) + 5)
            {
                ++page;
            }
        }
        if (par1GuiButton.id == 3)// Back
        {
            if (page > 0)
            {
                --page;
            }
            else
            {
                page = 0;
            }
        }
        if (par1GuiButton.id == 5)// destroy chest
        {
            player.openGui(GreaterSecurity.instance, 4, ChestLC.worldObj, ChestLC.xCoord, ChestLC.yCoord, ChestLC.zCoord);
        }
        super.actionPerformed(par1GuiButton);
    }

    /**
     * Fired when a key is typed. This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e).
     */
    protected void keyTyped(char par1, int par2)
    {
        // super.keyTyped(par1, par2); //<- leave allow this will cause GUI too
        // close when clicking E
        this.varType.textboxKeyTyped(par1, par2);
    }

    /**
     * Called when the mouse is clicked.
     */
    protected void mouseClicked(int par1, int par2, int par3)
    {
        super.mouseClicked(par1, par2, par3);
        this.varType.mouseClicked(par1, par2, par3);
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat
     * events
     */
    public void onGuiClosed()
    {
        super.onGuiClosed();
        Keyboard.enableRepeatEvents(false);
    }

    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        this.fontRenderer.drawString("Page " + page, 7, 17, 000000);
        DrawPage(0, 0, 10, 40, ChestLC.users, 5);
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the
     * items)
     */
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        int var4 = this.mc.renderEngine.getTexture(GreaterSecurity.GUI_File_PATH+"LCS.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(var4);
        int var5 = (this.width - this.xSize) / 2;
        int var6 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);
        this.varType.drawTextBox();
        // DrawPage(var5,var6,10,40,ChestLC.users,5);
    }

    public void DrawPage(int ScreenX, int ScreenY, int xAg, int yAg, List<String> list, int lines)
    {
        int hight = ScreenY + yAg;
        int width = ScreenX + xAg;
        int spacing = 10;
        int color = 000000;
        String[] display = new String[lines];
        // writes the current page to display array
        try
        {
            for (int u = 0; u < lines; u++)
            {
                int currentLine = ((int) u + ((int) page * lines));
                if (currentLine < list.size())
                {
                    display[u] = list.get(((int) u + lines * page));
                }
                else
                {
                    display[u] = "username" + ((int) u + lines * page);
                }
            }
            // draws display array to screen
            for (int i = 0; i < display.length; i++)
            {
                this.fontRenderer.drawString(display[i], width, spacing * i + hight, color);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
