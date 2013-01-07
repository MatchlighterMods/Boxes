package ml.boxes;

import net.minecraft.block.BlockCloth;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class RecipeBox implements IRecipe {
	
	ItemStack[] pattern;
	
	public RecipeBox() {
		ItemStack cb = new ItemStack(Boxes.ItemCardboard);
		pattern = new ItemStack[]{
				cb,cb,cb,
				cb,new ItemStack(Item.dyePowder,1,-1),cb,
				cb,cb,cb};
	}
	
	@Override
	public boolean matches(InventoryCrafting var1, World var2) {
		for (int i=0; i<9; i++){
			if (!checkItemEquals(pattern[i], var1.getStackInSlot(i)))
				return false;
		}
		return true;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting var1) {
		ItemStack is = new ItemStack(Boxes.BlockBox, 1, var1.getStackInRowAndColumn(1, 1).getItemDamage());
		BoxData data = ItemBox.getDataFromIS(is);
		//data.boxColor = var1.getStackInRowAndColumn(1, 1).getItemDamage();
		ItemBox.setBoxDataToIS(is, data);
		
		return is;
	}

	@Override
	public int getRecipeSize() {
		return pattern.length;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return new ItemStack(Boxes.BlockBox, 1, 0);
	}
	
    private boolean checkItemEquals(ItemStack target, ItemStack input)
    {
        if (input == null && target != null || input != null && target == null)
        {
            return false;
        }
        return (target.itemID == input.itemID && (target.getItemDamage() == -1 || target.getItemDamage() == input.getItemDamage()));
    }
}
