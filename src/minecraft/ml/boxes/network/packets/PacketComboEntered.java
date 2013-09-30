package ml.boxes.network.packets;

import java.io.IOException;
import java.util.Arrays;

import ml.boxes.tile.TileEntitySafe;
import ml.boxes.tile.safe.MechCombo;
import ml.core.network.MLPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

import com.google.common.io.ByteArrayDataInput;

public class PacketComboEntered extends MLPacket {

	public @data int tex;
	public @data int tey;
	public @data int tez;
	
	public int[] combo;
	
	public PacketComboEntered(EntityPlayer pl, TileEntitySafe tes, int[] combo) {
		super("Boxes");
		chunkDataPacket = false;
		
		tex = tes.xCoord;
		tey = tes.yCoord;
		tez = tes.zCoord;
		
		this.combo = combo;
		
//		writeInt(combo.length);
//		for (int i : combo){
//			writeInt(i);
//		}
	}
	
	public PacketComboEntered(EntityPlayer pl, ByteArrayDataInput data) {
		super(pl, data);
		
//		int len = data.readInt();
//		combo = new int[len];
//		for (int i=0; i<len; i++){
//			combo[i] = data.readInt();
//		}
	}
	
	@Override
	public void handleClientSide(EntityPlayer epl) throws IOException {}

	@Override
	public void handleServerSide(EntityPlayer epl) throws IOException {
		TileEntity te = epl.worldObj.getBlockTileEntity(tex, tey, tez);
		if (te instanceof TileEntitySafe) {
			TileEntitySafe tes = (TileEntitySafe)te;
			if (tes.mech instanceof MechCombo && Arrays.equals(tes.getMechTag().getIntArray(MechCombo.comboTagName), combo)) {
				tes.doUnlock();
				tes.playerOpened(epl);
			}
		}
	}
}
