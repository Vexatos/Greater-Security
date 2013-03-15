package hangcow.serversuit.events;

import hangcow.serversuit.commands.blockLogger.ActionInstance.ClickAction;
import hangcow.serversuit.commands.blockLogger.FileManager;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import universalelectricity.core.vector.Vector3;

public class EventHandlerPlayer
{ 
    public static final String SAVE_NAME = "GSSDATA";
    
   

    public enum killTypes
    {
        PVP("pvp", "GPPVPKills"), PVPSPREES("pvp spree", "GPspreeKills"), MOB("monster", "GPMOBKills");
        public String name;
        public String saveName;

        private killTypes(String name, String saveName)
        {
            this.name = name;
        }
    }

    @ForgeSubscribe
    public void entityDeathEvent(LivingDeathEvent event)
    {
        try
        {
            if (event.entity instanceof EntityPlayer)
            {

                EnumDeathTypes death = EnumDeathTypes.getTypes(event.source);
                ((EntityPlayer) event.entity).sendChatToPlayer("Debug: " + " You died to " + death.name);
                this.setDeath((EntityPlayer) event.entity, death, 1, true);

                if (death == EnumDeathTypes.PVP && event.source.getEntity() instanceof EntityPlayer)
                {
                    this.setKills((EntityPlayer) event.source.getEntity(), killTypes.PVP, 1, true);
                    if (this.getKills((EntityPlayer) event.entity, killTypes.PVPSPREES) > 2)
                    {
                        this.sendGlobalMessage(event.entity.worldObj, "Server", (EntityPlayer) event.source.getEntity() + " has ended " + (EntityPlayer) event.entity + " Killing spree ");
                    }
                }

                this.setKills((EntityPlayer) event.entity, killTypes.PVPSPREES, 0, false);
            }
            if (event.entity instanceof EntityMob && event.source.getEntity() instanceof EntityPlayer)
            {
                this.setKills((EntityPlayer) event.source.getEntity(), killTypes.MOB, 1, true);
            }
        }
        catch (Exception e)
        {
            System.out.print(" \n Error writing death data \n ");
            e.printStackTrace();
        }

    }

    /**
     * retrieves the deaths records from player's nbt data
     */
    public static int getDeaths(EntityPlayer player, EnumDeathTypes death)
    {
        if (player == null) { return 0; } 
        return  EventHandlerPlayer.getPlayerSave(player).getInteger(death.saveName);
    }

    /**
     * sets the deaths records in player's nbt data
     */
    public static void setDeath(EntityPlayer player, EnumDeathTypes death, int num, boolean increase)
    {
        if (player == null) { return; }
        NBTTagCompound victumData = EventHandlerPlayer.getPlayerSave(player);
        int deaths = getDeaths(player, death);
        if (increase)
        {            
            player.sendChatToPlayer("Deaths: " + death.name + " is at " + deaths);
            victumData.setInteger(death.saveName, deaths + num);
        }
        else
        {
            victumData.setInteger(death.saveName, num);
        }
        EventHandlerPlayer.saveToPlayerSave(player, victumData);
       
    }

    /**
     * retrieves the kill records from player's nbt data
     */
    public static int getKills(EntityPlayer player, killTypes kill)
    {
        if (player == null) { return 0; }
        return  EventHandlerPlayer.getPlayerSave(player).getInteger(kill.saveName);
    }

    /**
     * sets the kill records in player's nbt data
     */
    public static void setKills(EntityPlayer player, killTypes kill, int num, boolean increase)
    {
        if (player == null) { return; }
        NBTTagCompound victumData = EventHandlerPlayer.getPlayerSave(player);
        int deaths = 0;
        if (increase)
        {
            deaths = getKills(player, kill);
        }
        victumData.setInteger(kill.saveName, deaths + num);
        EventHandlerPlayer.saveToPlayerSave(player, victumData);
    }

    @ForgeSubscribe
    public void logBlock(PlayerInteractEvent event)
    {
        try
        {
            if (event.entityPlayer instanceof EntityPlayer)
            {
                EntityPlayer player = event.entityPlayer;
                World world = player.worldObj;
                if (player.dimension == 0)
                {
                    int id = world.getBlockId(event.x, event.y, event.z);
                    Block block = Block.blocksList[id];
                    TileEntity ent = world.getBlockTileEntity(event.x, event.y, event.z);
                    Vector3 loc = new Vector3(event.x, event.y, event.z);
                    if (id != 0 && block != null)
                    {
                        if (event.action == Action.RIGHT_CLICK_BLOCK)
                        {
                            if (ent != null)
                            {
                                if (ent.getClass() == TileEntityChest.class)
                                {
                                    FileManager.addEvent(loc, ClickAction.OPENED, player, block);
                                }
                                FileManager.addEvent(loc, ClickAction.OPENED, player, block);
                            }
                            else
                            {
                                FileManager.addEvent(loc, ClickAction.RIGHT, player, block);
                            }
                        }
                        else if (event.action == Action.LEFT_CLICK_BLOCK)
                        {
                            FileManager.addEvent(loc, ClickAction.LEFT, player, block);
                        }
                        else if (event.action == Action.RIGHT_CLICK_AIR)
                        {

                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            System.out.print(" \n Log failed to track something \n ");
            e.printStackTrace();

        }
    }

    /**
     * Sends a global message to all players
     * @param msg
     */
    public static void sendGlobalMessage(World world, String sender, String msg)
    {
        try
        {
            List<EntityPlayer> players = new ArrayList<EntityPlayer>();
            for (Object obj : world.playerEntities)
            {
                if (obj instanceof EntityPlayer && !players.contains((EntityPlayer) obj))
                {
                    players.add((EntityPlayer) obj);
                }
            }
            for (EntityPlayer player : players)
            {
                player.sendChatToPlayer(sender + ": " + msg);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    /**
     * gets the persistant save data that cares over with the player on death
     * @param player
     * @param data
     * @return
     */
    public static NBTTagCompound getPlayerSave(EntityPlayer player)
    {
        return player.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).getCompoundTag(EventHandlerPlayer.SAVE_NAME);
    }
    /**
     * resaves the persistant save data to the player
     * @param player
     * @param tag
     */
    public static void saveToPlayerSave(EntityPlayer player, NBTTagCompound tag)
    {
                
        NBTTagCompound save = player.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);        
        save.setCompoundTag(EventHandlerPlayer.SAVE_NAME, tag);
        player.getEntityData().setCompoundTag(EntityPlayer.PERSISTED_NBT_TAG, save);
       
    }

}
