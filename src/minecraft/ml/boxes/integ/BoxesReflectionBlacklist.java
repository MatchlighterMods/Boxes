package ml.boxes.integ;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ml.boxes.Registry;
import ml.boxes.api.IItemFilter;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class BoxesReflectionBlacklist implements IItemFilter {

	public static List<String> blClasses = new ArrayList<String>();
	
	public BoxesReflectionBlacklist() {
		blClasses.addAll(Arrays.asList(Registry.config.boxes_blacklist));
	}

	private boolean reflectionItemCheck(Item itm, String clsName) {
		try {
			Class cls = Class.forName(clsName); 
			return cls.isInstance(itm) || (itm instanceof ItemBlock && cls.isInstance(Block.blocksList[((ItemBlock)itm).getBlockID()]));
		} catch (Exception e) {}
		return false;
	}

	@Override
	public boolean ISMatchesFilter(ItemStack is) {
		if (is.getItem() != null) {
			for (String cls : blClasses) {
				if (reflectionItemCheck(is.getItem(), cls)) return true;
			}
		}
		return false;
	}

}
