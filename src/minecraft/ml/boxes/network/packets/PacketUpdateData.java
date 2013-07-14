package ml.boxes.network.packets;

import java.io.IOException;

import ml.boxes.tile.TileEntityBox;
import ml.core.network.MLPacket;
import ml.core.network.MLPacket.data;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.network.Player;

public class PacketUpdateData extends MLPacket {

	public @data TileEntityBox teb;
	public @data NBTTagCompound pktData;
	
	public PacketUpdateData(TileEntityBox te) {
		super("Boxes");

		this.teb = te;
		pktData = te.getBox().asNBTTag();
	}
	
	public PacketUpdateData(EntityPlayer pl, ByteArrayDataInput data) {
		super(pl, data);

	}

	@Override
	public void handleClientSide(EntityPlayer epl) throws IOException {
		teb.getBox().loadNBT(pktData);
	}

	@Override
	public void handleServerSide(EntityPlayer epl) throws IOException {}

}
