package ml.boxes.network.packets;

import java.io.IOException;

import ml.boxes.data.ItemBoxContainer;
import ml.boxes.inventory.ContentTip;
import ml.boxes.item.ItemBox;
import ml.core.network.MLPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.network.Player;

public class PacketTipClick extends MLPacket {
	
	public @data int inventorySlot;
	public @data int boxInvSlot;
	public @data int arg;
	public @data int action;

	public PacketTipClick(Player pl, int slotWithBox, int slotInBox, int arg, int action) {
		super(pl, "Boxes");
		chunkDataPacket = false;
		
		inventorySlot = slotWithBox;
		boxInvSlot = slotInBox;
		this.arg = arg;
		this.action = action;
	}
	
	public PacketTipClick(Player pl, ByteArrayDataInput data) {
		super(pl, data);
	}

	@Override
	public void handleClientSide() throws IOException {}

	@Override
	public void handleServerSide() throws IOException {
		EntityPlayer asEntPl = (EntityPlayer)player;

		if (asEntPl.openContainer != null){
			Slot slotWithBox = asEntPl.openContainer.getSlot(inventorySlot);
			ItemStack isInSlot = slotWithBox.getStack();
			
			if (isInSlot != null && isInSlot.getItem() instanceof ItemBox) {
				ItemBoxContainer iib = new ItemBoxContainer(isInSlot);
				ContentTip ctip = iib.getBox().createContentTip(slotWithBox, null);
				
				ctip.slotClick(boxInvSlot, arg, action, asEntPl);
			}
		}
	}
}
