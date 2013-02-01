package ml.boxes.network;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import ml.boxes.TileEntityBox;
import ml.boxes.network.packets.PacketUpdateData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;

/**
 * Thanks to MachineMuse for the idea on how to implement this
 * @author Matchlighter
 *
 */
public class PacketHandler implements IPacketHandler {
		
	protected static BiMap<Integer, Class<? extends MLPacket>> PacketTypes = HashBiMap.create();
	
	protected static void addHandler(Class<? extends MLPacket> pktClass){
		PacketTypes.put(PacketTypes.size(), pktClass);
	}
	
	public PacketHandler() {
		addHandler(PacketUpdateData.class);
	}
	
	@Override
	public void onPacketData(INetworkManager manager,
			Packet250CustomPayload packet, Player player) {
		
		MLPacket mlPkt = tryCastPacket(packet, player);
		if (mlPkt != null){
			try {
				
				if (FMLCommonHandler.instance().getSide().isServer()){
					mlPkt.handleServerSide();
				} else {
					mlPkt.handleClientSide();
				}
				
			} catch (IOException e) {
				// "UNPOSSIBLE?" -cpw
			}
		}
	}

	private static MLPacket tryCastPacket(Packet250CustomPayload pkt, Player pl){
		ByteArrayDataInput dat = ByteStreams.newDataInput(pkt.data);
		int pkId = dat.readInt();
		if (PacketTypes.get(pkId) != null){
			try {
				Constructor<? extends MLPacket> contructor = PacketTypes.get(pkId).getConstructor(Player.class, ByteArrayDataInput.class);
				return contructor.newInstance(pl, dat);
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			
		}
		return null;
	}
	
	public static Integer findPacketId(Class<? extends MLPacket> pktClass){
		return PacketTypes.inverse().get(pktClass);
	}
	
	public void sendBoxContentUpdatePacket(EntityPlayer player){
		EntityPlayerMP asPlayerMP = (EntityPlayerMP)player;
		
		ByteArrayOutputStream packStream = new ByteArrayOutputStream();
		DataOutputStream dataStream = new DataOutputStream(packStream);
		
		
		Packet250CustomPayload packet = new Packet250CustomPayload("Boxes", packStream.toByteArray());
		
		asPlayerMP.playerNetServerHandler.netManager.addToSendQueue(packet);
	}
	
	public Packet getBoxDataUpdatePacket(TileEntityBox teBox){
		
		ByteArrayOutputStream packStream = new ByteArrayOutputStream();
		DataOutputStream dataStream = new DataOutputStream(packStream);
		
		try {
			dataStream.writeInt(teBox.xCoord);
			dataStream.writeInt(teBox.yCoord);
			dataStream.writeInt(teBox.zCoord);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Packet250CustomPayload packet = new Packet250CustomPayload("Boxes", packStream.toByteArray());
		
		return packet;
	}

}
