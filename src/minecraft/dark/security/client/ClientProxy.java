package dark.security.client;


import java.awt.Color;

import net.minecraft.world.World;
import universalelectricity.core.vector.Vector3;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import dark.core.DarkMain;
import dark.library.effects.FXBeam;
import dark.security.client.render.RenderChest;
import dark.security.client.render.RenderEltroFence;
import dark.security.client.render.RenderLaserEmitter;
import dark.security.common.CommonProxy;
import dark.security.common.PlayerKeyHandler;
import dark.security.common.chest.TileEntityLockedChest;
import dark.security.common.fence.TileEntityElectroFence;
import dark.security.common.fence.TileEntityLaserFence;

public class ClientProxy extends CommonProxy
{
	@Override
	public void preInit()
	{
		KeyBindingRegistry.registerKeyBinding(new PlayerKeyHandler());
	}

	@Override
	public void init()
	{
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLockedChest.class, new RenderChest());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLaserFence.class, new RenderLaserEmitter());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityElectroFence.class, new RenderEltroFence());

		RenderingRegistry.registerBlockHandler(new dark.security.client.render.BlockRenderHelper());

	}

	/** Renders a laser beam from one power to another by a set color for a set time
	 * 
	 * @param world - world this laser is to be rendered in
	 * @param position - start vector3
	 * @param target - end vector3
	 * @param color - color of the beam
	 * @param age - life of the beam in 1/20 secs */
	public void renderBeam(World world, Vector3 position, Vector3 target, Color color, int age)
	{
		if (world.isRemote || FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
		{
			FMLClientHandler.instance().getClient().effectRenderer.addEffect(new FXBeam(world, position, target, color, DarkMain.TEXTURE_DIRECTORY + "", age));
		}
	}

}
