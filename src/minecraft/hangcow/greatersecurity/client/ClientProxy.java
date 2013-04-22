package hangcow.greatersecurity.client;

import hangcow.greatersecurity.client.render.FXBeam;
import hangcow.greatersecurity.client.render.RenderChest;
import hangcow.greatersecurity.client.render.RenderLaserEmitter;
import hangcow.greatersecurity.common.CommonProxy;
import hangcow.greatersecurity.common.chest.TileEntityLockedChest;
import hangcow.greatersecurity.common.laser.TileEntityLaserFence;

import java.awt.Color;

import net.minecraft.world.World;
import universalelectricity.core.vector.Vector3;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;

public class ClientProxy extends CommonProxy
{
	@Override
	public void preInit()
	{
	}

	@Override
	public void init()
	{
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLockedChest.class, new RenderChest());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLaserFence.class, new RenderLaserEmitter());
	}

	@Override
	public void renderBeam(World world, Vector3 position, Vector3 target, Color color, int age)
	{
		FMLClientHandler.instance().getClient().effectRenderer.addEffect(new FXBeam(world, position, target, color.getRed(), color.getGreen(), color.getBlue(), age));
	}
}
