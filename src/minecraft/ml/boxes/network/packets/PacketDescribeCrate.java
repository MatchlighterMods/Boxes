package ml.boxes.network.packets;

import java.io.IOException;

import ml.boxes.tile.TileEntityCrate;
import ml.core.network.MLPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeDirection;

import com.google.common.io.ByteArrayDataInput;

public class PacketDescribeCrate extends MLPacket {

	public @data TileEntityCrate tec;
	public @data ForgeDirection facing;
	
	public @data boolean hasStack;
	public @data ItemStack is;
	public @data int itemCnt;
	
	public @data boolean upg_label;
	
	public PacketDescribeCrate(TileEntityCrate tec) {
		super("Boxes");
		
		this.tec = tec;
		this.facing = tec.facing;

		hasStack = tec.getStackInSlot(0) != null;
		is = hasStack ? tec.getStackInSlot(0).copy() : null;
		//is.stackSize = 1;
		itemCnt = tec.getTotalItems();
		upg_label = tec.upg_label;
		
	}
	
	public PacketDescribeCrate(EntityPlayer pl, ByteArrayDataInput data) {
		super(pl, data);
	}
	
	@Override
	public void handleServerSide(EntityPlayer epl) throws IOException {}

	@Override
	public void handleClientSide(EntityPlayer epl) throws IOException {
		tec.facing = facing;
		tec.cItem = hasStack ? is : null;
		tec.itemCount = itemCnt;
		
		tec.upg_label = upg_label;
		
		tec.updateClientDetails();
	}

}
