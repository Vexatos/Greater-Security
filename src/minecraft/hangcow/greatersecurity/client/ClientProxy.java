package hangcow.greatersecurity.client;

import hangcow.greatersecurity.client.render.RenderChest;
import hangcow.greatersecurity.common.CommonProxy;
import hangcow.greatersecurity.common.chest.TileEntityLChest;
import net.minecraftforge.client.MinecraftForgeClient;
import cpw.mods.fml.client.registry.ClientRegistry;

public class ClientProxy extends CommonProxy
{
	@Override
	public void preInit()
	{
		 //Preloaded textures
		 MinecraftForgeClient.preloadTexture("/GUIs/GSBlocks.png");
		 MinecraftForgeClient.preloadTexture("/GUIs/blocks.png");
	}
	@Override
	public void init()
	{
	    ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLChest.class, new RenderChest());
	}
}
