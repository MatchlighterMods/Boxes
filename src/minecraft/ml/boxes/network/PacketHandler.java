package ml.boxes.network;

import java.util.logging.Level;

import net.minecraft.network.packet.Packet250CustomPayload;

import cpw.mods.fml.common.FMLLog;
import ml.boxes.network.packets.PacketTipClick;
import ml.boxes.network.packets.PacketUpdateData;
import ml.core.network.MLPacket;

public class PacketHandler extends ml.core.network.PacketHandler {
	public PacketHandler() {
		addHandler(PacketUpdateData.class);
		addHandler(PacketTipClick.class);
	}

	@Override
	protected void onError(Throwable e, MLPacket mlPkt) {
		FMLLog.log(Level.SEVERE, e, "Boxes failed to process packet");
	}
}
