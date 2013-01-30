package ml.boxes;

import java.util.ArrayList;

import ml.boxes.item.ItemBox;
import net.minecraft.block.BlockCloth;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class RecipeBox implements IRecipe {
	
	public final Object[] pattern;
	
    private static final int MAX_CRAFT_GRID_WIDTH = 3;
    private static final int MAX_CRAFT_GRID_HEIGHT = 3;
    
    public int width = 3;
    public int height = 3;
    
    private ItemStack currentOut;
	
	public RecipeBox() {
		ItemStack cb = new ItemStack(Boxes.ItemCardboard);
		pattern = new ItemStack[]{
				cb,cb,cb,
				cb,new ItemStack(Item.dyePowder,1,-1),cb,
				cb,cb,cb};
	}
	
    @Override
    public boolean matches(InventoryCrafting inv, World world){
        for (int x = 0; x <= MAX_CRAFT_GRID_WIDTH - width; x++)
        {
            for (int y = 0; y <= MAX_CRAFT_GRID_HEIGHT - height; ++y)
            {
            	if (checkMatch(inv, x, y, true))
            		return true;
            }
        }
    
        return false;
    }
    
    private boolean checkMatch(InventoryCrafting inv, int startX, int startY, boolean mirror)
    {
        for (int x = 0; x < MAX_CRAFT_GRID_WIDTH; x++)
        {
            for (int y = 0; y < MAX_CRAFT_GRID_HEIGHT; y++)
            {
                int subX = x - startX;
                int subY = y - startY;
                Object target = null;

                if (subX >= 0 && subY >= 0 && subX < width && subY < height)
                {
                    if (mirror)
                    {
                        target = pattern[width - subX - 1 + subY * width];
                    }
                    else
                    {
                        target = pattern[subX + subY * width];
                    }
                }

                ItemStack slot = inv.getStackInRowAndColumn(x, y);
                
                if (target instanceof ItemStack)
                {
                    if (!checkItemEquals((ItemStack)target, slot))
                    {
                        return false;
                    }
                }
                else if (target instanceof ArrayList)
                {
                    boolean matched = false;
                    
                    for (ItemStack item : (ArrayList<ItemStack>)target)
                    {
                        matched = matched || checkItemEquals(item, slot);
                    }
                    
                    if (!matched)
                    {
                        return false;
                    }
                }
                else if (target == null && slot != null)
                {
                    return false;
                }
            }
        }

        return true;
    }
	
	@Override
	public ItemStack getCraftingResult(InventoryCrafting var1) {
		ItemStack is = new ItemStack(Boxes.BlockBox, 1);
		BoxData data = ItemBox.getDataFromIS(is);
		data.boxColor = Lib.getEquivVanillaDye(var1.getStackInRowAndColumn(1, 1)).getItemDamage();
		ItemBox.setBoxDataToIS(is, data);
		System.out.println(Integer.toHexString(is.hashCode()));
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
