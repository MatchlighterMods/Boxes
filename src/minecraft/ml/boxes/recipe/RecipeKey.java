package ml.boxes.recipe;

import java.util.List;

import ml.boxes.Boxes;
import ml.core.inventory.InventoryUtils;
import ml.core.item.RecipeMixed;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class RecipeKey extends RecipeMixed {

	public RecipeKey(Object... recipe) {
		super(Boxes.ItemKey, recipe);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean shapelessItemsValid(InventoryCrafting inv,
			List<ItemStack> items) {
		
		List<ItemStack> keys = InventoryUtils.findItems(inv, Boxes.ItemKey);
		if (keys.size()>1) return false;
		
		for (ItemStack is : items) {
			if (is.getItem() != Boxes.ItemKey &&
				is.getItem() != Item.dyePowder) {
				return false;
			}
		}
		
		return true;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inventorycrafting) {
		ItemStack keyStack = new ItemStack(Boxes.ItemKey);
		
		ItemStack dkey = InventoryUtils.findItem(inventorycrafting, Boxes.ItemKey);
		if (dkey != null) keyStack.setItemDamage(dkey.getItemDamage());
		
		NBTTagCompound dataTag = new NBTTagCompound();
		
		
		return keyStack;
	}
}
