package ml.boxes.tile.safe;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

import ml.boxes.tile.TileEntitySafe;
import ml.boxes.tile.safe.SafeMechanism.MechanismInstance;
import ml.core.ReflectionUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class MechsHelper {
	
	public static MechanismInstance tryInstantiate(String mechName, TileEntitySafe safe) {
		if (MechanismInstance.mechs.containsKey(mechName)){
			Class clazz = MechanismInstance.mechs.get(mechName).mechClass;
			try {
				return (MechanismInstance)clazz.getConstructor(TileEntitySafe.class).newInstance(safe);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return new MechFallback(safe);
	}

	public static void attemptGetISInfo(ItemStack is, List infos) {
		NBTTagCompound tag = is.getTagCompound() != null ? is.getTagCompound() : new NBTTagCompound();
		String mechN = tag.getString("mechType");
		if (MechanismInstance.mechs.containsKey(mechN)) {
			Class smech = MechanismInstance.mechs.get(mechN).mechClass;
			infos.add("Mechanism: " + LanguageRegistry.instance().getStringLocalization(mechN));
			List<Method> anns = ReflectionUtils.getMethodsAnnotatedWith(smech, MechanismInstance.MethodAddInfo.class);
			for (Method mthd : anns) {
				if (Modifier.isStatic(mthd.getModifiers())) {
					try {
						mthd.invoke(null, is, infos);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				} else {
					throw new IllegalStateException("Method in class \"" + smech.getName() + "\" has the \"MethodAddInfo\" annotation, but is not static.");
				}
			}
		} else {
			infos.add("Mechanism: \u00A7c\u00A7oInvalid");
		}
	}
	
	public static void copyNBTDataToSafe(String MechID, ItemStack mechStack, NBTTagCompound safeMechTag) {
		if (MechanismInstance.mechs.containsKey(MechID)) {
			Class smech = MechanismInstance.mechs.get(MechID).mechClass;
			List<Method> anns = ReflectionUtils.getMethodsAnnotatedWith(smech, MechanismInstance.MethodAddInfo.class);
			for (Method mthd : anns) {
				if (Modifier.isStatic(mthd.getModifiers())) {
					try {
						mthd.invoke(null, mechStack, safeMechTag);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				} else {
					throw new IllegalStateException("Method in class \"" + smech.getName() + "\" has the \"OnUsedInCrafting\" annotation, but is not static.");
				}
			}
		}
	}

	public static ItemStack getDecraftedMechStack(String MechID, NBTTagCompound safeMechTag) {
		if (MechanismInstance.mechs.containsKey(MechID)) {
			Class smech = MechanismInstance.mechs.get(MechID).mechClass;
			List<Method> anns = ReflectionUtils.getMethodsAnnotatedWith(smech, MechanismInstance.MethodAddInfo.class);
			for (Method mthd : anns) {
				if (Modifier.isStatic(mthd.getModifiers())) {
					try {
						return (ItemStack)mthd.invoke(null, safeMechTag);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
			}
			throw new IllegalStateException("Method in class \"" + smech.getName() + "\" does not have a static method with the \"MethodGetItemStack\" annotation");
		}
		return null;
	}

}
