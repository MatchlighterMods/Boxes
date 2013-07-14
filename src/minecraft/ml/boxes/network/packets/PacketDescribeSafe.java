package ml.boxes.network.packets;

import java.io.IOException;

import ml.boxes.tile.TileEntitySafe;
import ml.core.network.MLPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeDirection;

import com.google.common.io.ByteArrayDataInput;

public class PacketDescribeSafe extends MLPacket {
	
	public @data TileEntitySafe tes;
	public @data ForgeDirection facing;
	public @data ForgeDirection linkDir;
	
	public @data boolean sUnlocked;
	public @data NBTTagCompound mechData;
	
	public PacketDescribeSafe(TileEntitySafe tes) {
		super("Boxes");
		
		this.tes = tes;
		this.facing = tes.facing;
		this.linkDir = tes.linkedDir;
		
		sUnlocked = tes.unlocked;
		mechData = tes.mech.writeNBTPacket();
	}
	
	public PacketDescribeSafe(EntityPlayer pl, ByteArrayDataInput data) throws IOException {
		super(pl, data);
	}
	
	@Override
	public void handleClientSide(EntityPlayer epl) throws IOException {

		tes.facing = facing;
		tes.linkedDir = linkDir;
		
		tes.unlocked = sUnlocked;
		tes.mech.loadNBT(mechData);
	}

	@Override
	public void handleServerSide(EntityPlayer epl) throws IOException {}
	
}
