package ml.boxes.network.packets;

import java.io.IOException;

import ml.boxes.BoxData;
import ml.boxes.Boxes;
import ml.boxes.item.ItemBox;
import ml.core.network.MLPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.network.Player;

public class PacketTipClick extends MLPacket {
	
	public final Integer inventorySlot;
	public final Integer boxInvSlot;

	public PacketTipClick(Player pl, Integer invSlot, Integer boxSlot) {
		super(pl);
		
		inventorySlot = invSlot;
		boxInvSlot = boxSlot;
		
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
		
		ItemStack isInSlot = asEntPl.inventory.mainInventory[inventorySlot];
		if (isInSlot != null && isInSlot.getItem().itemID == Boxes.BlockBox.blockID) {
			BoxData bd  = ItemBox.getDataFromIS(isInSlot);
			ItemStack isInBox = bd.getStackInSlot(boxInvSlot);
			bd.setInventorySlotContents(boxInvSlot, asEntPl.inventory.getItemStack());
			asEntPl.inventory.setItemStack(isInBox);
		}
	}
}
