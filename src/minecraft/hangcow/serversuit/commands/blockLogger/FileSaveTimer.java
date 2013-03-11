package hangcow.serversuit.commands.blockLogger;

import java.io.IOException;
import java.util.EnumSet;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class FileSaveTimer implements ITickHandler
{
	protected static long ticks = 0;
	private static int saveTime = 72000;
	private static int entrySize = 1000;

	/**
	 * sets the save interval for the logs in minutes
	 */
	public void setSaveTimer(int mins)
	{
		this.saveTime = (mins * 60 * 20);
	}

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData)
	{
		ticks++;
		if (ticks >= Long.MAX_VALUE)
		{
			ticks = 1;
		}

	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData)
	{
		if (ticks % saveTime == 0 || FileManager.breakEventList.size() >= entrySize)
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
