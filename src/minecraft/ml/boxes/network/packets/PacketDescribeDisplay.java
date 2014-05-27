package ml.boxes.network.packets;

import java.io.IOException;

import ml.boxes.Boxes;
import ml.boxes.tile.TileEntityDisplayCase;
import ml.core.network.MLPacket;
import ml.core.vec.BlockCoord;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeDirection;

import com.google.common.io.ByteArrayDataInput;

public class PacketDescribeDisplay extends MLPacket {

	public @data BlockCoord teCoord;
	public @data ForgeDirection facing;
	public @data ForgeDirection rotation;
	
	public @data ItemStack is;
	
	public PacketDescribeDisplay(EntityPlayer pl, ByteArrayDataInput dataIn) {
		super(pl, dataIn);
	}	
	
	public PacketDescribeDisplay(TileEntityDisplayCase te) {
		super(Boxes.netChannel);
		
		this.teCoord = new BlockCoord(te);
		this.facing = te.facing;
		this.rotation = te.rotation;
		this.is = te.getStackInSlot(0);
	}
	
	@Override
	public void handleClientSide(EntityPlayer epl) throws IOException {
		TileEntityDisplayCase tedc = (TileEntityDisplayCase)teCoord.getTileEntity(epl.worldObj);
		tedc.facing = facing;
		tedc.rotation = rotation;
		tedc.rItem = is;
	}
	
}
