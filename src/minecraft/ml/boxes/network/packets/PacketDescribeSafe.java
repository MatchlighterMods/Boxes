package ml.boxes.network.packets;

import java.io.IOException;

import ml.boxes.tile.TileEntitySafe;
import ml.core.network.PacketDescribe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.network.Player;

public class PacketDescribeSafe extends PacketDescribe {

	ForgeDirection linkDir;
	
	public PacketDescribeSafe(TileEntitySafe tes) {
		super(tes);
		linkDir = tes.linkedDir;
		
		writeInt(linkDir.ordinal());
	}
	
	public PacketDescribeSafe(Player pl, ByteArrayDataInput data) {
		super(pl, data);
		
		linkDir = ForgeDirection.getOrientation(data.readInt());
	}
	
	@Override
	public void handleClientSide(TileEntity te) throws IOException {
		TileEntitySafe tes = (TileEntitySafe)te;

		tes.linkedDir = linkDir;
	}

}
