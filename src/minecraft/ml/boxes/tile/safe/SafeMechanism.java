package ml.boxes.tile.safe;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ml.boxes.tile.TileEntitySafe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class SafeMechanism {
	
	public final static Map<String, SafeMechanism> mechs = new HashMap<String, SafeMechanism>();
	
	public final static void registerMechanism(IItemMech itm, SafeMechanism mech) {
		mechs.put(mech.getClass().getName(), mech);
	}
	
	public static abstract class MechanismInstance {
		
		public final TileEntitySafe safe;
	
		public MechanismInstance(TileEntitySafe tsafe) {
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
		public abstract boolean canConnectWith(MechanismInstance tmech);
	
		/**
		 * Called when the safe is locked 
		 */
		public void onLocked(){};
		
		public abstract SafeMechanism getSingleton();
		
		/**
		 * Render any additions to the safe.
		 * @param pass Indicates what the GLMatrix is currently transformed for.
		 * @param stacked True if the safe is a double-safe
		 */
		@SideOnly(Side.CLIENT)
		public abstract void render(RenderPass pass, boolean stacked); 
		
	}
	
	public abstract MechanismInstance createInstance(TileEntitySafe tsafe);
	
	public void addInfoToSafeStack(ItemStack sis, List<String> lines) {}
	
	public void onUsedInCrafting(ItemStack mechStack, NBTTagCompound safeMechData) {}
	
	public abstract ItemStack decraftGetMechStack(NBTTagCompound safeMechData);
	
	public static enum RenderPass {
		SafeBody,
		SafeDoor;
	}
}
