package ml.boxes.tile.safe;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.HashMap;
import java.util.Map;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import ml.boxes.tile.TileEntitySafe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

	public abstract class SafeMechanism {
	
		/**
	 * <b>(Optional)</b> Annotate your MechanismInfo static method with this annotation.<br>
	 * Used to get Tooltip info for safes with the mechanism installed.<br>
	 * Must accept 2 parameters: {@link ItemStack} safeStack, List toolTipLines
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public static @interface MethodAddInfo {}
	
	/**
	 * <b>(Optional)</b> Annotate your OnSafeCraftedWith static method with this annotation.<br>
	 * Used to copy necessary NBT tags to the safeStack from the IItemMechs ItemStack.<br>
	 * Must accept 2 parameters: {@link ItemStack} mechStack, {@link NBTTagCompound} safeMechData
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public static @interface OnUsedInCrafting {}
	
	/**
	 * <b>(Required)</b> Annotate your OnSafeDecrafted static method with this annotation.<br>
	 * Used to remove the Mechanism from a safe and return the ItemStack of the Mechanism<br>
	 * Must accept 1 parameter: {@link NBTTagCompound} safeMechData<br>
	 * Must return an {@link ItemStack} of the Mechanism
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public static @interface MethodGetItemStack {}

	public final static Map<String, RegMech> mechs = new HashMap<String, RegMech>();

	public final static void registerMechanism(IItemMech itm, Class<? extends SafeMechanism> mechCls) {
		mechs.put(mechCls.getName(), new RegMech(itm, mechCls));
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
	 * Should eventually call Safe.unlock() if the auth process succeeds
	 * 
	 * @param epl The player attempting to open the safe
	 */
	public abstract void beginUnlock(EntityPlayer epl);

	/**
	 * Used for determining if two safes can connect.
	 * You don't need to Type check
	 */
	public abstract boolean canConnectWith(SafeMechanism tmech);

	/**
	 * Called when the safe is locked 
	 */
	public void onLocked(){};
	
	/**
	 * Render any additions to the safe.
	 * @param pass Indicates what the GLMatrix is currently transformed for.
	 * @param stacked True if the safe is a double-safe
	 */
	@SideOnly(Side.CLIENT)
	public abstract void render(RenderPass pass, boolean stacked); 
	
	public static enum RenderPass {
		SafeBody,
		SafeDoor;
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
