package hangcow.serversuit.commands.Managers;

import java.io.IOException;
import java.util.EnumSet;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class Clock implements ITickHandler  {
	int ticks = 0;
	int seconds = 0;
	int mins = 0;
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		if(ticks++ >= 20)
		{
			ticks = 0;
			if(seconds++ >= 60)
			{
				if(FileManager.breakEventList.size() >= 1000)
				{
					try {
						FileManager.updateFiles();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				seconds = 0;
				if(mins++ >= 60)
				{
					mins = 0;
					try {
						FileManager.updateFiles();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public EnumSet<TickType> ticks() {
		// TODO Auto-generated method stub
		return EnumSet.of(TickType.SERVER);
	}

	@Override
	public String getLabel() {
		// TODO Auto-generated method stub
		return "UpdaterClock";
	}

}
