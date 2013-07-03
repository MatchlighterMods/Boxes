package ml.boxes.network.packets;

import java.io.IOException;
import java.util.Arrays;

import ml.boxes.tile.TileEntitySafe;
import ml.boxes.tile.safe.MechCombo;
import ml.core.network.MLPacket;
import ml.core.network.MLPacket.data;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.network.Player;

public class PacketComboEntered extends MLPacket {

	public @data int tex;
	public @data int tey;
	public @data int tez;
	
	public int[] combo;
	
	public PacketComboEntered(Player pl, TileEntitySafe tes, int[] combo) {
		super(pl, "Boxes");
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
	
	public PacketComboEntered(Player pl, ByteArrayDataInput data) {
		super(pl, data);
		
//		int len = data.readInt();
//		combo = new int[len];
//		for (int i=0; i<len; i++){
//			combo[i] = data.readInt();
//		}
	}
	
	@Override
	public void handleClientSide() throws IOException {}

	@Override
	public void handleServerSide() throws IOException {
		EntityPlayer asEntPl = (EntityPlayer)player;
		TileEntity te = asEntPl.worldObj.getBlockTileEntity(tex, tey, tez);
		if (te instanceof TileEntitySafe) {
			TileEntitySafe tes = (TileEntitySafe)te;
			if (tes.mech instanceof MechCombo && Arrays.equals(((MechCombo)tes.mech).combination, combo)) {
				tes.unlock();
				tes.playerOpened(asEntPl);
			}
		}
	}
}
