package ml.boxes.network;

import ml.boxes.network.packets.PacketUpdateData;
import ml.core.network.MLPacketHandler;

public class PacketHandler extends MLPacketHandler {
	public PacketHandler() {
		addHandler(PacketUpdateData.class);
	}
}
