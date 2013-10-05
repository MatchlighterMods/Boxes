package ml.boxes.item;

import java.util.List;

import ml.boxes.Boxes;
import ml.boxes.api.safe.IItemMech;
import ml.boxes.api.safe.SafeMechanism;
import ml.boxes.tile.safe.MechCombo;
import ml.boxes.tile.safe.MechKey;
import ml.boxes.tile.safe.MechRegistry;
import ml.core.item.StackUtils;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class ItemMechs extends Item implements IItemMech {
	
	//Used for DRY internal mapping of Metadata to the SafeMechanism subclass
	private static BiMap<Integer, SafeMechanism> ourMechs = HashBiMap.create();
	static {
		ourMechs.put(0, new MechCombo());
		ourMechs.put(1, new MechKey());
		
		for (SafeMechanism m : ourMechs.values()) {
			MechRegistry.registerMech(m.getMechId(), m);
		}
	}
	
	public static int metaForMech(Class<?extends SafeMechanism> cls) {
		for (SafeMechanism sm : ourMechs.values()) {
			if (cls.isInstance(sm))
				return ourMechs.inverse().get(sm);
		}
		return -1;
	}
	
	public ItemMechs(int par1) {
		super(par1);
		setMaxStackSize(1);
		setHasSubtypes(true);
		setCreativeTab(Boxes.BoxTab);
	}
	
	@Override
	public void addInformation(ItemStack is, EntityPlayer par2EntityPlayer, List lst, boolean par4) {
		super.addInformation(is, par2EntityPlayer, lst, par4);
		NBTTagCompound tag = StackUtils.getStackTag(is);
		
		switch (is.getItemDamage()) {
		case 0:
			MechCombo.addInfo(tag, lst);
			break;
		case 1:
			MechKey.addInfo(tag, lst);
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
			return ourMechs.get(mechStack.getItemDamage()).getMechId();
		}
		return null;
	}

}
