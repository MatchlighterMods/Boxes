package ml.boxes.recipe;

import ml.boxes.Registry;
import ml.core.item.StackUtils;
import ml.core.item.recipe.CRecipeShapedBase.AutoNEI;
import ml.core.item.recipe.ShapedRecipe;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;

@AutoNEI()
public class RecipeKeyMech extends ShapedRecipe {

	public RecipeKeyMech(Object... recipe) {
		super(new ItemStack(Registry.itemMechKey), recipe);
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		ItemStack mch = getRecipeOutput();
		ItemStack keyStack = inv.getStackInRowAndColumn(1, 0);
		StackUtils.setTag(mch, StackUtils.getTag(keyStack, -1, "key_id"), "key_id");
		if (StackUtils.hasTagAt(keyStack, "color")) {
			StackUtils.setTag(mch, StackUtils.getTag(keyStack, 0, "color"), "color");
		}
		return mch;
	}
}
