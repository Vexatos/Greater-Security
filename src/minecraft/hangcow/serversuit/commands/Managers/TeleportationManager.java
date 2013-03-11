package hangcow.serversuit.commands.Managers;

import hangcow.serversuit.common.GreaterSeverSuit;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class TeleportationManager implements ITickHandler
{

    public static List<TeleportInstance> teleportList = new ArrayList<TeleportInstance>();
    public static long ticks = 0;

    /**
     * Adds a player to the teleporter list if he is not already on it
     */
    public static boolean addTeleport(EntityPlayerMP player, Loc teleport)
    {
        for (TeleportInstance tele : teleportList)
        {
            if (tele.player.equals(player)) { return false; }
        }
        TeleportationManager.teleportList.add(new TeleportInstance(player, 0, new Loc(player.posX, player.posY, player.posZ), teleport));
        return true;
    }

    /**
     * update the teleport list changing time and player prev location. if
     * location changes by more than 1 the teleport fails. If the time reaches
     * the max the player is teleported
     */
    public void updateTeleportList()
    {
        try
        {
            List<TeleportInstance> list = new ArrayList<TeleportInstance>();
            List<TeleportInstance> listNew = new ArrayList<TeleportInstance>();
            list.addAll(teleportList);

            teleportList.clear();
            for (TeleportInstance teleport : list)
            {
                if (teleport.player.hurtTime > 0)
                {
                    teleport.player.sendChatToPlayer("Teleport stoped");
                    return;
                }
                else if (teleport.Time++ >= GreaterSeverSuit.teleportTime)
                {
                    TPPlayer(teleport.player.worldObj, teleport.teleport.xx, teleport.teleport.yy, teleport.teleport.zz, teleport.player);
                    teleport.player.sendChatToPlayer("Teleporting");

                }
                else
                {
                    listNew.add(new TeleportInstance(teleport.player, teleport.Time++, new Loc(teleport.player.posX, teleport.player.posY, teleport.player.posZ), teleport.teleport));
                    if (teleport.Time == 0)
                    {
                        teleport.player.sendChatToPlayer("Teleporting in " + GreaterSeverSuit.teleportTime);
                    }
                    else if (teleport.Time >= GreaterSeverSuit.teleportTime)
                    {

                    }
                    else
                    {
                        teleport.player.sendChatToPlayer("... " + (GreaterSeverSuit.teleportTime - teleport.Time));
                    }
                }
            }
            teleportList.addAll(listNew);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Teleporation stuff
     */

    /**
     * Teleport the player to a location in the same dimension
     */
    public static void TPPlayer(World world, double xx, double yy, double zz, EntityPlayer player)
    {
        boolean canTP = false;
        if (player == null || world.isRemote || yy < 6 || yy > 254) { return; }

        IChunkProvider chunkProvider = world.getChunkProvider();
        ChunkCoordIntPair coords = world.getChunkFromBlockCoords((int) xx, (int) yy).getChunkCoordIntPair();
        chunkProvider.loadChunk(coords.chunkXPos - 3 >> 4, coords.chunkZPos - 3 >> 4);
        chunkProvider.loadChunk(coords.chunkXPos + 3 >> 4, coords.chunkZPos - 3 >> 4);
        chunkProvider.loadChunk(coords.chunkXPos - 3 >> 4, coords.chunkZPos + 3 >> 4);
        chunkProvider.loadChunk(coords.chunkXPos + 3 >> 4, coords.chunkZPos + 3 >> 4);
        player.setPositionAndUpdate(xx, yy, zz);

    }

    /**
     * Tick stuff
     */
    @Override
    public void tickStart(EnumSet<TickType> type, Object... tickData)
    {
        if (ticks++ >= 20)
        {
            ticks = 0;
            this.updateTeleportList();
        }

    }

    @Override
    public void tickEnd(EnumSet<TickType> type, Object... tickData)
    {

    }

    @Override
    public EnumSet<TickType> ticks()
    {
        return EnumSet.of(TickType.SERVER);
    }

    @Override
    public String getLabel()
    {
        // TODO Auto-generated method stub
        return "TeleTick";
    }

    public static ChunkCoordinates verifyRespawnCoordinates(World par0World, ChunkCoordinates par1ChunkCoordinates, boolean par2)
    {

        ChunkCoordinates c = par1ChunkCoordinates;
        Block block = Block.blocksList[par0World.getBlockId(c.posX, c.posY, c.posZ)];

        if (block != null && block.isBed(par0World, c.posX, c.posY, c.posZ, null))
        {
            ChunkCoordinates var8 = block.getBedSpawnPosition(par0World, c.posX, c.posY, c.posZ, null);
            return var8;
        }
        else
        {
            Material var4 = par0World.getBlockMaterial(par1ChunkCoordinates.posX, par1ChunkCoordinates.posY, par1ChunkCoordinates.posZ);
            Material var5 = par0World.getBlockMaterial(par1ChunkCoordinates.posX, par1ChunkCoordinates.posY + 1, par1ChunkCoordinates.posZ);
            boolean var6 = !var4.isSolid() && !var4.isLiquid();
            boolean var7 = !var5.isSolid() && !var5.isLiquid();
            return par2 && var6 && var7 ? par1ChunkCoordinates : null;
        }
    }
}
