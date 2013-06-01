package hangcow.greatersecurity.client;

import hangcow.greatersecurity.client.render.RenderChest;
import hangcow.greatersecurity.client.render.RenderEltroFence;
import hangcow.greatersecurity.client.render.RenderLaserEmitter;
import hangcow.greatersecurity.common.CommonProxy;
import hangcow.greatersecurity.common.PlayerKeyHandler;
import hangcow.greatersecurity.common.chest.TileEntityLockedChest;
import hangcow.greatersecurity.common.fence.eltro.TileEntityEltroFence;
import hangcow.greatersecurity.common.fence.laser.TileEntityLaserFence;

import java.awt.Color;

import net.minecraft.world.World;
import universalelectricity.core.vector.Vector3;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import dark.library.DarkMain;
import dark.library.effects.FXBeam;

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
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityEltroFence.class, new RenderEltroFence());

		RenderingRegistry.registerBlockHandler(new hangcow.greatersecurity.client.render.BlockRenderHelper());

	}

	/**
	 * Renders a laser beam from one power to another by a set color for a set time
	 * 
	 * @param world - world this laser is to be rendered in
	 * @param position - start vector3
	 * @param target - end vector3
	 * @param color - color of the beam
	 * @param age - life of the beam in 1/20 secs
	 */
	public void renderBeam(World world, Vector3 position, Vector3 target, Color color, int age)
	{
		if (world.isRemote || FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
		{
			FMLClientHandler.instance().getClient().effectRenderer.addEffect(new FXBeam(world, position, target, color, DarkMain.TEXTURE_DIRECTORY + "", age));
		}
	}

}
