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
	
	public static Map<String, Class<? extends SafeMechanism>> mechs = new HashMap<String, Class<? extends SafeMechanism>>();
	
	public static void registerMechansim(Class mechCls) {
		mechs.put(mechCls.getName(), mechCls);
	}
	
	public static SafeMechanism tryInstantialize(String mechName, TileEntitySafe safe) {
		if (mechs.containsKey(mechName)){
			Class clazz = mechs.get(mechName);
			try {
				return (SafeMechanism)clazz.getConstructor(TileEntitySafe.class).newInstance(safe);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return new MechFallback(safe);
	}
	
	public static void attemptGetISInfo(ItemStack is, List infos) {
		NBTTagCompound tag = is.getTagCompound() != null ? is.getTagCompound() : new NBTTagCompound();
		//if (tag != null) {
			String mechN = tag.getString("mechType");
			if (mechs.containsKey(mechN)) {
				Class smech = mechs.get(mechN);
				infos.add("Mechanism: " + LanguageRegistry.instance().getStringLocalization(mechN));
				try {
					Method getInfo = smech.getMethod("getISInfo", ItemStack.class, List.class);
					getInfo.invoke(null, is, infos);
				} catch (Exception e) {

				}
			} else {
				infos.add("Mechanism: \u00A7c\u00A7oInvalid");
			}
		//}
	}
	
	public final TileEntitySafe safe;
	
	public SafeMechanism(TileEntitySafe tsafe) {
		this.safe = tsafe;
	}
	
	public abstract NBTTagCompound saveNBT();
	public abstract void loadNBT(NBTTagCompound mechKey);
	
	public NBTTagCompound writeNBTPacket() {
		return saveNBT();
	}
	
	public NBTTagCompound saveNBTForIS() {
		return saveNBT();
	}
	
	public abstract void beginUnlock(EntityPlayer epl);
	
	public abstract boolean matches(SafeMechanism tmech);
	
	public void onClosed(){};
}
