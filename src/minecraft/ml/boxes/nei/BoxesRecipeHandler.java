package ml.boxes.nei;

import java.util.ArrayList;
import java.util.List;

import ml.boxes.Boxes;
import ml.boxes.recipe.RecipeBox;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import codechicken.nei.InventoryCraftingDummy;
import codechicken.nei.NEIClientUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.ShapedRecipeHandler;
import codechicken.nei.recipe.ShapedRecipeHandler.CachedShapedRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler.CachedRecipe;

public class BoxesRecipeHandler extends ShapedRecipeHandler {

	public class CachedBoxesRecipe extends CachedShapedRecipe{
		public CachedBoxesRecipe() {
			super(2, 1, new Object[]{new ItemStack(Boxes.ItemCardboard), new ItemStack(Boxes.ItemCardboard)}, recipe.getRecipeOutput()); //TODO Add the recipe
			cycle();
		}
				
		private void cycle(){
			ArrayList<PositionedStack> ingreds = getIngredients();
            for(int i = 0; i < 9; i++)
                invCrafting.setInventorySlotContents(i, i < ingreds.size() ? ingreds.get(i).item : null);
			this.result = new PositionedStack(recipe.getCraftingResult(invCrafting), 119, 24);
		}		
	}
	
	private InventoryCrafting invCrafting = new InventoryCraftingDummy();
	private RecipeBox recipe = new RecipeBox();
	private final CachedBoxesRecipe cached;
	
	public BoxesRecipeHandler() {
		cached = new CachedBoxesRecipe();
	}
	
	@Override
	public void loadCraftingRecipes(String outputId, Object... results) {
		if (outputId == "crafting"){
			arecipes.add(cached);
		} else {
			super.loadCraftingRecipes(outputId, results);
		}
	}

	@Override
	public void loadCraftingRecipes(ItemStack result) {
		if (result.isItemEqual(recipe.getRecipeOutput())){
			arecipes.add(cached);
		}
	}

	@Override
	public void loadUsageRecipes(ItemStack ingredient) {
		List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
		if (cached.contains(cached.ingredients, ingredient)){
			cached.setIngredientPermutation(cached.ingredients, ingredient);
			arecipes.add(cached);
		}
	}

	@Override
	public void onUpdate() {
		if(!NEIClientUtils.shiftKey())
        {
            cycleticks++;
            if(cycleticks%20 == 0)
                for(CachedRecipe crecipe : arecipes)
                    ((CachedBoxesRecipe)crecipe).cycle();
        }
	}
}
