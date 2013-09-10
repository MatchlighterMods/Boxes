package ml.boxes.network;

import ml.boxes.network.packets.PacketDescribeCrate;
import ml.boxes.network.packets.PacketDescribeDisplay;
import ml.boxes.network.packets.PacketDescribeSafe;
import ml.boxes.network.packets.PacketTipClick;
import ml.boxes.network.packets.PacketUpdateData;

public class PacketHandler extends ml.core.network.PacketHandler {
	
	public static final String defChan = "Boxes";
	
	public PacketHandler() {
		addHandler(PacketUpdateData.class);
		addHandler(PacketTipClick.class);
		
		addHandler(PacketDescribeCrate.class);
		addHandler(PacketDescribeSafe.class);
		addHandler(PacketDescribeSafe.PacketLockSafe.class);
		addHandler(PacketDescribeDisplay.class);
	}

}
