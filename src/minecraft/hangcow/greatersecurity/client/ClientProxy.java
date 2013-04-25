package hangcow.greatersecurity.client;

import hangcow.greatersecurity.client.render.RenderChest;
import hangcow.greatersecurity.client.render.RenderLaserEmitter;
import hangcow.greatersecurity.common.CommonProxy;
import hangcow.greatersecurity.common.PlayerKeyHandler;
import hangcow.greatersecurity.common.chest.TileEntityLockedChest;
import hangcow.greatersecurity.common.laser.TileEntityLaserFence;

import java.awt.Color;

import net.minecraft.world.World;
import universalelectricity.core.vector.Vector3;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
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

		RenderingRegistry.registerBlockHandler(new hangcow.greatersecurity.client.render.BlockRenderHelper());
		
	}

	
}
