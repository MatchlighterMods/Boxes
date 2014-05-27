package ml.boxes.network.packets;

import java.io.IOException;

import ml.boxes.Boxes;
import ml.boxes.tile.TileEntityCrate;
import ml.core.network.MLPacket;
import ml.core.vec.BlockCoord;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeDirection;

import com.google.common.io.ByteArrayDataInput;

public class PacketDescribeCrate extends MLPacket {

	public @data BlockCoord teCoord;
	public @data ForgeDirection facing;
	
	public @data ItemStack is;
	public @data int itemCnt;
	
	public @data boolean upg_label;
	
	public PacketDescribeCrate(TileEntityCrate tec) {
		super(Boxes.netChannel);
		
		this.teCoord = new BlockCoord(tec);
		this.facing = tec.facing;

		is = tec.getStackInSlot(0) != null ? tec.getStackInSlot(0).copy() : null;
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
		TileEntityCrate tec = (TileEntityCrate)teCoord.getTileEntity(epl.worldObj);
		tec.facing = facing;
		tec.cItem = is;
		tec.itemCount = itemCnt;
		
		tec.upg_label = upg_label;
		
		tec.updateClientDetails();
	}

}
