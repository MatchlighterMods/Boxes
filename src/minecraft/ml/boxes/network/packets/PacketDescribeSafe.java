package ml.boxes.network.packets;

import java.io.IOException;

import ml.boxes.tile.TileEntitySafe;
import ml.core.network.PacketDescribeConnectable;
import ml.core.network.MLPacket.data;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.network.Player;

public class PacketDescribeSafe extends PacketDescribeConnectable {
	
	public @data boolean sUnlocked;
	public @data NBTTagCompound mechData;
	
	public PacketDescribeSafe(TileEntitySafe tes) {
		super(tes, "Boxes");
		
		sUnlocked = tes.unlocked;
		mechData = tes.mech.writeNBTPacket();
	}
	
	public PacketDescribeSafe(Player pl, ByteArrayDataInput data) throws IOException {
		super(pl, data);
	}
	
	@Override
	public void handleClientSide(TileEntity te) throws IOException {
		super.handleClientSide(te);
		TileEntitySafe tes = (TileEntitySafe)te;

		tes.unlocked = sUnlocked;
		tes.mech.loadNBT(mechData);
	}
}
