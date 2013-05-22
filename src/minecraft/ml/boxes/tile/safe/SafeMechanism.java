package ml.boxes.tile.safe;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ml.boxes.tile.TileEntitySafe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.common.registry.LanguageRegistry;

public abstract class SafeMechanism {
	
	private static MechFallback fallback = new MechFallback();
	public static Map<String, SafeMechanism> mechs = new HashMap<String, SafeMechanism>();
	
	public static void registerMechansim(SafeMechanism mech) {
		mechs.put(mech.getClass().getName(), mech);
	}
	
	public static SafeMechanism tryInstantialize(String mechName, TileEntitySafe safe) {
		if (mechs.containsKey(mechName)){
//			Class clazz = mechs.get(mechName);
//			try {
//				return (SafeMechanism)clazz.getConstructor(TileEntitySafe.class).newInstance(safe);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
			return mechs.get(mechName);
		}
		return fallback;
	}
	
	public static void attemptGetISInfo(ItemStack is, List infos) {
		NBTTagCompound tag = is.getTagCompound() != null ? is.getTagCompound() : new NBTTagCompound();
		//if (tag != null) {
			String mechN = tag.getString("mechType");
			if (mechs.containsKey(mechN)) {
				SafeMechanism smech = mechs.get(mechN);
				infos.add("Mechanism: " + LanguageRegistry.instance().getStringLocalization(mechN));
				smech.addISInfo(is, infos);
			} else {
				infos.add("Mechanism: \u00A7c\u00A7oInvalid");
			}
		//}
	}
	
	public NBTTagCompound writeNBTPacket(TileEntitySafe tes) {
		return tes.mechTag;
	}
	
	public NBTTagCompound saveNBTForIS(TileEntitySafe tes) {
		return tes.mechTag;
	}
	
	public abstract void addISInfo(ItemStack is, List infos);
	
	public abstract void beginUnlock(TileEntitySafe tes, EntityPlayer epl);
	
	public abstract boolean matches(NBTTagCompound mech1, NBTTagCompound mech2);
	
	public void onClosed(TileEntitySafe tes){};
}
