package hangcow.greatersecurity.common.network;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;

import com.google.common.io.ByteArrayDataInput;
/**
* @author Calclavia
**/
public interface IPacketReceiver
{
    /**
* Sends the tileEntity the rest of the data
*/
    public void handlePacketData(INetworkManager network, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream);
}