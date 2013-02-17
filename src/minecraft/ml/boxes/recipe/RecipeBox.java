package ml.boxes.recipe;

import cpw.mods.fml.common.registry.LanguageRegistry;
import ml.boxes.BoxData;
import ml.boxes.Boxes;
import ml.boxes.item.ItemBox;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.StringTranslate;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public class RecipeBox implements IRecipe {

	private ItemStack currentOut;

	public RecipeBox() {

	}

	@Override
	public boolean matches(InventoryCrafting inv, World world){
		ItemStack cb = new ItemStack(Boxes.ItemCardboard);
		
		for (int i : new int[]{0,1,2, 3,5, 6,7,8}){
			if (!checkItemEquals(cb, inv.getStackInSlot(i)))
				return false;
		}
		
		int oId = OreDictionary.getOreID(inv.getStackInSlot(4));
		if (oId == -1 || !OreDictionary.getOreName(oId).startsWith("dye", 0)){
			return false;
		}

		return true;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting var1) {
		ItemStack is = new ItemStack(Boxes.BlockBox, 1);
		BoxData data = ItemBox.getDataFromIS(is);
		
		ItemStack dyeStack = var1.getStackInRowAndColumn(1, 1);
		for (int i=0; i<16; i++){
			if (OreDictionary.getOreID(new ItemStack(Item.dyePowder, 1, i)) == OreDictionary.getOreID(dyeStack)){
				data.boxName = ItemBox.getColoredBoxName(i);
				data.boxColor = ItemDye.dyeColors[i];
				break;
			}
		}

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
