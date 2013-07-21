package dark.security.client.render;


import java.util.Calendar;

import net.minecraft.block.Block;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.model.ModelLargeChest;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import dark.core.render.RenderMachine;
import dark.security.common.GreaterSecurity;
import dark.security.common.chest.BlockLockedChest;
import dark.security.common.chest.TileEntityLockedChest;

@SideOnly(Side.CLIENT)
public class RenderChest extends RenderMachine
{
	/** The normal small chest model. */
	private ModelChest chestModel = new ModelChest();

	/** The large double chest model. */
	private ModelChest largeChestModel = new ModelLargeChest();
	private boolean isXmas;

	public RenderChest()
	{
		Calendar date = Calendar.getInstance();

		if (date.get(2) + 1 == 12 && date.get(5) >= 24 && date.get(5) <= 26)
		{
			this.isXmas = true;
		}
	}

	/** Renders the TileEntity for the chest at a position. */
	public void renderTileEntityChestAt(TileEntityLockedChest chest, double xx, double yy, double zz, float par8)
	{
		int metaData = 0;

			Block block = chest.getBlockType();
			metaData = chest.getBlockMetadata();

			if (block != null && metaData == 0)
			{
				try
				{
					((BlockLockedChest) block).unifyAdjacentChests(chest.getWorldObj(), chest.xCoord, chest.yCoord, chest.zCoord);
				}
				catch (ClassCastException e)
				{
					FMLLog.severe("Attempted to render a locked chest at %d,  %d, %d that was not a chest", chest.xCoord, chest.yCoord, chest.zCoord);
				}
				metaData = chest.getBlockMetadata();
			}

			chest.checkForAdjacentChests();

		if (chest.adjacentChestZNeg == null && chest.adjacentChestXNeg == null)
		{
			ModelChest var14;

			if (chest.adjacentChestXPos == null && chest.adjacentChestZPosition == null)
			{
				var14 = this.chestModel;

					this.bindTextureByName(GreaterSecurity.DOMAIN,GreaterSecurity.MODEL_DIRECTORY + "chest/1XChestRender.png");
			}
			else
			{
				var14 = this.largeChestModel;

					this.bindTextureByName(GreaterSecurity.DOMAIN,GreaterSecurity.MODEL_DIRECTORY + "chest/2xChestRender.png");
			}

			GL11.glPushMatrix();
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glTranslatef((float) xx, (float) yy + 1.0F, (float) zz + 1.0F);
			GL11.glScalef(1.0F, -1.0F, -1.0F);
			GL11.glTranslatef(0.5F, 0.5F, 0.5F);
			short var11 = 0;

			if (metaData == 2)
			{
				var11 = 180;
			}

			if (metaData == 3)
			{
				var11 = 0;
			}

			if (metaData == 4)
			{
				var11 = 90;
			}

			if (metaData == 5)
			{
				var11 = -90;
			}

			if (metaData == 2 && chest.adjacentChestXPos != null)
			{
				GL11.glTranslatef(1.0F, 0.0F, 0.0F);
			}

			if (metaData == 5 && chest.adjacentChestZPosition != null)
			{
				GL11.glTranslatef(0.0F, 0.0F, -1.0F);
			}

			GL11.glRotatef((float) var11, 0.0F, 1.0F, 0.0F);
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
			var14.chestLid.rotateAngleX = -(var12 * (float) Math.PI / 2.0F);
			var14.renderAll();
			GL11.glDisable(GL12.GL_RESCALE_NORMAL);
			GL11.glPopMatrix();
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		}
	}

	public void renderTileEntityAt(TileEntity par1TileEntity, double par2, double par4, double par6, float par8)
	{
		this.renderTileEntityChestAt((TileEntityLockedChest) par1TileEntity, par2, par4, par6, par8);
	}

	@Override
	public ResourceLocation getTexture(int block, int meta)
	{
		// TODO Auto-generated method stub
		return null;
	}
}
