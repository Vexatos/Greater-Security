package hangcow.greatersecurity.common.Dev;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.src.ModLoader;

public class NbtSaver 
{
    /**
     * 
     * @param data -  data being saved
     * @param dest - file destination
     * @param filename - file
     * @return if the file saved correctly
     */
    public static boolean saveData(NBTTagCompound data,String dest, String filename)
    {
        try
        {
            File bFile = new File(dest, filename + ".Backup.dat");
            File file = new File(dest, filename + ".dat");
            //write data to file
            CompressedStreamTools.writeCompressed(data, new FileOutputStream(file));

            if (bFile.exists()){bFile.delete();}
            file.renameTo(bFile);
            return true;
        }
        catch (Exception e)
        {
            ModLoader.getLogger().severe("Failed to create " + filename + ".dat!");
            return false;
        }
    }
    /**
     * 
     * @param loc - location of the file
     * @param filename - file 
     * @return nbt read from the file
     */
    public static NBTTagCompound loadData(String loc, String filename)
    {
        try
        {
            File file = new File(loc, filename + ".dat");

            if(file.exists())
            {
                return CompressedStreamTools.readCompressed(new FileInputStream(file));
            }
            else
            {
                return new NBTTagCompound();
            }
        }
        catch (Exception e)
        {
            ModLoader.getLogger().severe("Failed to read " + filename + ".dat!");
            e.printStackTrace();
            return null;
        }
    }
}