package ml.boxes.api.safe;

import java.util.List;

import ml.core.item.StackUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * A really simple Item subclass that you can use when adding SafeMechanisms
 * @author Matchlighter
 */
public class ItemMechanism extends Item {

	public final SafeMechanism safeMechanism;
	
	public ItemMechanism(int par1, SafeMechanism safeMechanism) {
		super(par1);
		this.safeMechanism = safeMechanism;
	}

	@Override
	public void addInformation(ItemStack is, EntityPlayer par2EntityPlayer, List lst, boolean par4) {
		super.addInformation(is, par2EntityPlayer, lst, par4);
		NBTTagCompound tag = StackUtils.getStackTag(is);
		
		safeMechanism.addInfoForStack(tag, lst);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack is) {
		return safeMechanism.getUnlocalizedMechName()+".item";
	}
	
}
