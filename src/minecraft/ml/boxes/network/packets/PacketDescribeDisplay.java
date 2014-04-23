package ml.boxes.network.packets;

import java.io.IOException;

import ml.boxes.Boxes;
import ml.boxes.tile.TileEntityDisplayCase;
import ml.core.network.MLPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeDirection;

import com.google.common.io.ByteArrayDataInput;

public class PacketDescribeDisplay extends MLPacket {

	public @data TileEntityDisplayCase tedc;
	public @data ForgeDirection facing;
	public @data ForgeDirection rotation;
	
	public @data ItemStack is;
	
	public PacketDescribeDisplay(EntityPlayer pl, ByteArrayDataInput dataIn) {
		super(pl, dataIn);
	}	
	
	public PacketDescribeDisplay(TileEntityDisplayCase te) {
		super(Boxes.netChannel);
		
		this.tedc = te;
		this.facing = te.facing;
		this.rotation = te.rotation;
		this.is = te.getStackInSlot(0);
	}
	
	@Override
	public void handleClientSide(EntityPlayer epl) throws IOException {
		tedc.facing = facing;
		tedc.rotation = rotation;
		tedc.rItem = is;
	}
	
}
