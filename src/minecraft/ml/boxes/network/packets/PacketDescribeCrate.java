package ml.boxes.network.packets;

import java.io.IOException;

import ml.boxes.tile.TileEntityCrate;
import ml.core.network.PacketDescribe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.network.Player;

public class PacketDescribeCrate extends PacketDescribe {

	public boolean hasStack;
	public ItemStack is;
	public int itemCnt;
	
	public boolean upg_label;
	
	public PacketDescribeCrate(TileEntityCrate tec) {
		super(tec, "Boxes");

		hasStack = tec.getStackInSlot(0) != null;
		is = hasStack ? tec.getStackInSlot(0).copy() : new ItemStack(0, 0, 0);
		is.stackSize = 1;
		itemCnt = tec.getTotalItems();
		upg_label = tec.upg_label;
		
		writeBoolean(hasStack);
		writeNBTTagCompound(is.writeToNBT(new NBTTagCompound()));
		writeInt(itemCnt);
		
		writeBoolean(upg_label);
	}
	
	public PacketDescribeCrate(Player pl, ByteArrayDataInput data) {
		super(pl, data);

		try {
			hasStack = dataIn.readBoolean();
			is = ItemStack.loadItemStackFromNBT(readNBTTagCompound());
			itemCnt = dataIn.readInt();
			
			upg_label = dataIn.readBoolean();
		} catch (IOException e){
			
		}
	}
	
	@Override
	public void handleServerSide() throws IOException {
		
	}

	@Override
	public void handleClientSide(TileEntity te) throws IOException {
		if (te instanceof TileEntityCrate){
			TileEntityCrate tec = (TileEntityCrate)te;
			tec.cItem = hasStack ? is : null;
			tec.itemCount = itemCnt;
			
			tec.upg_label = upg_label;
			
			tec.updateClientDetails();
		}
	}

}
