package ml.boxes.item;

import java.util.List;

import ml.core.item.StackUtils;
import ml.core.util.RandomUtils;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class ItemKey extends Item {

	protected Icon keyIcon;
	protected Icon keyColorIcon;
	
	public ItemKey(int par1) {
		super(par1);
		setContainerItem(this);
		setUnlocalizedName("key");
		setMaxStackSize(1);
	}
	
	@Override
	public ItemStack getContainerItemStack(ItemStack itemStack) {
		return itemStack;
	}

	@Override
	public boolean requiresMultipleRenderPasses() {
		return true;
	}
	
	@Override
	public int getRenderPasses(int metadata) {
		return 2;
	}
	
	@Override
	public void onCreated(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
		if (par2World.isRemote) return;
		checkIDd(par1ItemStack);
	}
	
	@Override
	public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) {
		checkIDd(par1ItemStack);
	}
	
	public static void checkIDd(ItemStack is) {
		if (!StackUtils.hasTagAt(is, "key_id"))
			StackUtils.setTag(is, RandomUtils.randomInt(1, 32000), "key_id");
	}
	
	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List lst, boolean par4) {
		int key_id = StackUtils.getTag(par1ItemStack, -1, "key_id");
		if (key_id > -1) {
			lst.add("Id: " + EnumChatFormatting.WHITE + key_id);
		}
	}
	
	@Override
	public void registerIcons(IconRegister par1IconRegister) {
		keyIcon = par1IconRegister.registerIcon("Boxes:key_gold");
		keyColorIcon = par1IconRegister.registerIcon("Boxes:key_color");
	}
	
	@Override
	public Icon getIcon(ItemStack stack, int pass) {
		return (pass>0 && StackUtils.hasTagAt(stack, "color")) ? keyColorIcon : keyIcon;
	}
	
	@Override
	public int getColorFromItemStack(ItemStack stack, int pass) {
		return (pass>0 && StackUtils.hasTagAt(stack, "color")) ? StackUtils.getTag(stack, 0x000000, "color") : 0xFFFFFF;
	}
}
