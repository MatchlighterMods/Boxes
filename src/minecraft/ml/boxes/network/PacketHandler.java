package ml.boxes.network;

import ml.boxes.network.packets.PacketUpdateData;

public class PacketHandler extends ml.core.network.PacketHandler {
	public PacketHandler() {
		addHandler(PacketUpdateData.class);
	}
}
