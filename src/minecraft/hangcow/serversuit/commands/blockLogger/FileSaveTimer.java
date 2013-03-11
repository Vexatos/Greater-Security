package hangcow.serversuit.commands.blockLogger;

import java.io.IOException;
import java.util.EnumSet;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class FileSaveTimer implements ITickHandler
{
	Long ticks;
	int saveTime = 72000;

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData)
	{
		ticks++;
		if (ticks == Long.MAX_VALUE)
		{
			ticks = Long.MIN_VALUE;
		}

	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData)
	{
		if (ticks % saveTime == 0 || FileManager.breakEventList.size() >= 1000)
		{

			try
			{
				FileManager.updateFiles();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}

	}

	@Override
	public EnumSet<TickType> ticks()
	{
		return EnumSet.of(TickType.SERVER);
	}

	@Override
	public String getLabel()
	{
		return "SimulatedClock";
	}

}
