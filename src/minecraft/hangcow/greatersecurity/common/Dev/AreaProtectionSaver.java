package hangcow.greatersecurity.common.Dev;

import java.io.File;

import cpw.mods.fml.common.Loader;
import net.minecraft.nbt.NBTTagCompound;

public class AreaProtectionSaver
{
    public static final String SAVE_LOC = Loader.instance().getConfigDir().getParent()+"/GreaterSecurity/Saves/";
    /**
     * gets the nbt file linked to the player
     */
    public static NBTTagCompound getPlayer(String name)
    {
       return NbtSaver.loadData(SAVE_LOC+"players/", name);
    }
    
    public static void writeToPlayer(NBTTagCompound data, String name)
    {
        NbtSaver.saveData(data, SAVE_LOC+"players/", name);
    }
}
