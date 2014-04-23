package ml.boxes.recipe;

import java.util.List;

import ml.boxes.Registry;
import ml.core.inventory.InventoryUtils;
import ml.core.item.StackUtils;
import ml.core.item.recipe.RecipeMixed;
import ml.core.util.DyeUtils;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class RecipeKey extends RecipeMixed {

	public RecipeKey(Object... recipe) {
		super(new ItemStack(Registry.itemKey, 1, OreDictionary.WILDCARD_VALUE), recipe);
	}
	
	@Override
	public void addShapelessItemsToNEI(List<Object[]> lst) {
		lst.add(new Object[]{
			new ItemStack(Registry.itemKey)
		});
		lst.add(new Object[]{DyeUtils.getAllDyeStacks()});
	}

	@Override
	public boolean shapelessItemsValid(InventoryCrafting inv,
			List<ItemStack> items) {
		
		List<ItemStack> keys = InventoryUtils.findItems(inv, Registry.itemKey);
		if (keys.size()>1) return false;
		
		for (ItemStack is : items) {
			if (is.getItem() != Registry.itemKey &&
				!DyeUtils.isDye(is)) {
				return false;
			}
		}
		
		return true;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inventorycrafting) {
		ItemStack keyStack = new ItemStack(Registry.itemKey);
		
		ItemStack dkey = InventoryUtils.findItem(inventorycrafting, Registry.itemKey);
		if (dkey != null) {
			StackUtils.setTag(keyStack, StackUtils.getTag(dkey, null, "key_id"), "key_id");
		}
		
		List<ItemStack> dyes = InventoryUtils.findItems(inventorycrafting, Item.dyePowder);
		if (dyes.size() > 0) {
			StackUtils.setTag(keyStack, DyeUtils.mixDyeColors(dyes), "color");
		}
		
		return keyStack;
	}
}
