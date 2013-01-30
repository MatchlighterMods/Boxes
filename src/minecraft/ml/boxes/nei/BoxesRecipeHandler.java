package ml.boxes.nei;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ml.boxes.BoxData;
import ml.boxes.Boxes;
import ml.boxes.RecipeBox;
import ml.boxes.item.ItemBox;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import codechicken.nei.InventoryCraftingDummy;
import codechicken.nei.NEIClientUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.ShapedRecipeHandler;
import codechicken.nei.recipe.TemplateRecipeHandler;
import codechicken.nei.recipe.FireworkRecipeHandler.CachedFireworkRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler.CachedRecipe;

public class BoxesRecipeHandler extends TemplateRecipeHandler {

	public class CachedBoxesRecipe extends CachedRecipe{
		public PositionedStack result;
		
		public CachedBoxesRecipe() {
			cycle();
		}
				
		private void cycle(){
			ArrayList<PositionedStack> ingreds = getIngredients();
            for(int i = 0; i < 9; i++)
                invCrafting.setInventorySlotContents(i, i < ingreds.size() ? ingreds.get(i).item : null);
			this.result = new PositionedStack(recipe.getCraftingResult(invCrafting), 119, 24);
		}

		@Override
		public PositionedStack getResult() {
			return result;
		}
		
	}
	
	private InventoryCrafting invCrafting = new InventoryCraftingDummy();
	private RecipeBox recipe = new RecipeBox();
	private final CachedBoxesRecipe cached;
	
	public BoxesRecipeHandler() {
		cached = new CachedBoxesRecipe();
	}
	
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
	
	@Override
	public String getGuiTexture()
	{
		return "/gui/crafting.png";
	}
    
    @Override
    public String getOverlayIdentifier()
    {
        return "crafting";
    }

	
}
