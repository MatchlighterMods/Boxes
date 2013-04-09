package ml.boxes.api.mail;

import ml.boxes.api.BasicItemFilter;
import ml.boxes.api.IItemFilter;
import net.minecraft.item.ItemStack;

public class BasicContrabandHandler implements IContrabandHandler {

	private final IItemFilter iFilter;
	private final double chance;
	
	public BasicContrabandHandler(ItemStack is, double chance) {
		this.iFilter = new BasicItemFilter(is);
		this.chance = chance; 
	}
	
	public BasicContrabandHandler(IItemFilter iif, double chance) {
		this.iFilter = iif;
		this.chance = chance; 
	}
	
	@Override
	public double getDetectionChance(ItemStack is) {
		if (iFilter.ISMatchesFilter(is))
			return chance;
		return 0;
	}

}
