package ml.boxes;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class ServerPacketHandler implements IPacketHandler {

	@Override
	public void onPacketData(INetworkManager manager,
			Packet250CustomPayload packet, Player player) {
		
		ByteArrayDataInput data = ByteStreams.newDataInput(packet.data);
		
		
	}
	
	public void sendBoxContentUpdatePacket(EntityPlayer player){
		EntityPlayerMP asPlayerMP = (EntityPlayerMP)player;
		
		ByteArrayOutputStream packStream = new ByteArrayOutputStream();
		DataOutputStream dataStream = new DataOutputStream(packStream);
		
		
		Packet250CustomPayload packet = new Packet250CustomPayload("Boxes", packStream.toByteArray());
		
		asPlayerMP.playerNetServerHandler.netManager.addToSendQueue(packet);
	}

}
