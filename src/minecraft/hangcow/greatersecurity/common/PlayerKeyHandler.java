package hangcow.greatersecurity.common;

import java.util.EnumSet;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.world.World;
import cpw.mods.fml.client.registry.KeyBindingRegistry.KeyHandler;
import cpw.mods.fml.common.TickType;
import dark.library.DarkMain;

public class PlayerKeyHandler extends KeyHandler
{
	public static KeyBinding openUserListGUI = new KeyBinding("User Access Settings", Keyboard.KEY_G);

	public PlayerKeyHandler()
	{
		super(new KeyBinding[] { openUserListGUI }, new boolean[]{false});
	}

	@Override
	public String getLabel()
	{
		return "GreaterSecurity";
	}

	@Override
	public void keyDown(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd, boolean isRepeat)
	{
		EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;		
		if (player == null || tickEnd)
		{
			return;
		}
		if (kb.equals(openUserListGUI))
		{
			World world = Minecraft.getMinecraft().theWorld;
			if (Minecraft.getMinecraft().inGameHasFocus)
			{
				player.openGui(GreaterSecurity.instance, CommonProxy.USERGLOBAL_GUI, world, 0, 0, 0);
			}
		}

	}

	@Override
	public void keyUp(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public EnumSet<TickType> ticks()
	{
		 return EnumSet.of(TickType.CLIENT);
	}

}
