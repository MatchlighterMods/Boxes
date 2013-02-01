package ml.boxes.network;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet250CustomPayload;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.network.Player;

public abstract class MLPacket {
	
	protected Player player;
	protected Integer packetID;
	
	// Incoming
	protected ByteArrayDataInput dataIn;
	
	public MLPacket(Player pl, ByteArrayDataInput data) {
		dataIn = data;
		player = pl;
	}
	
	public abstract void handleClientSide() throws IOException;
	
	public abstract void handleServerSide() throws IOException;
	
	// Outgoing
	protected ByteArrayOutputStream dataOutRaw;
	protected DataOutputStream dataOut;
	protected boolean chunkDataPacket = true;
	protected String channel;
	
	public MLPacket(Player pl) {
		player = pl;
		dataOutRaw = new ByteArrayOutputStream();
		dataOut = new DataOutputStream(dataOutRaw);
		packetID = PacketHandler.findPacketId(this.getClass());
		writeInt(packetID);
	}
	
	public Packet250CustomPayload convertToPkt250(){
		Packet250CustomPayload mcPkt = new Packet250CustomPayload("Boxes", dataOutRaw.toByteArray());
		mcPkt.isChunkDataPacket = chunkDataPacket;
		return mcPkt;
	}
	
	// Readers
	
//	public Integer readInt() throws IOException
//    {
//		return dataIn.readInt();
//    }
//    
//    public Double readDouble() throws IOException
//    {
//    	return dataIn.readDouble();
//    }
//    
//    public Boolean readBoolean() throws IOException
//    {
//    	return dataIn.readBoolean();
//    }
//    
//    public Byte readByte() throws IOException
//    {
//    	return dataIn.readByte();
//    }
	
    public ItemStack readItemStack() throws IOException
    {
        ItemStack var1 = null;
        short var2 = dataIn.readShort();

        if (var2 >= 0)
        {
            byte var3 = dataIn.readByte();
            short var4 = dataIn.readShort();
            var1 = new ItemStack(var2, var3, var4);
            var1.stackTagCompound = readNBTTagCompound();
        }

        return var1;
    }

    public NBTTagCompound readNBTTagCompound() throws IOException
    {
        short var1 = dataIn.readShort();

        if (var1 < 0)
        {
            return null;
        }
        else
        {
            byte[] var2 = new byte[var1];
            dataIn.readFully(var2);
            return CompressedStreamTools.decompress(var2);
        }
    }
    
    public String readString(Integer maxLength) throws IOException
    {
    	short var2 = dataIn.readShort();

    	if (var2 > maxLength){
    		throw new IOException("Received string length longer than maximum allowed (" + var2 + " > " + maxLength + ")");
    	}else if (var2 < 0){
    		throw new IOException("Received string length is less than zero! Weird string!");
    	}else{
    		StringBuilder var3 = new StringBuilder();

    		for (int var4 = 0; var4 < var2; ++var4){
    			var3.append(dataIn.readChar());
    		}

    		return var3.toString();
    	}
    }
    
    // Writers
    
    public void writeInt(Integer i)
    {
    	try {
			dataOut.writeInt(i);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public void writeDouble(Double d)
    {
    	try {
			dataOut.writeDouble(d);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public void writeBoolean(Boolean b)
    {
    	try {
    		dataOut.writeBoolean(b);
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }

    public void writeByte(Byte b)
    {
    	try {
    		dataOut.writeByte(b);
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }

    public void writeItemStack(ItemStack par0ItemStack)
    {
    	try {
    		if (par0ItemStack == null)
    		{
    			dataOut.writeShort(-1);
    		}
    		else
    		{
    			dataOut.writeShort(par0ItemStack.itemID);
    			dataOut.writeByte(par0ItemStack.stackSize);
    			dataOut.writeShort(par0ItemStack.getItemDamage());
    			NBTTagCompound var2 = null;

    			if (par0ItemStack.getItem().isDamageable() || par0ItemStack.getItem().getShareTag())
    			{
    				var2 = par0ItemStack.stackTagCompound;
    			}

    			writeNBTTagCompound(var2);
    		}
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }

    public void writeNBTTagCompound(NBTTagCompound par0NBTTagCompound)
    {
    	try {
    		if (par0NBTTagCompound == null)
    		{
    			dataOut.writeShort(-1);
    		}
    		else
    		{
    			byte[] var2 = CompressedStreamTools.compress(par0NBTTagCompound);
    			dataOut.writeShort((short)var2.length);
    			dataOut.write(var2);
    		}
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }

    public void writeString(String par0Str)
    {
    	try {
    		if (par0Str.length() > 32767){
    			throw new IOException("String too big");
    		}else{
    			dataOut.writeShort(par0Str.length());
    			dataOut.writeChars(par0Str);
    		}
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }
    
}
