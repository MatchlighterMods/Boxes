package ml.boxes.network.packets;

import java.io.IOException;

import ml.boxes.Boxes;
import ml.boxes.data.BoxData;
import ml.boxes.data.ItemIBox;
import ml.boxes.item.ItemBox;
import ml.core.network.MLPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.network.Player;

public class PacketTipClick extends MLPacket {
	
	public final int inventorySlot;
	public final int boxInvSlot;

	public PacketTipClick(Player pl, int slotWithBox, int slotInBox, int arg, int action) {
		super(pl);
		chunkDataPacket = false;
		
		inventorySlot = slotWithBox;
		boxInvSlot = slotInBox;
		
		writeInt(inventorySlot);
		writeInt(boxInvSlot);
	}
	
	public PacketTipClick(Player pl, ByteArrayDataInput data) {
		super(pl, data);
		
		inventorySlot = data.readInt();
		boxInvSlot = data.readInt();
	}

	@Override
	public void handleClientSide() throws IOException {}

	@Override
	public void handleServerSide() throws IOException {
		EntityPlayer asEntPl = (EntityPlayer)player;

		ItemStack isInSlot = asEntPl.inventory.getStackInSlot(inventorySlot);
		if (isInSlot != null && isInSlot.getItem().itemID == Boxes.BlockBox.blockID) {
			ItemIBox iib = new ItemIBox(isInSlot);
			if (asEntPl.inventory.getItemStack() == null || iib.getBoxData().ISAllowedInBox(asEntPl.inventory.getItemStack())){
				ItemStack isInBox = iib.getBoxData().getStackInSlot(boxInvSlot);
				iib.getBoxData().setInventorySlotContents(boxInvSlot, asEntPl.inventory.getItemStack());
				iib.saveData();
				asEntPl.inventory.setItemStack(isInBox);
				asEntPl.inventory.setInventorySlotContents(inventorySlot, isInSlot);
			}
		}
	}
}
