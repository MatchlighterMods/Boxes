package ml.boxes.tile.safe;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ml.boxes.tile.TileEntitySafe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public abstract class SafeMechanism {
	
	public static Map<String, Class<? extends SafeMechanism>> mechs = new HashMap<String, Class<? extends SafeMechanism>>();
	static {
		mechs.put(MechCombo.class.getName(), MechCombo.class);
	}
	
	public static SafeMechanism tryInstantialize(String mechName, TileEntitySafe safe) {
		if (mechs.get(mechName) != null){
			Class clazz = mechs.get(mechName);
			try {
				return (SafeMechanism)clazz.getConstructor(TileEntitySafe.class).newInstance(safe);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return new MechFallback(safe);
	}
	
	public final TileEntitySafe safe;
	
	public SafeMechanism(TileEntitySafe tsafe) {
		this.safe = tsafe;
	}
	
	public abstract NBTTagCompound saveNBT();
	public abstract void loadNBT(NBTTagCompound mechKey);
	
	public abstract void beginUnlock(EntityPlayer epl);
	
	public abstract boolean matches(SafeMechanism tmech);
	
	public void onClosed(){};
}
