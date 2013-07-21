package dark.security.common;


import java.awt.Color;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import universalelectricity.core.vector.Vector3;
import cpw.mods.fml.common.network.IGuiHandler;
import dark.library.gui.ContainerFake;
import dark.library.gui.GuiGlobalList;
import dark.library.machine.terminal.TileEntityTerminal;
import dark.security.client.GuiDestroyResponce;
import dark.security.client.GuiUserAccess;
import dark.security.common.chest.TileEntityLockedChest;

public class CommonProxy implements IGuiHandler
{
	public static final int USERACCESS_GUI = 1;
	public static final int USERGLOBAL_GUI = 2;
	public static final int YES_NO_GUI = 3;

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int i, int j, int k)
	{
		TileEntity tileEntity = world.getBlockTileEntity(i, j, k);

		if (tileEntity != null)
		{
			switch (ID)
			{
				default:
					return new ContainerFake(tileEntity);
			}
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int i, int j, int k)
	{
		TileEntity tileEntity = world.getBlockTileEntity(i, j, k);
		if (ID == USERGLOBAL_GUI)
		{
			return new GuiGlobalList(player);
		}
		if (tileEntity != null)
		{
			switch (ID)
			{
				case USERACCESS_GUI:
					return new GuiUserAccess(player, (TileEntityTerminal) tileEntity, true);
				case YES_NO_GUI:
					return new GuiDestroyResponce(player, (TileEntityLockedChest) tileEntity);
			}
		}
		return null;
	}

	public void preInit()
	{

	}

	public void postInit()
	{

	}

	public void init()
	{

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
	}

	/** A method which pushes the entity backwards or forwards.
	 * 
	 * @param entity - entity to push
	 * @param knockback - distance to push
	 * @param inverted - true if the entity should be pushed forwards */
	public static void entityPush(Entity entity, int knockback, boolean inverted)
	{

		int facing = MathHelper.floor_double((entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		int knockbackNew;

		if (inverted)
		{

			knockbackNew = knockback * -1;

		}
		else
		{

			knockbackNew = knockback;

		}

		switch (facing)
		{

			case 0:
				entity.motionZ -= knockbackNew;
				break;

			case 1:
				entity.motionX += knockback;
				break;

			case 2:
				entity.motionZ += knockback;
				break;

			case 3:
				entity.motionX -= knockback;
				break;

			default:
				// TODO Possibly change, added for switch statement stability.
				entity.motionY += knockback;

				// Facing source: http://www.minecraftwiki.net/wiki/Coordinates

		}
	}

}
