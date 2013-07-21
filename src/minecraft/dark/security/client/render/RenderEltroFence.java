package dark.security.client.render;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import dark.core.render.RenderMachine;
import dark.security.common.GreaterSecurity;
import dark.security.common.fence.electro.BlockElectroFence;

public class RenderEltroFence extends RenderMachine
{
	ModelEltroFence fenceModel;

	public RenderEltroFence()
	{
		fenceModel = new ModelEltroFence();
	}

	public void renderAModelAt(TileEntity tileEntity, double d, double d1, double d2, float sa)
	{
		if (tileEntity != null)
		{
			GL11.glPushMatrix();
			GL11.glTranslatef((float) d + 0.5F, (float) d1 + 1.5F, (float) d2 + 0.5F);
			GL11.glScalef(1.0F, -1F, -1F);

			this.bindTextureByName(GreaterSecurity.DOMAIN,GreaterSecurity.MODEL_DIRECTORY+ "EltroFence.png");

			BlockElectroFence fence = GreaterSecurity.blockElectroFence;

			fenceModel.render(0.0625F, tileEntity.worldObj.getBlockId(tileEntity.xCoord, tileEntity.yCoord + 1, tileEntity.zCoord) == 0);

			if (fence.canConnectFenceTo(tileEntity.worldObj, tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord - 1))
			{
				fenceModel.renderFence(0.0625F, false, false, 3);
			}
			if (fence.canConnectFenceTo(tileEntity.worldObj, tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord + 1))
			{
				fenceModel.renderFence(0.0625F, false, false, 1);
			}
			if (fence.canConnectFenceTo(tileEntity.worldObj, tileEntity.xCoord - 1, tileEntity.yCoord, tileEntity.zCoord))
			{
				fenceModel.renderFence(0.0625F, false, false, 0);
			}
			if (fence.canConnectFenceTo(tileEntity.worldObj, tileEntity.xCoord + 1, tileEntity.yCoord, tileEntity.zCoord))
			{
				fenceModel.renderFence(0.0625F, false, false, 2);
			}
			GL11.glPopMatrix();
		}
	}

	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double var2, double var4, double var6, float var8)
	{
		this.renderAModelAt(tileEntity, var2, var4, var6, var8);
	}

	@Override
	public ResourceLocation getTexture(int block, int meta)
	{
		// TODO Auto-generated method stub
		return null;
	}

}