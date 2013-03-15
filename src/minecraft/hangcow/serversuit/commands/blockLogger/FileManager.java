package hangcow.serversuit.commands.blockLogger;


import hangcow.serversuit.commands.blockLogger.ActionInstance.ClickAction;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import universalelectricity.core.vector.Vector3;

public class FileManager
{
    static File folder = new File(cpw.mods.fml.common.Loader.instance().getConfigDir().getParent(), "/BlockLogger/");
    static File firstFile = new File(folder, "Loggs.txt");
    public static List<ActionInstance> breakEventList = new ArrayList<ActionInstance>();

    public static boolean fileCheck()
    {
        try
        {
            if (!folder.exists())
            {
                folder.mkdir();
            }
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public static void updateFiles() throws IOException
    {
        if (folder.exists())
        {

            if (breakEventList.size() <= 0) { return; }
            String timeStamp = new SimpleDateFormat("dd_HH.mm.ss").format(Calendar.getInstance().getTime());
            FileWriter writer = new FileWriter(new File(folder, "BlockEventLoggs_" + timeStamp + ".txt"));
            for (ActionInstance blocks : breakEventList)
            {

                String str = blocks.time + " " + blocks.location.intX() + "x " + blocks.location.intY() + "y " + blocks.location.intZ() + "z " + blocks.player.username + " " + blocks.action.name + " " + blocks.block.getUnlocalizedName();
                writer.write(str + System.getProperty("line.separator"));

            }
            writer.close();
            breakEventList.clear();
        }
        else
        {
            fileCheck();
        }
    }

    public static void addEvent(Vector3 loc, ClickAction act, EntityPlayer player, Block block)
    {
        String timeStamp = new SimpleDateFormat("MM_dd_HH.mm.ss").format(Calendar.getInstance().getTime());
        breakEventList.add(new ActionInstance(loc, block, act, timeStamp, player));
    }

}
