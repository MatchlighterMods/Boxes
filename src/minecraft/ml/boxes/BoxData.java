package ml.boxes;

import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StringTranslate;

public class BoxData implements IInventory {

	private ItemStack[] inventory;
	//public int boxColor = 2;
	
	public BoxData() {
		inventory=new ItemStack[this.getSizeInventory()];
	}
	
	public BoxData(NBTTagCompound data){
		this();
		//boxColor = data.getInteger("color");
	}
	
	public NBTTagCompound asNBTTag(){
		NBTTagCompound tag = new NBTTagCompound();
		//tag.setInteger("color", boxColor);
		
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
		// TODO Auto-generated method stub
		return false;
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
