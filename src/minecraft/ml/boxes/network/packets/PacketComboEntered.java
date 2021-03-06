package ml.boxes.network.packets;

import java.io.IOException;

import ml.boxes.Boxes;
import ml.boxes.tile.TileEntitySafe;
import ml.boxes.tile.safe.MechCombo;
import ml.core.network.MLPacket;
import ml.core.util.StringUtils;
import ml.core.vec.BlockCoord;
import net.minecraft.entity.player.EntityPlayer;

import com.google.common.io.ByteArrayDataInput;

public class PacketComboEntered extends MLPacket {

	public @data BlockCoord teCoord;
	public @data String combo;
	
	public PacketComboEntered(EntityPlayer pl, TileEntitySafe tes, String combo) {
		super(Boxes.netChannel);
		chunkDataPacket = false;
		this.teCoord = new BlockCoord(tes);
		this.combo = combo;
	}
	
	public PacketComboEntered(EntityPlayer pl, TileEntitySafe tes, int[] combo) {
		this(pl, tes, StringUtils.join(combo, "-"));
	}
	
	public PacketComboEntered(EntityPlayer pl, ByteArrayDataInput data) {
		super(pl, data);
	}
	
	@Override
	public void handleClientSide(EntityPlayer epl) throws IOException {}

	@Override
	public void handleServerSide(EntityPlayer epl) throws IOException {
		TileEntitySafe tes = (TileEntitySafe)teCoord.getTileEntity(epl.worldObj);
		if (tes.mech instanceof MechCombo) {
			if (StringUtils.join(((MechCombo)tes.mech).getCombo(tes.getMechTag()), "-").equals(combo)) {
				tes.doUnlock();
				tes.playerOpened(epl);
			}
		}
	}
}
