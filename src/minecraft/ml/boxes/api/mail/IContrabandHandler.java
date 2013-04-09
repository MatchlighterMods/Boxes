package ml.boxes.api.mail;

import net.minecraft.item.ItemStack;

public interface IContrabandHandler {
		
	/**
	 * Note that it is up to you to check that the Item matches yours.
	 * @param is The ItemStack that is being tested as contraband
	 * @return The % chance that your item will be detected. 0 for no chance/no match
	 */
	public double getDetectionChance(ItemStack is);
}
