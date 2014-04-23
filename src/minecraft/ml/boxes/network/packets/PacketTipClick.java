package ml.boxes.network.packets;

import java.io.IOException;

import ml.boxes.Boxes;
import ml.boxes.data.ItemBoxContainer;
import ml.boxes.inventory.ContentTip;
import ml.boxes.item.ItemBox;
import ml.core.network.MLPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.google.common.io.ByteArrayDataInput;

public class PacketTipClick extends MLPacket {
	
	public @data int inventorySlot;
	public @data int boxInvSlot;
	public @data int arg;
	public @data int action;

	public PacketTipClick(EntityPlayer pl, int slotWithBox, int slotInBox, int arg, int action) {
		super(Boxes.netChannel);
		chunkDataPacket = false;
		
		inventorySlot = slotWithBox;
		boxInvSlot = slotInBox;
		this.arg = arg;
		this.action = action;
	}
	
	public PacketTipClick(EntityPlayer pl, ByteArrayDataInput data) {
		super(pl, data);
	}

	@Override
	public void handleClientSide(EntityPlayer epl) throws IOException {}

	@Override
	public void handleServerSide(EntityPlayer epl) throws IOException {
		
		if (epl.openContainer != null){
			Slot slotWithBox = epl.openContainer.getSlot(inventorySlot);
			ItemStack isInSlot = slotWithBox.getStack();
			
			if (isInSlot != null && isInSlot.getItem() instanceof ItemBox) {
				ItemBoxContainer iib = new ItemBoxContainer(isInSlot);
				ContentTip ctip = iib.getBox().createContentTip(slotWithBox, null);
				
				ctip.slotClick(boxInvSlot, arg, action, epl);
			}
		}
	}
}
