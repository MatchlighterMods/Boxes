package ml.boxes.tile.safe;

import ml.boxes.api.safe.ISafe;
import ml.boxes.api.safe.SafeMechanism;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.EnumChatFormatting;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * A fall-back mechanism that will be loaded if a safe fails to save or load properly.
 * Shouldn't ever happen, but neither should NPEs
 * 
 * @author Matchlighter
 */
public class MechFallback extends SafeMechanism {
	
	@Override
	public String getMechId() {
		return "fallback";
	}
	
	@Override
	public String getUnlocalizedMechName() {
		return "mechanism.invalid";
	}
	
	@Override
	public String getLocalizedName() {
		return EnumChatFormatting.DARK_RED.toString() + EnumChatFormatting.ITALIC.toString() + super.getLocalizedName();
	}

	@Override
	public void beginUnlock(ISafe safe, EntityPlayer epl) {
		epl.sendChatToPlayer(ChatMessageComponent.createFromText("\u00A77\u00A7oWarning: The safe has been corrupted and can no longer be locked properly!"));
		//FMLLog.log("Boxes", Level.WARNING, "The safe at ");
		safe.doUnlock();
	}

	@Override
	public boolean canConnectWith(ISafe self, ISafe remote) {
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void render(NBTTagCompound mech_data, RenderPass pass, boolean stacked) {
		
	}

	@Override
	public NBTTagCompound writeNBTPacket(ISafe safe) {
		return null;
	}

	@Override
	public boolean stackIsMech(ItemStack itm) {
		return false;
	}

	@Override
	public ItemStack toItemStack(NBTTagCompound mechData) {
		return null;
	}
}
