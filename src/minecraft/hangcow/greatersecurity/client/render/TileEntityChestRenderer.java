package hangcow.greatersecurity.client.render;

import hangcow.greatersecurity.common.GreaterSecurity;
import hangcow.greatersecurity.common.chest.BlockLockedChest;
import hangcow.greatersecurity.common.chest.TileEntityLockedChest;

import java.util.Calendar;

import net.minecraft.block.Block;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.model.ModelLargeChest;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TileEntityChestRenderer extends TileEntitySpecialRenderer
{
    /** The normal small chest model. */
    private ModelChest chestModel = new ModelChest();

    /** The large double chest model. */
    private ModelChest largeChestModel = new ModelLargeChest();
    private boolean isXmas;

    public TileEntityChestRenderer()
    {
        Calendar date = Calendar.getInstance();

        if (date.get(2) + 1 == 12 && date.get(5) >= 24 && date.get(5) <= 26)
        {
            this.isXmas = true;
        }
    }

    /**
     * Renders the TileEntity for the chest at a position.
     */
    public void renderTileEntityChestAt(TileEntityLockedChest chest, double xx, double yy, double zz, float par8)
    {
        int var9;

        if (!chest.func_70309_m())
        {
            var9 = 0;
        }
        else
        {
            Block block = chest.getBlockType();
            var9 = chest.getBlockMetadata();

            if (block != null && var9 == 0)
            {
                try
                {
                    ((BlockLockedChest)block).unifyAdjacentChests(chest.getWorldObj(), chest.xCoord, chest.yCoord, chest.zCoord);
                }
                catch (ClassCastException e)
                {
                    FMLLog.severe("Attempted to render a locked chest at %d,  %d, %d that was not a chest", chest.xCoord, chest.yCoord, chest.zCoord);
                }
                var9 = chest.getBlockMetadata();
            }

            chest.checkForAdjacentChests();
        }

        if (chest.adjacentChestZNeg == null && chest.adjacentChestXNeg == null)
        {
            ModelChest var14;

            if (chest.adjacentChestXPos == null && chest.adjacentChestZPosition == null)
            {
                var14 = this.chestModel;

                if (this.isXmas)
                {
                    this.bindTextureByName("/item/xmaschest.png");// TODO make my own holiday texture for chests
                }
                else
                {
                    this.bindTextureByName(GreaterSecurity.RESOURCE_PATH+"chest/1XChestRender.png");
                }
            }
            else
            {
                var14 = this.largeChestModel;

                if (this.isXmas)
                {
                    this.bindTextureByName("/item/largexmaschest.png");// TODO make my own holiday texture for chests
                }
                else
                {
                    this.bindTextureByName(GreaterSecurity.RESOURCE_PATH+"chest/2XChestRender.png");
                }
            }

            GL11.glPushMatrix();
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glTranslatef((float)xx, (float)yy + 1.0F, (float)zz + 1.0F);
            GL11.glScalef(1.0F, -1.0F, -1.0F);
            GL11.glTranslatef(0.5F, 0.5F, 0.5F);
            short var11 = 0;

            if (var9 == 2)
            {
                var11 = 180;
            }

            if (var9 == 3)
            {
                var11 = 0;
            }

            if (var9 == 4)
            {
                var11 = 90;
            }

            if (var9 == 5)
            {
                var11 = -90;
            }

            if (var9 == 2 && chest.adjacentChestXPos != null)
            {
                GL11.glTranslatef(1.0F, 0.0F, 0.0F);
            }

            if (var9 == 5 && chest.adjacentChestZPosition != null)
            {
                GL11.glTranslatef(0.0F, 0.0F, -1.0F);
            }

            GL11.glRotatef((float)var11, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
            float var12 = chest.prevLidAngle + (chest.lidAngle - chest.prevLidAngle) * par8;
            float var13;

            if (chest.adjacentChestZNeg != null)
            {
                var13 = chest.adjacentChestZNeg.prevLidAngle + (chest.adjacentChestZNeg.lidAngle - chest.adjacentChestZNeg.prevLidAngle) * par8;

                if (var13 > var12)
                {
                    var12 = var13;
                }
            }

            if (chest.adjacentChestXNeg != null)
            {
                var13 = chest.adjacentChestXNeg.prevLidAngle + (chest.adjacentChestXNeg.lidAngle - chest.adjacentChestXNeg.prevLidAngle) * par8;

                if (var13 > var12)
                {
                    var12 = var13;
                }
            }

            var12 = 1.0F - var12;
            var12 = 1.0F - var12 * var12 * var12;
            var14.chestLid.rotateAngleX = -(var12 * (float)Math.PI / 2.0F);
            var14.renderAll();
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            GL11.glPopMatrix();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    public void renderTileEntityAt(TileEntity par1TileEntity, double par2, double par4, double par6, float par8)
    {
        this.renderTileEntityChestAt((TileEntityLockedChest)par1TileEntity, par2, par4, par6, par8);
    }
}
