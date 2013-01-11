package hangcow.greatersecurity.client.render;

import hangcow.greatersecurity.common.GreaterSecurity;
import hangcow.greatersecurity.common.chest.TileEntityLChest;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderChest extends TileEntitySpecialRenderer
{
    /** The normal small chest model. */
    private RenderSmallChest chestModel = new RenderSmallChest();

    /** The large double chest model. */
    private RenderSmallChest largeChestModel = new RenderLargeChest();

    /**
     * Renders the TileEntity for the chest at a position.
     */
    public void renderTileEntityChestAt(TileEntityLChest par1TileEntityChest, double par2, double par4, double par6, float par8)
    {
        int meta;
        int type = par1TileEntityChest.getType();
        if (!par1TileEntityChest.func_70309_m())
        {
            meta = 0;
        }
        else
        {
            Block block = par1TileEntityChest.getBlockType();
            meta = (par1TileEntityChest.getBlockMetadata()-(type*4));
            par1TileEntityChest.checkForAdjacentChests();
        }

        if (par1TileEntityChest.chests[0] == null && par1TileEntityChest.chests[2] == null)
        {
        	RenderSmallChest model;

            if (par1TileEntityChest.chests[1] == null && par1TileEntityChest.chests[3] == null)
            {
                model = this.chestModel;
                switch(type)
                {
                	default: this.bindTextureByName(GreaterSecurity.RESOURCE_PATH+"chest/1XChestRender.png");break;
                	case 1:  this.bindTextureByName(GreaterSecurity.RESOURCE_PATH+"chest/1XChestRenderI.png");break;
                	case 2:  this.bindTextureByName(GreaterSecurity.RESOURCE_PATH+"chest/1XChestRenderS.png");break;
                	case 3:  this.bindTextureByName(GreaterSecurity.RESOURCE_PATH+"chest/1XChestRenderO.png");break;
                }
            }
            else
            {
                model = this.largeChestModel;
                switch(type)
                {
                	default: this.bindTextureByName(GreaterSecurity.RESOURCE_PATH+"chest/2xChestRender.png");break;
                	case 1:  this.bindTextureByName(GreaterSecurity.RESOURCE_PATH+"chest/2xChestRenderI.png");break;
                	case 2:  this.bindTextureByName(GreaterSecurity.RESOURCE_PATH+"chest/2xChestRenderS.png");break;
                	case 3:  this.bindTextureByName(GreaterSecurity.RESOURCE_PATH+"chest/2xChestRenderO.png");break;
                }
            }

            GL11.glPushMatrix();
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glTranslatef((float)par2, (float)par4 + 1.0F, (float)par6 + 1.0F);
            GL11.glScalef(1.0F, -1.0F, -1.0F);
            GL11.glTranslatef(0.5F, 0.5F, 0.5F);
            short angle = 0;
            
            if (meta == 0)
            {
                angle = 180;
            }

            if (meta == 2)
            {
                angle = 0;
            }

            if (meta == 1)
            {
                angle = -90;
            }

            if (meta == 3)
            {
                angle = +90;
            }

            if (meta == 0 && par1TileEntityChest.chests[1] != null)
            {
                GL11.glTranslatef(1.0F, 0.0F, 0.0F);
            }

            if (meta == 1 && par1TileEntityChest.chests[3] != null)
            {
                GL11.glTranslatef(0.0F, 0.0F, -1.0F);
            }

            GL11.glRotatef((float)angle, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
            model.renderAll();
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            GL11.glPopMatrix();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    public void renderTileEntityAt(TileEntity par1TileEntity, double par2, double par4, double par6, float par8)
    {
        this.renderTileEntityChestAt((TileEntityLChest)par1TileEntity, par2, par4, par6, par8);
    }
}
