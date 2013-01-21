package ml.boxes;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class BoxData implements IInventory {

	private ItemStack[] inventory;
	public String boxName = "";
	//public int boxColor = 2;
	
	public BoxData() {
		inventory=new ItemStack[this.getSizeInventory()];
	}
	
	public BoxData(NBTTagCompound data){
		//this();
		boxName = data.getString("name");
		//boxColor = data.getInteger("color");
		
		NBTTagList nbttaglist = data.getTagList("Items");
        inventory = new ItemStack[getSizeInventory()];
        for (int i = 0; i < nbttaglist.tagCount(); i++)
        {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist.tagAt(i);
            int j = nbttagcompound1.getByte("Slot") & 0xff;
            if (j >= 0 && j < inventory.length)
            {
            	inventory[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }
	}
	
	public NBTTagCompound asNBTTag(){
		NBTTagCompound tag = new NBTTagCompound();
		tag.setString("name", boxName);
		//tag.setInteger("color", boxColor);
		
		NBTTagList nbttaglist = new NBTTagList();
        for (int i = 0; i < inventory.length; i++)
        {
            if (inventory[i] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte) i);
                inventory[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }
        
        tag.setTag("Items", nbttaglist);
		
		return tag;
	}
	
//	public int getColor(){
//		return ItemDye.dyeColors[boxColor];
//	}
	
//	public String getBoxDisplayName() {
//		String nameBuild = "";
//		if (boxColor>-1){
//			nameBuild += StringTranslate.getInstance().translateKey("item.fireworksCharge." + ItemDye.dyeColorNames[boxColor]) + " ";
//		}
//		nameBuild += LanguageRegistry.instance().getStringLocalization("item.box.name", "en_US");
//		return nameBuild;
//	}
	
	public List<ItemStack> getContainedItemStacks(){
		List<ItemStack> iStacks = new ArrayList<ItemStack>();
		for (int i=0; i<getSizeInventory(); i++){
			if (inventory[i] != null)
				iStacks.add(inventory[i]);
		}
		return iStacks;
	}
			
	@Override
	public int getSizeInventory() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ItemStack getStackInSlot(int var1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemStack decrStackSize(int var1, int var2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int var1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setInventorySlotContents(int var1, ItemStack var2) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getInvName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getInventoryStackLimit() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void onInventoryChanged() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer var1) {
		return true;
	}

	@Override
	public void openChest() {
		// TODO Auto-generated method stub

	}

	@Override
	public void closeChest() {
		// TODO Auto-generated method stub

	}

}
