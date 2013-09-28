package ml.boxes.recipe;

import java.util.List;

import ml.boxes.Registry;
import ml.core.inventory.InventoryUtils;
import ml.core.item.recipe.RecipeMixed;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class RecipeKey extends RecipeMixed {

	public RecipeKey(Object... recipe) {
		super(Registry.ItemKey, recipe);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean shapelessItemsValid(InventoryCrafting inv,
			List<ItemStack> items) {
		
		List<ItemStack> keys = InventoryUtils.findItems(inv, Registry.ItemKey);
		if (keys.size()>1) return false;
		
		for (ItemStack is : items) {
			if (is.getItem() != Registry.ItemKey &&
				is.getItem() != Item.dyePowder) {
				return false;
			}
		}
		
		return true;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inventorycrafting) {
		ItemStack keyStack = new ItemStack(Registry.ItemKey);
		
		ItemStack dkey = InventoryUtils.findItem(inventorycrafting, Registry.ItemKey);
		if (dkey != null) keyStack.setItemDamage(dkey.getItemDamage());
		
		NBTTagCompound dataTag = new NBTTagCompound();
		
		
		return keyStack;
	}
}
