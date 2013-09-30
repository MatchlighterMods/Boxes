package ml.boxes.tile.safe;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import ml.boxes.Registry;
import ml.boxes.api.safe.ISafe;
import ml.boxes.api.safe.SafeMechanism;
import ml.boxes.item.ItemKey;
import ml.boxes.tile.TileEntitySafe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class MechKey extends SafeMechanism {
	
	@Override
	public String getMechId() {
		return "ml.key";
	}
	
	@Override
	public String getUnlocalizedMechName() {
		return "item.mechanism.key";
	}
	
	@Override
	public void addInfoForSafe(NBTTagCompound mechTag, List lst) {
		lst.add("KeyId: " + mechTag.getInteger("keyId"));
	}
	
	@Override
	public void beginUnlock(ISafe safe, EntityPlayer epl) {
		ItemStack is = epl.getHeldItem();
		if (is.getItem() instanceof ItemKey && is.getItemDamage() == safe.getMechTag().getInteger("keyId")) {
			safe.doUnlock();
		}
	}

	@Override
	public boolean canConnectWith(ISafe self, ISafe remote) {
		return (self.getMechTag().getInteger("keyId")==remote.getMechTag().getInteger("keyId"));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void render(ISafe safe, RenderPass pass, boolean stacked) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public NBTTagCompound writeNBTPacket(ISafe safe) {
		return new NBTTagCompound();
	}

}
