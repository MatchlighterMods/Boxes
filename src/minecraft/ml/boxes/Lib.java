package ml.boxes;

import net.minecraft.entity.player.EntityPlayer;

public class Lib {

	public static boolean isRealPlayer(EntityPlayer pl){
		Package pkg = pl.getClass().getPackage();
		if (!pkg.getName().contains(".") || pkg.getName().contains("net.minecraft"))
			return true;
		return false;
	}

	public static Boolean pointInRect(int pntX, int pntY, int rectX, int rectY, int rectW, int rectH){
		return (pntX >= rectX && pntY >= rectY && pntX <= rectX+rectW && pntY <= rectY + rectH);
	}
}
