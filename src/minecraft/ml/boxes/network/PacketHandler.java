package ml.boxes.network;

import java.util.logging.Level;

import ml.boxes.network.packets.PacketDescribeCrate;
import ml.boxes.network.packets.PacketDescribeSafe;
import ml.boxes.network.packets.PacketTipClick;
import ml.boxes.network.packets.PacketUpdateData;
import ml.core.network.MLPacket;
import cpw.mods.fml.common.FMLLog;

public class PacketHandler extends ml.core.network.PacketHandler {
	public PacketHandler() {
		addHandler(PacketUpdateData.class);
		addHandler(PacketTipClick.class);
		addHandler(PacketDescribeCrate.class);
		addHandler(PacketDescribeSafe.class);
	}

	@Override
	protected void onError(Throwable e, MLPacket mlPkt) {
		FMLLog.log(Level.SEVERE, e, "Boxes failed to process packet");
	}
}
