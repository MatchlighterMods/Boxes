package ml.boxes.network.packets;

import java.io.IOException;

import ml.boxes.Boxes;
import ml.boxes.tile.TileEntityAbstractBox;
import ml.core.network.MLPacket;
import ml.core.vec.BlockCoord;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import com.google.common.io.ByteArrayDataInput;

public class PacketUpdateData extends MLPacket {

	public @data BlockCoord teCoord;
	public @data NBTTagCompound pktData;
	
	public PacketUpdateData(TileEntityAbstractBox te) {
		super(Boxes.netChannel);

		this.teCoord = new BlockCoord(te);
		pktData = te.getBox().asNBTTag();
	}
	
	public PacketUpdateData(EntityPlayer pl, ByteArrayDataInput data) {
		super(pl, data);

	}

	@Override
	public void handleClientSide(EntityPlayer epl) throws IOException {
		TileEntityAbstractBox teb = (TileEntityAbstractBox)teCoord.getTileEntity(epl.worldObj);
		teb.getBox().loadNBT(pktData);
	}

	@Override
	public void handleServerSide(EntityPlayer epl) throws IOException {}

}
