package ml.boxes;

import net.minecraft.entity.player.EntityPlayer;

public class Lib {

	public static boolean isRealPlayer(EntityPlayer pl){
		Package pkg = pl.getClass().getPackage();
		System.out.println(pkg.getName());
		if (!pkg.getName().contains(".") || pkg.getName().contains("net.minecraft"))
			return true;
		return false;
	}
}
