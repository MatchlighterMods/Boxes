package ml.boxes.network.packets;

import java.io.IOException;

import ml.boxes.tile.TileEntitySafe;
import ml.core.network.PacketDescribeConnectable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.network.Player;

public class PacketDescribeSafe extends PacketDescribeConnectable {
	
	public boolean sUnlocked;
	public NBTTagCompound mechData;
	
	public PacketDescribeSafe(TileEntitySafe tes) {
		super(tes, "Boxes");
		
		sUnlocked = tes.unlocked;
		mechData = tes.mech.writeNBTPacket();
		
		writeBoolean(sUnlocked);
		writeNBTTagCompound(mechData);
	}
	
	public PacketDescribeSafe(Player pl, ByteArrayDataInput data) throws IOException {
		super(pl, data);
		
		sUnlocked = data.readBoolean();
		mechData = readNBTTagCompound();
	}
	
	@Override
	public void handleClientSide(TileEntity te) throws IOException {
		super.handleClientSide(te);
		TileEntitySafe tes = (TileEntitySafe)te;

		tes.unlocked = sUnlocked;
		tes.mech.loadNBT(mechData);
	}
}
