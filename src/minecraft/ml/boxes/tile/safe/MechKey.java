package ml.boxes.tile.safe;

import java.util.List;

import org.lwjgl.opengl.GL11;

import ml.boxes.Registry;
import ml.boxes.api.safe.ISafe;
import ml.boxes.api.safe.SafeMechanism;
import ml.boxes.item.ItemKey;
import ml.core.item.StackUtils;
import ml.core.world.WorldRenderUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class MechKey extends SafeMechanism {

	@Override
	public String getMechId() {
		return "ml.key";
	}
	
	@Override
	public String getUnlocalizedMechName() {
		return "mechanism.key";
	}
	
	@Override
	public void addInfoForStack(NBTTagCompound mechTag, List lst) {
		lst.add("Key Id: " + EnumChatFormatting.WHITE.toString() + mechTag.getInteger("key_id"));
	}
	
	@Override
	public void addInfoForSafe(NBTTagCompound mechTag, List lst) {
		addInfoForStack(mechTag, lst);
	}
	
	@Override
	public void beginUnlock(ISafe safe, EntityPlayer epl) {
		ItemStack is = epl.getHeldItem();
		if (is != null && is.getItem() instanceof ItemKey && StackUtils.getTag(is, -1, "key_id") == safe.getMechTag().getInteger("key_id")) {
			safe.doUnlock();
		}
	}

	@Override
	public boolean canConnectWith(ISafe self, ISafe remote) {
		return (self.getMechTag().getInteger("key_id")==remote.getMechTag().getInteger("key_id"));
	}

	private ItemStack renderStack;
	
	@Override
	@SideOnly(Side.CLIENT)
	public void render(NBTTagCompound mech_data, RenderPass pass, boolean stacked) {
		if (pass == RenderPass.SafeDoor) {
			if (renderStack == null) {
				renderStack = this.toItemStack(mech_data);
			}
			renderStack.setTagCompound(mech_data);
			
			GL11.glTranslatef(5F, stacked ? 20F:8F, 0.75F);
			GL11.glScalef(12F, 12F, 12F);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			
			WorldRenderUtils.renderItemIntoWorldCenteredAt(renderStack, true);
		}
	}

	@Override
	public NBTTagCompound writeNBTPacket(ISafe safe) {
		return safe.getMechTag();
	}

	@Override
	public boolean stackIsMech(ItemStack itm) {
		return itm.getItem() == Registry.itemMechKey;
	}

	@Override
	public ItemStack toItemStack(NBTTagCompound mechData) {
		return StackUtils.create(Registry.itemMechKey, 1, 0, mechData);
	}

}
