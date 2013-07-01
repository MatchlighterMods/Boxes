package ml.boxes.item;

import java.util.List;

import ml.boxes.Boxes;
import ml.boxes.tile.safe.IItemMech;
import ml.boxes.tile.safe.MechCombo;
import ml.boxes.tile.safe.MechKey;
import ml.boxes.tile.safe.SafeMechanism;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class ItemMechs extends Item implements IItemMech {
	
	//Used for DRY internal mapping of Metadata to the SafeMechanism subclass
	private static BiMap<Integer, Class<? extends SafeMechanism>> ourMechs = HashBiMap.create();
	static {
		ourMechs.put(0, MechCombo.class);
		ourMechs.put(1, MechKey.class);
	}
	
	public ItemMechs(int par1) {
		super(par1);
		setMaxStackSize(1);
		setHasSubtypes(true);
		setCreativeTab(Boxes.BoxTab);
		
		//Register our Mechanisms now that we have an Item instance for association
		for (Class<? extends SafeMechanism> clazz : ourMechs.values()) {
			SafeMechanism.registerMechanism(this, clazz);
		}
	}
	
	@Override
	public void addInformation(ItemStack par1ItemStack,
			EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		super.addInformation(par1ItemStack, par2EntityPlayer, par3List, par4);
	}
	
	@Override
	public void registerIcons(IconRegister par1IconRegister) {
		
	}

	@Override
	public String getMechID(InventoryCrafting inv, ItemStack mechStack,
			ItemStack safeStack) {
		if (ourMechs.containsKey(mechStack.getItemDamage())){
			return ourMechs.get(mechStack.getItemDamage()).getName();
		}
		return "";
	}

}
