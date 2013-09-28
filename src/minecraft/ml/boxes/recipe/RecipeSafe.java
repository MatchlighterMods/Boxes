package ml.boxes.recipe;

import java.util.Arrays;

import ml.boxes.Boxes;
import ml.boxes.tile.safe.IItemMech;
import ml.boxes.tile.safe.MechRegistry;
import ml.core.item.ItemUtils;
import ml.core.item.recipe.RecipeShapedVariable;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public class RecipeSafe extends RecipeShapedVariable {

	@Override
	public boolean matches(InventoryCrafting inv, World world) {

		boolean steelOn = Arrays.asList(OreDictionary.getOreNames()).contains("ingotSteel");
		for (int i : new int[]{0,1,2, 3, 6,7,8}){
			if (Boxes.config.lockbox_useSteel && steelOn) {
				if (OreDictionary.getOreID(inv.getStackInSlot(i)) != OreDictionary.getOreID("ingotSteel"))
					return false;
			} else {
				if (!ItemUtils.checkItemEquals(Item.ingotIron, inv.getStackInSlot(i)))
					return false;
			}
		}
		
		return true;
	}

	@Override
	public boolean itemMatchesAt(int lx, int ly, ItemStack is) {
		if (lx==2 && ly==1) {
			return is != null && MechRegistry.isAMechanism(is);
		}
		return super.itemMatchesAt(lx, ly, is);
	}
	
	@Override
	public ItemStack getCraftingResult(InventoryCrafting inventorycrafting) {
		return null;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return null;
	}

}
