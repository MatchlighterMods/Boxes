package ml.boxes.api.mail;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;

public class MailContraband {
	private static List<IContrabandHandler> contrabandHandlers = new ArrayList<IContrabandHandler>();
	
	public static void addContrabandHandler(IContrabandHandler ICH){
		contrabandHandlers.add(ICH);
	}
	
	public static double getDetectionChance(ItemStack is){
		double max = 0;
		for (IContrabandHandler ICH : contrabandHandlers){
			double chance = ICH.getDetectionChance(is);
			if (chance>max)
				max = chance;
		}
		return max;
	}
}
