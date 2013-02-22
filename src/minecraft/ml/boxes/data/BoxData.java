package ml.boxes.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ml.boxes.IBox;
import ml.boxes.api.ContentBlacklist;
import ml.boxes.item.ItemBox;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.ForgeDirection;

public class BoxData implements IInventory {

	private ItemStack[] inventory;
	public String boxName = "";
	public int boxColor = 5;
	public final IBox IBoxOwner;
	
	public BoxData(IBox owner) {
		inventory=new ItemStack[this.getSizeInventory()];
		IBoxOwner = owner;
	}
	
	public BoxData(NBTTagCompound data, IBox owner){
		IBoxOwner = owner;
		loadNBT(data);
	}
	
	public void loadNBT(NBTTagCompound nbt){
		boxName = nbt.getString("name");
		boxColor = nbt.getInteger("color");
		
		NBTTagList nbttaglist = nbt.getTagList("Items");
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
		tag.setInteger("color", boxColor);
		
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
	
	public boolean ISAllowedInBox(ItemStack is){
		if (is == null)
			return true;
		if (is.getItem() instanceof ItemBox || ContentBlacklist.ItemBlacklisted(is))
			return false;
		return true;
	}
	
	public List<ItemStack> getContainedItemStacks(){
		List<ItemStack> iStacks = new ArrayList<ItemStack>();
		for (int i=0; i<getSizeInventory(); i++){
			if (inventory[i] != null){
				iStacks.add(inventory[i]);
			}
		}
		return iStacks;
	}
			
	@Override
	public int getSizeInventory() {
		return 18;
	}

	@Override
	public ItemStack getStackInSlot(int var1) {
		return inventory[var1];
	}

	@Override
	public ItemStack decrStackSize(int slotIndex, int var2) {
        if (this.inventory[slotIndex] != null)
        {
            ItemStack var3;

            if (this.inventory[slotIndex].stackSize <= var2)
            {
                var3 = this.inventory[slotIndex];
                this.inventory[slotIndex] = null;
                this.onInventoryChanged();
                return var3;
            }
            else
            {
                var3 = this.inventory[slotIndex].splitStack(var2);

                if (this.inventory[slotIndex].stackSize == 0)
                {
                    this.inventory[slotIndex] = null;
                }

                this.onInventoryChanged();
                return var3;
            }
        }
        else
        {
            return null;
        }
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int var1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setInventorySlotContents(int var1, ItemStack var2) {
		if (ISAllowedInBox(var2)){
			inventory[var1] = var2;
			this.onInventoryChanged();
		}else{
			IBoxOwner.ejectItem(var2);
		}
	}

	@Override
	public String getInvName() {
		return "box";
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	public int pipeTransferIn(ItemStack stack, boolean doAdd, ForgeDirection from){
		return 0;
	}
	
	public ItemStack[] pipeExtract(boolean doRemove, ForgeDirection from, int maxItemCount){
		return null;
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
	public void openChest() {}

	@Override
	public void closeChest() {}

}
