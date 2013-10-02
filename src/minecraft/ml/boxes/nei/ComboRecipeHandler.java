package ml.boxes.nei;

import java.util.ArrayList;
import java.util.List;

import ml.boxes.Registry;
import ml.boxes.nei.BoxesRecipeHandler.CachedBoxesRecipe;
import ml.boxes.recipe.RecipeBox;
import ml.boxes.recipe.RecipeComboMech;
import ml.boxes.recipe.RecipeSafe;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.OreDictionary;
import codechicken.nei.InventoryCraftingDummy;
import codechicken.nei.NEIClientUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.ShapedRecipeHandler;
import codechicken.nei.recipe.ShapedRecipeHandler.CachedShapedRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler.CachedRecipe;

public class ComboRecipeHandler extends ShapedRecipeHandler {

	public class CachedDialRecipe extends CachedShapedRecipe{
		public CachedDialRecipe(Object [] rec) {
			super(3, 3, rec, recipe.getRecipeOutput());
			cycle();
		}

		private void cycle(){
			List<PositionedStack> ingreds = getIngredients();
			for (int x=0; x<3; x++) {
				for (int y=0; y<3; y++) {
					invCrafting.setInventorySlotContents(x+y*3, x*3+y < ingreds.size() ? ingreds.get(x*3+y).item : null);
				}
			}
			this.result = new PositionedStack(recipe.getCraftingResult(invCrafting), 119, 24);
		}		
	}

	private InventoryCrafting invCrafting = new InventoryCraftingDummy();
	private RecipeComboMech recipe = new RecipeComboMech();
	private final CachedDialRecipe cached;

	public ComboRecipeHandler() {
		ItemStack irn = new ItemStack(Item.ingotIron);
		List<ItemStack> dyes = new ArrayList<ItemStack>();
		for (int i=0; i<16; i++){
			dyes.addAll(OreDictionary.getOres(OreDictionary.getOreID(new ItemStack(Item.dyePowder, 1, i))));
		}
		cached = new CachedDialRecipe(new Object[]{irn,irn,irn, dyes,dyes,dyes, irn,irn,irn});
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
					((CachedDialRecipe)crecipe).cycle();
		}
	}
}
