package ml.boxes.recipe;

import ml.boxes.Registry;
import ml.boxes.data.ItemBoxContainer;
import ml.boxes.item.ItemBox;
import ml.boxes.item.ItemType;
import ml.core.item.recipe.CRecipeShapedBase.AutoNEI;
import ml.core.item.recipe.ShapedRecipe;
import ml.core.util.DyeUtils;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

@AutoNEI
public class RecipeBox extends ShapedRecipe implements IRecipe {

	public RecipeBox() {
		super(new ItemStack(Registry.BlockBox), "ccc", "cdc", "ccc", 'c', ItemType.ISFromType(ItemType.Cardboard, 1), 'd', DyeUtils.getAllDyeStacks());
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting var1) {
		ItemStack is = new ItemStack(Registry.BlockBox, 1);
		ItemBoxContainer iib = new ItemBoxContainer(is);
		
		int dyeId = DyeUtils.getVanillaColorId(var1.getStackInRowAndColumn(1, 1));
		iib.getBox().boxName = ItemBox.getColoredBoxName(dyeId);
		iib.getBox().boxColor = ItemDye.dyeColors[dyeId];

		iib.saveData();

		return is;
	}
}
