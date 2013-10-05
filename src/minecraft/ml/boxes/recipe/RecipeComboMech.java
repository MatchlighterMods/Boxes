package ml.boxes.recipe;

import ml.boxes.Registry;
import ml.boxes.item.ItemMechs;
import ml.boxes.tile.safe.MechCombo;
import ml.core.item.ItemUtils;
import ml.core.item.StackUtils;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public class RecipeComboMech implements IRecipe {

	private ItemStack irnStack = new ItemStack(Item.ingotIron);
	@Override
	public boolean matches(InventoryCrafting inv, World world) {

		for (int i : new int[]{0,1,2, 6,7,8}){
			if (!ItemUtils.checkItemEquals(irnStack, inv.getStackInSlot(i)))
				return false;
		}

		for (int i : new int[]{3,4,5}) {
			int oId = OreDictionary.getOreID(inv.getStackInSlot(i));
			if (oId == -1 || !OreDictionary.getOreName(oId).startsWith("dye", 0)){
				return false;
			}	
		}
		
		return true;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		ItemStack mch = getRecipeOutput();
		NBTTagCompound tag = StackUtils.getStackTag(mch);
		
		int[] combo = new int[3];
		for (int c=0; c<3; c++) {
			combo[c] = ItemUtils.getVanillaColorId(inv.getStackInRowAndColumn(c, 1));
		}
		tag.setIntArray("combination", combo);
		mch.setTagCompound(tag);
		return mch;
	}

	@Override
	public int getRecipeSize() {
		return 9;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return new ItemStack(Registry.ItemMechs, 1, ItemMechs.metaForMech(MechCombo.class));
	}

}
