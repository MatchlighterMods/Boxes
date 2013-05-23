package ml.boxes.tile.safe;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cpw.mods.fml.common.registry.LanguageRegistry;

import ml.boxes.tile.TileEntitySafe;
import ml.core.ReflectionUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public abstract class SafeMechanism {

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public static @interface methodAddInfo {}

	public static Map<String, RegMech> mechs = new HashMap<String, RegMech>();

	public final static void registerMechanism(IItemMech itm, Class mechCls) {
		mechs.put(mechCls.getName(), new RegMech(itm, mechCls));
	}
	
	public final static SafeMechanism tryInstantiate(String mechName, TileEntitySafe safe) {
		if (SafeMechanism.mechs.containsKey(mechName)){
			Class clazz = SafeMechanism.mechs.get(mechName).mechClass;
			try {
				return (SafeMechanism)clazz.getConstructor(TileEntitySafe.class).newInstance(safe);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return new MechFallback(safe);
	}

	public final static void attemptGetISInfo(ItemStack is, List infos) {
		NBTTagCompound tag = is.getTagCompound() != null ? is.getTagCompound() : new NBTTagCompound();
		String mechN = tag.getString("mechType");
		if (SafeMechanism.mechs.containsKey(mechN)) {
			Class smech = SafeMechanism.mechs.get(mechN).mechClass;
			infos.add("Mechanism: " + LanguageRegistry.instance().getStringLocalization(mechN));
			List<Method> anns = ReflectionUtils.getMethodsAnnotatedWith(smech, SafeMechanism.methodAddInfo.class);
			for (Method mthd : anns) {
				if (Modifier.isStatic(mthd.getModifiers())) {
					try {
						mthd.invoke(null, is, infos);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					throw new IllegalStateException("Method in class \"" + smech.getName() + "\" has the \"methodAddInfo\" annotation, but is not static.");
				}
			}
		} else {
			infos.add("Mechanism: \u00A7c\u00A7oInvalid");
		}
	}
	
	public final TileEntitySafe safe;

	public SafeMechanism(TileEntitySafe tsafe) {
		this.safe = tsafe;
	}

	public abstract NBTTagCompound saveNBT();
	public abstract void loadNBT(NBTTagCompound mechKey);

	/**
	 * @return NBTData specifically for when Mechanism is sent to client. Proper security dictates that combinations and the like should NOT be sent.
	 */
	public NBTTagCompound writeNBTPacket() {
		return saveNBT();
	}

	/**
	 * Begin the unlock process. e.g. Check if its the right key or open Gui<br>
	 * May or may not call Safe.unlock()
	 * 
	 * @param epl The player attempting to open the safe
	 */
	public abstract void beginUnlock(EntityPlayer epl);

	/**
	 * Used for determining if two safes can connect.
	 * You don't need to Type check.
	 */
	public abstract boolean matches(SafeMechanism tmech);

	/**
	 * Called when the safe is locked 
	 */
	public void onLocked(){};
	
	/**
	 * The decrafter calls IItemMech.getMechStackForNBT(), this should too.
	 */
	public final ItemStack getOldMechStack() {
		if (mechs.containsKey(this.getClass().getName())) {
			return mechs.get(getClass().getName()).craftingItem.getMechStackFromNBT(saveNBT());
		}
		return null;
	}
	
	public static class RegMech {
		public final IItemMech craftingItem;
		public final Class<? extends SafeMechanism> mechClass;
		
		public RegMech(IItemMech cItem, Class<? extends SafeMechanism> mCls) {
			craftingItem = cItem;
			mechClass = mCls;
		}
	}
}
