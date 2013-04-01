package ml.boxes.network.packets;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import ml.boxes.data.BoxData;
import ml.boxes.tile.TileEntityBox;
import ml.core.network.MLPacket;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.network.Player;

public class PacketUpdateData extends MLPacket {

	public Integer x;
	public Integer y;
	public Integer z;
	public NBTTagCompound pktData;
	
	public PacketUpdateData(TileEntityBox te) {
		super(null);
		
		x = te.xCoord;
		y = te.yCoord;
		z = te.zCoord;
		pktData = te.getBoxData().asNBTTag();
		
		writeInt(x);
		writeInt(y);
		writeInt(z);
		writeNBTTagCompound(pktData);
	}
	
	public PacketUpdateData(Player pl, ByteArrayDataInput data) {
		super(pl, data);
		try {
			x = dataIn.readInt();
			y = dataIn.readInt();
			z = dataIn.readInt();
			pktData = readNBTTagCompound();
		} catch (IOException e){
			
		}
	}

	@Override
	public void handleClientSide() throws IOException {
		EntityPlayer asEntPl = (EntityPlayer)player;
		TileEntity te = asEntPl.worldObj.getBlockTileEntity(x, y, z);
		
		if (te instanceof TileEntityBox){
			((TileEntityBox)te).getBoxData().loadNBT(pktData);
		}
	}

	@Override
	public void handleServerSide() throws IOException {}

}
