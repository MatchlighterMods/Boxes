package ml.boxes.network.packets;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

import ml.boxes.BoxData;
import ml.boxes.TileEntityBox;
import ml.core.network.MLPacket;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.network.Player;

public class PacketUpdateData extends MLPacket {

	public Integer x;
	public Integer y;
	public Integer z;
	public BoxData pktData;
	
	public PacketUpdateData(TileEntityBox te) {
		super(null);
		
		x = te.xCoord;
		y = te.yCoord;
		z = te.zCoord;
		pktData = te.data;
		
		writeInt(x);
		writeInt(y);
		writeInt(z);
		writeInt(pktData.boxColor);
		writeString(pktData.boxName);
	}
	
	public PacketUpdateData(Player pl, ByteArrayDataInput data) {
		super(pl, data);
		pktData = new BoxData();
		try {
			x = dataIn.readInt();
			y = dataIn.readInt();
			z = dataIn.readInt();
			pktData.boxColor = dataIn.readInt();
			pktData.boxName = readString(200);
		} catch (IOException e){
			
		}
	}

	@Override
	public void handleClientSide() throws IOException {
		EntityPlayer asEntPl = (EntityPlayer)player;
		TileEntity te = asEntPl.worldObj.getBlockTileEntity(x, y, z);
		
		if (te instanceof TileEntityBox){
			((TileEntityBox)te).data = pktData;
		}
	}

	@Override
	public void handleServerSide() throws IOException {}

}
