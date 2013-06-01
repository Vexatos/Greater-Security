package hangcow.greatersecurity.client.render;

import dark.library.DarkMain;
import hangcow.greatersecurity.common.GreaterSecurity;
import hangcow.greatersecurity.common.fence.eltro.BlockEletroFence;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

public class RenderEltroFence extends TileEntitySpecialRenderer
{
	public RenderEltroFence()
	{
	}

	public void renderAModelAt(TileEntity tileEntity, double d, double d1, double d2, float sa)
	{
		if (tileEntity != null)
		{
			RenderBlocks renderBlocks = new RenderBlocks(tileEntity.worldObj);
			int x = (int) d;
			int y = (int) d1;
			int z = (int) d2;
			this.bindTextureByName(DarkMain.BLOCK_TEXTURE_DIRECTORY + "metalWoodEdge.png");
			BlockEletroFence fence = GreaterSecurity.blockEltroFence;
			renderBlocks.setOverrideBlockTexture(Block.blockIron.getBlockTextureFromSide(0));
			boolean flag = false;
			float f = 0.375F;
			float f1 = 0.625F;

			renderBlocks.setRenderBounds((double) f, 0.0D, (double) f, (double) f1, 1.0D, (double) f1);
			renderBlocks.renderStandardBlock(fence, x, y, z);

			flag = true;
			boolean flag1 = false;
			boolean flag2 = false;

			if (fence.canConnectFenceTo(renderBlocks.blockAccess, x - 1, y, z) || fence.canConnectFenceTo(renderBlocks.blockAccess, x + 1, y, z))
			{
				flag1 = true;
			}

			if (fence.canConnectFenceTo(renderBlocks.blockAccess, x, y, z - 1) || fence.canConnectFenceTo(renderBlocks.blockAccess, x, y, z + 1))
			{
				flag2 = true;
			}

			boolean flag3 = fence.canConnectFenceTo(renderBlocks.blockAccess, x - 1, y, z);
			boolean flag4 = fence.canConnectFenceTo(renderBlocks.blockAccess, x + 1, y, z);
			boolean flag5 = fence.canConnectFenceTo(renderBlocks.blockAccess, x, y, z - 1);
			boolean flag6 = fence.canConnectFenceTo(renderBlocks.blockAccess, x, y, z + 1);

			if (!flag1 && !flag2)
			{
				flag1 = true;
			}

			f = 0.4375F;
			f1 = 0.5625F;
			float f2 = 0.75F;
			float f3 = 0.9375F;
			float f4 = flag3 ? 0.0F : f;
			float f5 = flag4 ? 1.0F : f1;
			float f6 = flag5 ? 0.0F : f;
			float f7 = flag6 ? 1.0F : f1;

			if (flag1)
			{
				renderBlocks.setRenderBounds((double) f4, (double) f2, (double) f, (double) f5, (double) f3, (double) f1);
				renderBlocks.renderStandardBlock(fence, x, y, z);
				flag = true;
			}

			if (flag2)
			{
				renderBlocks.setRenderBounds((double) f, (double) f2, (double) f6, (double) f1, (double) f3, (double) f7);
				renderBlocks.renderStandardBlock(fence, x, y, z);
				flag = true;
			}

			f2 = 0.375F;
			f3 = 0.5625F;

			if (flag1)
			{
				renderBlocks.setRenderBounds((double) f4, (double) f2, (double) f, (double) f5, (double) f3, (double) f1);
				renderBlocks.renderStandardBlock(fence, x, y, z);
				flag = true;
			}

			if (flag2)
			{
				renderBlocks.setRenderBounds((double) f, (double) f2, (double) f6, (double) f1, (double) f3, (double) f7);
				renderBlocks.renderStandardBlock(fence, x, y, z);
				flag = true;
			}

			fence.setBlockBoundsBasedOnState(renderBlocks.blockAccess, x, y, z);
		}
	}

	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double var2, double var4, double var6, float var8)
	{
		this.renderAModelAt(tileEntity, var2, var4, var6, var8);
	}

}