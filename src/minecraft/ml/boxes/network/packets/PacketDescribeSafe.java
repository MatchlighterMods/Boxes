package ml.boxes.network.packets;

import java.io.IOException;

import ml.boxes.tile.TileEntitySafe;
import ml.core.network.PacketDescribe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.network.Player;

public class PacketDescribeSafe extends PacketDescribe {

	public PacketDescribeSafe(TileEntitySafe tes) {
		super(tes);
		
	}
	
	public PacketDescribeSafe(Player pl, ByteArrayDataInput data) {
		super(pl, data);

	}
	
	@Override
	public void handleClientSide(TileEntity te) throws IOException {
		// TODO Auto-generated method stub

	}

}
