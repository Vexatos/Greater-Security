package hangcow.greatersecurity.client;

import hangcow.greatersecurity.common.CommonProxy;
import hangcow.greatersecurity.common.GreaterSecurity;
import hangcow.greatersecurity.common.chest.TileEntityLockedChest;
import net.minecraftforge.client.MinecraftForgeClient;
import cpw.mods.fml.client.registry.ClientRegistry;

public class ClientProxy extends CommonProxy
{
	@Override
	public void preInit()
	{
		// Preloaded textures
		MinecraftForgeClient.preloadTexture(GreaterSecurity.ITEM_File_PATH);
		MinecraftForgeClient.preloadTexture(GreaterSecurity.BLOCK_File_PATH);
	}

	@Override
	public void init()
	{
		//ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLockedChest.class, new RenderChest());
	}
}
