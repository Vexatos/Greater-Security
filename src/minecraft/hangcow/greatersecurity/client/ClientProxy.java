package hangcow.greatersecurity.client;

import hangcow.greatersecurity.client.render.RenderChest;
import hangcow.greatersecurity.client.render.RenderLaserEmitter;
import hangcow.greatersecurity.common.CommonProxy;
import hangcow.greatersecurity.common.chest.TileEntityLockedChest;
import hangcow.greatersecurity.common.laser.TileEntityLaserFence;
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
}
