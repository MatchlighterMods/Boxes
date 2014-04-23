package ml.boxes.recipe;

import java.util.Arrays;

import ml.boxes.Registry;
import ml.boxes.api.safe.SafeMechanism;
import ml.boxes.block.MetaType;
import ml.boxes.tile.safe.MechRegistry;
import ml.core.item.StackUtils;
import ml.core.item.recipe.RecipeShapedVariable;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;

public class RecipeSafe extends RecipeShapedVariable {
	
	public RecipeSafe() {
		super(3,3);
	}

	@Override
	public boolean itemMatchesAt(int lx, int ly, ItemStack is, InventoryCrafting ic) {
	
		boolean steelOn = Arrays.asList(OreDictionary.getOreNames()).contains("ingotSteel");
		if (lx==2 && ly==1) {
			return (is!=null && MechRegistry.getMechForItem(is) != MechRegistry.fallback);
		} else if ((ly==0 || ly==2) || lx==0) {
			if (Registry.config.lockbox_useSteel && steelOn) {
				return (OreDictionary.getOreID(is) == OreDictionary.getOreID("ingotSteel"));
			} else {
				return StackUtils.checkItemEquals(Item.ingotIron, is);
			}
		}
		return super.itemMatchesAt(lx, ly, is, ic);
	}
	
	@Override
	public ItemStack getCraftingResult(InventoryCrafting inventorycrafting) {
		ItemStack mechStack = inventorycrafting.getStackInRowAndColumn(2, 1);
		ItemStack safeStack = new ItemStack(Registry.BlockMeta, 1, MetaType.Safe.meta);
		
		SafeMechanism sm = MechRegistry.getMechForItem(mechStack);
		
		NBTTagCompound mechTag = sm.toMechData(mechStack);
		NBTTagCompound safeTag = StackUtils.getStackTag(safeStack);
		
		safeTag.setString("mech_id", sm.getMechId());
		safeTag.setCompoundTag("mech_data", mechTag);
		
		return safeStack;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return null;
	}

}
