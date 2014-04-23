package ml.boxes.tile.safe;

import java.util.List;

import ml.boxes.Registry;
import ml.boxes.api.safe.ISafe;
import ml.boxes.api.safe.SafeMechanism;
import ml.boxes.item.ItemKey;
import ml.core.item.StackUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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
		lst.add("Key Id: " + mechTag.getInteger("keyId"));
	}
	
	@Override
	public void addInfoForSafe(NBTTagCompound mechTag, List lst) {
		addInfoForStack(mechTag, lst);
	}
	
	@Override
	public void beginUnlock(ISafe safe, EntityPlayer epl) {
		ItemStack is = epl.getHeldItem();
		if (is.getItem() instanceof ItemKey && StackUtils.getTag(is, -1, "key_id") == safe.getMechTag().getInteger("keyId")) {
			safe.doUnlock();
		}
	}

	@Override
	public boolean canConnectWith(ISafe self, ISafe remote) {
		return (self.getMechTag().getInteger("keyId")==remote.getMechTag().getInteger("keyId"));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void render(NBTTagCompound mech_data, RenderPass pass, boolean stacked) {
		// TODO Render the Key Hole
		
	}

	@Override
	public NBTTagCompound writeNBTPacket(ISafe safe) {
		return new NBTTagCompound();
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
