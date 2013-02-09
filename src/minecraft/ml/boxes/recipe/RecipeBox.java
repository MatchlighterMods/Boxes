package ml.boxes.recipe;

import java.util.ArrayList;

import ml.boxes.BoxData;
import ml.boxes.Boxes;
import ml.boxes.Lib;
import ml.boxes.item.ItemBox;
import net.minecraft.block.BlockCloth;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class RecipeBox implements IRecipe {
	
    
    private ItemStack currentOut;
	
	public RecipeBox() {
		
	}
	
    @Override
    public boolean matches(InventoryCrafting inv, World world){
    	ItemStack cb = new ItemStack(Boxes.ItemCardboard);
    	
        return false;
    }
    

	
	@Override
	public ItemStack getCraftingResult(InventoryCrafting var1) {
		ItemStack is = new ItemStack(Boxes.BlockBox, 1);
		BoxData data = ItemBox.getDataFromIS(is);
		
		//data.boxColor = ItemDye.dyeColors[Lib.getEquivVanillaDye(var1.getStackInRowAndColumn(1, 1)).getItemDamage()];
		ItemBox.setBoxDataToIS(is, data);
		
		return is;
	}

	@Override
	public int getRecipeSize() {
		return 9;
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
