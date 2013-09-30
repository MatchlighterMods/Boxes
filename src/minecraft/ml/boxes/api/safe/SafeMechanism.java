package ml.boxes.api.safe;

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
	
	public abstract String getMechId();
	
	public abstract String getUnlocalizedMechName();

	public void addInfoForSafe(NBTTagCompound mechTag, List lst) {}
	
	/**
	 * @return NBTData specifically for when Mechanism is sent to client. Proper security dictates that combinations and the like should NOT be sent.
	 */
	public abstract NBTTagCompound writeNBTPacket(ISafe safe);

	/**
	 * Begin the unlock process. e.g. Check if its the right key or open Gui<br>
	 * Should eventually call Safe.unlock() if the auth process succeeds
	 * 
	 * @param epl The player attempting to open the safe
	 */
	public abstract void beginUnlock(ISafe safe, EntityPlayer epl);

	/**
	 * Used for determining if two safes can connect.
	 * You don't need to Type check
	 */
	public abstract boolean canConnectWith(ISafe self, ISafe remote);

	/**
	 * Called when the safe is locked 
	 */
	public void onLocked(ISafe safe){};
	
	/**
	 * Render any additions to the safe.
	 * @param pass Indicates what the GLMatrix is currently transformed for.
	 * @param stacked True if the safe is a double-safe
	 */
	@SideOnly(Side.CLIENT)
	public abstract void render(ISafe safe, RenderPass pass, boolean stacked); 
	
	public static enum RenderPass {
		SafeBody,
		SafeDoor;
	}
}
