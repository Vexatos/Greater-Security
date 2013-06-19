package hangcow.greatersecurity.common.cmd;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import universalelectricity.core.vector.Vector3;
import dark.core.api.ISpecialAccess;
import dark.core.api.ITerminal;
import dark.library.access.AccessLevel;
import dark.library.machine.terminal.TerminalCommand;
import dark.library.machine.terminal.TileEntityTerminal;

public class CommandBreak extends TerminalCommand
{

	@Override
	public String getCommandPrefix()
	{
		// TODO Auto-generated method stub
		return "break";
	}

	@Override
	public boolean processCommand(EntityPlayer player, ITerminal terminal, String[] args)
	{
		if (terminal instanceof TileEntity)
		{
			player.sendChatToPlayer("Removing Block");
			Vector3 vec = new Vector3(((TileEntity) terminal).xCoord, ((TileEntity) terminal).yCoord, ((TileEntity) terminal).zCoord);
			World world = ((TileEntity) terminal).worldObj;
			int id = vec.getBlockID(world);
			int meta = vec.getBlockMetadata(world);
			Block block = Block.blocksList[id];
			if (block != null)
			{
				block.breakBlock(world, vec.intX(), vec.intY(), vec.intZ(), id, meta);
				block.dropBlockAsItem(world, vec.intX(), vec.intY(), vec.intZ(), meta, 0);
				world.setBlock(vec.intX(), vec.intY(), vec.intZ(), 0);
				return true;
			}

		}

		return false;
	}

	@Override
	public boolean canPlayerUse(EntityPlayer var1, ISpecialAccess mm)
	{
		return mm.getUserAccess(var1.username).ordinal() >= AccessLevel.ADMIN.ordinal();
	}

	@Override
	public boolean showOnHelp(EntityPlayer player, ISpecialAccess mm)
	{
		return this.canPlayerUse(player, mm);
	}

	@Override
	public List<String> getCmdUses(EntityPlayer player, ISpecialAccess specialAccess)
	{
		List<String> list = new ArrayList<String>();
		list.add("break");
		return list;
	}

	@Override
	public boolean canMachineUse(ISpecialAccess specialAccess)
	{
		return specialAccess instanceof TileEntityTerminal;
	}

}
