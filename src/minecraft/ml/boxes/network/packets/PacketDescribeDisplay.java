package ml.boxes.network.packets;

import java.io.IOException;

import com.google.common.io.ByteArrayDataInput;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.ForgeDirection;
import ml.boxes.network.PacketHandler;
import ml.boxes.tile.TileEntityDisplayCase;
import ml.core.network.MLPacket;

public class PacketDescribeDisplay extends MLPacket {

	public @data TileEntityDisplayCase tedc;
	public @data ForgeDirection facing;
	public @data ForgeDirection rotation;
	
	public PacketDescribeDisplay(EntityPlayer pl, ByteArrayDataInput dataIn) {
		super(pl, dataIn);
	}	
	
	public PacketDescribeDisplay(TileEntityDisplayCase te) {
		super(PacketHandler.defChan);
		
		this.tedc = te;
		this.facing = te.facing;
		this.rotation = te.rotation;
	}
	
	@Override
	public void handleClientSide(EntityPlayer epl) throws IOException {
		tedc.facing = facing;
		tedc.rotation = rotation;
	}
	
}
