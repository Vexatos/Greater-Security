package hangcow.greatersecurity.client.render;

import hangcow.greatersecurity.common.GreaterSecurity;
import hangcow.greatersecurity.common.laser.TileEntityLaserFence;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

import org.lwjgl.opengl.GL11;

public class RenderLaserEmitter extends TileEntitySpecialRenderer
{
	private ModelLaserEmitter model;

	public RenderLaserEmitter()
	{
		model = new ModelLaserEmitter();
	}

	public void renderAModelAt(TileEntityLaserFence tileEntity, double d, double d1, double d2, float f)
	{
		bindTextureByName(GreaterSecurity.MODEL_File_PATH + "LaserEmitter.png");

		int meta = tileEntity.worldObj.getBlockMetadata(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord) % 6;

		GL11.glPushMatrix();
		GL11.glTranslatef((float) d + 0.5F, (float) d1 + 1.5F, (float) d2 + 0.5F);
		GL11.glScalef(1.0F, -1F, -1F);

		boolean rotate = tileEntity.worldObj.getBlockMetadata(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord) > 5;

		ForgeDirection direction = ForgeDirection.getOrientation(meta);

		if (direction == ForgeDirection.UP)
		{
			GL11.glRotatef(-90f, 1f, 0f, 0f);
			GL11.glTranslatef(0f, -1f, 1f);
			if (rotate)
			{
				GL11.glRotatef(90f, 0f, 0f, 1f);
				GL11.glTranslatef(1f, -1f, 0f);
			}
		}
		else if (direction == ForgeDirection.DOWN)
		{
			GL11.glRotatef(90f, 1f, 0f, 0f);
			GL11.glTranslatef(0f, -1f, -1f);
			if (rotate)
			{
				GL11.glRotatef(90f, 0f, 0f, 1f);
				GL11.glTranslatef(1f, -1f, 0f);
			}
		}
		else if (direction == ForgeDirection.EAST)
		{
			GL11.glRotatef(90f, 0f, 1f, 0f);
			if (rotate)
			{
				GL11.glRotatef(90f, 0f, 0f, 1f);
				GL11.glTranslatef(1f, -1f, 0f);
			}
		}
		else if (direction == ForgeDirection.WEST)
		{
			GL11.glRotatef(270f, 0f, 1f, 0f);
			if (rotate)
			{
				GL11.glRotatef(90f, 0f, 0f, 1f);
				GL11.glTranslatef(1f, -1f, 0f);
			}
		}
		else if (direction == ForgeDirection.NORTH)
		{
			GL11.glRotatef(0f, 0f, 1f, 0f);
			if (rotate)
			{
				GL11.glRotatef(90f, 0f, 0f, 1f);
				GL11.glTranslatef(1f, -1f, 0f);
			}
		}
		else if (direction == ForgeDirection.SOUTH)
		{
			GL11.glRotatef(180f, 0f, 1f, 0f);
			if (rotate)
			{
				GL11.glRotatef(90f, 0f, 0f, 1f);
				GL11.glTranslatef(1f, -1f, 0f);
			}
		}

		model.render(0.0625F);
		GL11.glPopMatrix();

	}

	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double var2, double var4, double var6, float var8)
	{
		this.renderAModelAt((TileEntityLaserFence) tileEntity, var2, var4, var6, var8);
	}

}