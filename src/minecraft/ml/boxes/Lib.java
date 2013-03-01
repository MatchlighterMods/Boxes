package ml.boxes;

import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class Lib {
	public static String[] DyeOreNames = {"dyeBlack", "dyeRed", "dyeGreen", "dyeBrown", "dyeBlue", "dyePurple", "dyeCyan", "dyeLightGray", "dyeGray", "dyePink", "dyeLime", "dyeYellow", "dyeLightBlue", "dyeMagenta", "dyeOrange", "dyeWhite"};
	
	public static ItemStack getEquivVanillaDye(ItemStack is){
		for (int i=0; i<ItemDye.dyeColorNames.length; i++){
			if (OreDictionary.getOreID(is) == OreDictionary.getOreID(new ItemStack(Item.dyePowder, 1, i))){
				return new ItemStack(Item.dyePowder, 1, i);
			}
		}
		return null;
	}
}
