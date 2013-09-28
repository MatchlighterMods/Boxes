package ml.boxes.item;

import java.util.List;

import ml.boxes.Boxes;
import ml.boxes.api.safe.SafeMechanism;
import ml.boxes.tile.safe.IItemMech;
import ml.boxes.tile.safe.MechCombo;
import ml.boxes.tile.safe.MechKey;
import ml.core.ChatUtils;
import ml.core.StringUtils;
import ml.core.item.StackUtils;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class ItemMechs extends Item implements IItemMech {
	
	//Used for DRY internal mapping of Metadata to the SafeMechanism subclass
	private static BiMap<Integer, Class<? extends SafeMechanism>> ourMechs = HashBiMap.create();
	static {
		ourMechs.put(0, MechCombo.class);
		ourMechs.put(1, MechKey.class);
	}
	
	public static int metaFroMech(Class<?extends SafeMechanism> cls) {
		return ourMechs.inverse().get(cls);
	}
	
	public ItemMechs(int par1) {
		super(par1);
		setMaxStackSize(1);
		setHasSubtypes(true);
		setCreativeTab(Boxes.BoxTab);
		
		//Register our Mechanisms now that we have an Item instance for association
		for (Class<? extends SafeMechanism> clazz : ourMechs.values()) {
			//SafeMechanism.registerMechanism(clazz);
		}
	}
	
	@Override
	public void addInformation(ItemStack is, EntityPlayer par2EntityPlayer, List lst, boolean par4) {
		super.addInformation(is, par2EntityPlayer, lst, par4);
		NBTTagCompound tag = StackUtils.getStackTag(is);
		switch (is.getItemDamage()) {
		case 0:
			MechCombo.getISInfo(is, lst);
			break;
		case 1:
			MechKey.getISInfo(is, lst);
			break;
		}
	}
	
	@Override
	public String getUnlocalizedName(ItemStack is) {
		switch (is.getItemDamage()) {
		case 0:
			return "item.mechanism.combo";
		case 1:
			return "item.mechanism.key";
		}
		return "";
	}
	
	@Override
	public void registerIcons(IconRegister par1IconRegister) {
		
	}

	@Override
	public String getMechID(ItemStack mechStack) {
		if (ourMechs.containsKey(mechStack.getItemDamage())){
			return ourMechs.get(mechStack.getItemDamage()).getName();
		}
		return null;
	}

}
