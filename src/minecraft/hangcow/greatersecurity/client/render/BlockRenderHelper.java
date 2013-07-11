package hangcow.greatersecurity.client.render;

import hangcow.greatersecurity.common.GreaterSecurity;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.resources.ResourceLocation;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class BlockRenderHelper implements ISimpleBlockRenderingHandler
{
	public static BlockRenderHelper instance = new BlockRenderHelper();
	public static int renderID = RenderingRegistry.getNextAvailableRenderId();
	private ModelLaserEmitter laser = new ModelLaserEmitter();

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer)
	{
		if (block.blockID == GreaterSecurity.blockLaserFence.blockID)
		{
			GL11.glPushMatrix();
			GL11.glTranslatef((float) 0.0F, (float) 1.1F, (float) 0.0F);
			GL11.glRotatef(180f, 0f, 0f, 1f);
			FMLClientHandler.instance().getClient().renderEngine.func_110577_a(new ResourceLocation(GreaterSecurity.MODEL_File_PATH + "LaserEmitter.png"));
			laser.render(0.0725F);
			GL11.glPopMatrix();
		}
		else if (block.blockID == GreaterSecurity.blockElectroFence.blockID)
		{
		}

	}

	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
	{
		return false;
	}

	public boolean shouldRender3DInInventory()
	{
		return true;
	}

	public int getRenderId()
	{
		return renderID;
	}
}
