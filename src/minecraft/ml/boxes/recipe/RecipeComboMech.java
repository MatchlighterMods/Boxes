package ml.boxes.recipe;

import ml.boxes.Registry;
import ml.core.item.StackUtils;
import ml.core.item.recipe.CRecipeShapedBase.AutoNEI;
import ml.core.item.recipe.ShapedRecipe;
import ml.core.util.DyeUtils;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;

@AutoNEI
public class RecipeComboMech extends ShapedRecipe implements IRecipe {

	public RecipeComboMech(Object... recipe) {
		super(new ItemStack(Registry.itemMechCombination), recipe);
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		ItemStack mch = getRecipeOutput();
		NBTTagCompound tag = StackUtils.getStackTag(mch);
		
		int[] combo = new int[3];
		for (int c=0; c<3; c++) {
			combo[c] = DyeUtils.getVanillaColorId(inv.getStackInRowAndColumn(c, 1));
		}
		tag.setIntArray("combination", combo);
		mch.setTagCompound(tag);
		return mch;
	}

}
