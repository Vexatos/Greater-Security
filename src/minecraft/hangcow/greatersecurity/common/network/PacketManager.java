package hangcow.greatersecurity.common.network;
import hangcow.greatersecurity.common.chest.TileEntityLChest;
import hangcow.greatersecurity.common.door.TileEntityLDoor;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class PacketManager implements IPacketHandler
{
@Override
public void onPacketData(INetworkManager network, Packet250CustomPayload packet, Player player)
{
try
        {
			ByteArrayDataInput data = ByteStreams.newDataInput(packet.data);
			int x = data.readInt();
			int y = data.readInt();
			int z = data.readInt();			
			World world = ((EntityPlayer)player).worldObj;
			
			if(world != null)
			{
				TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
				
				if(tileEntity != null)
				{
					if(tileEntity instanceof IPacketReceiver)
					{
					((IPacketReceiver)tileEntity).handlePacketData(network,
							packet,((EntityPlayer)player),
							data);
					}
				}
			}

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
}


public static void sendChestPacketPacket(TileEntityLChest sender,int ID, String BlockOwner,List<String> users)
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        DataOutputStream data = new DataOutputStream(bytes);

        try
        {
            data.writeInt(sender.xCoord);
            data.writeInt(sender.yCoord);
            data.writeInt(sender.zCoord);
            data.writeInt(users.size());
            data.writeInt(ID);
            data.writeInt(sender.numUsingPlayers);
            if(ID == 0)
            {
	            data.writeUTF(BlockOwner);
	            for(int i = 0;i<users.size();i++)
	            {
	            	data.writeUTF(users.get(i));
	            }
            }
            if(ID == 1 || ID == 2)
            {
            	for(int i = 0;i<users.size();i++)
	            {
	            	data.writeUTF(users.get(i));
	            }
            }
            
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Packet250CustomPayload packet = new Packet250CustomPayload();
        packet.channel = "GreaterSecurity";
        packet.data = bytes.toByteArray();
        packet.length = packet.data.length;
        packet.isChunkDataPacket = true;
    	FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().sendPacketToAllPlayers(packet);

    }
public static void sendDoorPacketPacket(TileEntityLDoor sender,int ID, String BlockOwner,List<String> users)
{
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    DataOutputStream data = new DataOutputStream(bytes);

    try
    {
        data.writeInt(sender.xCoord);
        data.writeInt(sender.yCoord);
        data.writeInt(sender.zCoord);
        data.writeInt(users.size());
        data.writeInt(ID);
        if(ID == 0)
        {
            data.writeUTF(BlockOwner);
            for(int i = 0;i<users.size();i++)
            {
            	data.writeUTF(users.get(i));
            }
        }
        if(ID == 1 || ID == 2)
        {
        	for(int i = 0;i<users.size();i++)
            {
            	data.writeUTF(users.get(i));
            }
        }
        
    }
    catch (IOException e)
    {
        e.printStackTrace();
    }

    Packet250CustomPayload packet = new Packet250CustomPayload();
    packet.channel = "GreaterSecurity";
    packet.data = bytes.toByteArray();
    packet.length = packet.data.length;
    packet.isChunkDataPacket = true;
	FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().sendPacketToAllPlayers(packet);

}
public static void sendChestPacketServer(TileEntityLChest sender,int ID, String users)
{
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    DataOutputStream data = new DataOutputStream(bytes);

    try
    {
        data.writeInt(sender.xCoord);
        data.writeInt(sender.yCoord);
        data.writeInt(sender.zCoord);
        data.writeInt(1);
        data.writeInt(ID);
        data.writeInt(sender.numUsingPlayers);
        data.writeUTF(users);
        
    }
    catch (IOException e)
    {
        e.printStackTrace();
    }

    Packet250CustomPayload packet = new Packet250CustomPayload();
    packet.channel = "GreaterSecurity";
    packet.data = bytes.toByteArray();
    packet.length = packet.data.length;
    packet.isChunkDataPacket = true;
    PacketDispatcher.sendPacketToServer(packet);

}
public static void sendDoorPacketServer(TileEntityLDoor sender,int ID, String users)
{
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    DataOutputStream data = new DataOutputStream(bytes);

    try
    {
        data.writeInt(sender.xCoord);
        data.writeInt(sender.yCoord);
        data.writeInt(sender.zCoord);
        data.writeInt(1);
        data.writeInt(ID);
        data.writeUTF(users);
        
    }
    catch (IOException e)
    {
        e.printStackTrace();
    }

    Packet250CustomPayload packet = new Packet250CustomPayload();
    packet.channel = "GreaterSecurity";
    packet.data = bytes.toByteArray();
    packet.length = packet.data.length;
    packet.isChunkDataPacket = true;
    PacketDispatcher.sendPacketToServer(packet);

}
}