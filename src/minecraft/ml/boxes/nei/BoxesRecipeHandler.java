package ml.boxes.nei;

import java.util.List;

import ml.boxes.Boxes;
import ml.boxes.RecipeBox;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import codechicken.nei.NEIClientUtils;
import codechicken.nei.recipe.ShapedRecipeHandler;
import codechicken.nei.recipe.TemplateRecipeHandler;

public class BoxesRecipeHandler extends ShapedRecipeHandler {

	@Override
	public String getRecipeName() {
		return "Boxes";
	}

	@Override
	public void loadCraftingRecipes(String outputId, Object... results) {
		if (outputId == "crafting"){
			List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
			for (IRecipe ir : recipes){
				if (ir instanceof RecipeBox){
					RecipeBox rb = (RecipeBox)ir;
					arecipes.add(new CachedShapedRecipe(rb.width, rb.height, rb.pattern, rb.getRecipeOutput()));
				}
			}
		} else {
			super.loadCraftingRecipes(outputId, results);
		}
	}

	@Override
	public void loadCraftingRecipes(ItemStack result) {
		List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
		for (IRecipe ir : recipes){
			if (NEIClientUtils.areStacksSameTypeCrafting(ir.getRecipeOutput(), result) && ir instanceof RecipeBox) {
				RecipeBox rb = (RecipeBox)ir;
				arecipes.add(new CachedShapedRecipe(rb.width, rb.height, rb.pattern, rb.getRecipeOutput()));
			}
		}
	}

	@Override
	public void loadUsageRecipes(ItemStack ingredient) {
		List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
		for (IRecipe ir : recipes){
			if (ir instanceof RecipeBox){
				RecipeBox rb = (RecipeBox)ir;
				CachedShapedRecipe recipe = new CachedShapedRecipe(rb.width, rb.height, rb.pattern, rb.getRecipeOutput());
				if (recipe.contains(recipe.ingredients, ingredient)){
					recipe.setIngredientPermutation(recipe.ingredients, ingredient);
					arecipes.add(recipe);
				}
			}
		}
	}

	
}
