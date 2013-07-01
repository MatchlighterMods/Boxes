package ml.boxes.tile.safe;

import java.util.List;

import ml.boxes.Registry;
import ml.boxes.item.ItemKey;
import ml.boxes.tile.TileEntitySafe;
import ml.boxes.tile.safe.SafeMechanism.MechanismInstance;
import ml.boxes.tile.safe.SafeMechanism.RenderPass;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
public class MechKey extends SafeMechanism {
	public static class keymech extends MechanismInstance {
		
		public int keyId = 0;
		
		public keymech(TileEntitySafe tsafe) {
			super(tsafe);
		}
		
		@Override
		public NBTTagCompound saveNBT() {
			NBTTagCompound tag = new NBTTagCompound();
			tag.setInteger("keyId", keyId);
			return tag;
		}
	
		@Override
		public void loadNBT(NBTTagCompound mechKey) {
			keyId = mechKey.getInteger("keyId");
		}
		
		@Override
		public void beginUnlock(EntityPlayer epl) {
			ItemStack is = epl.getHeldItem();
			if (is.getItem() instanceof ItemKey && is.getItemDamage() == keyId) {
				this.safe.unlock();
			}
		}
	
		@Override
		public boolean canConnectWith(MechanismInstance tmech) {
			return ((keymech)tmech).keyId == keyId;
		}
	
		@Override
		public void render(RenderPass pass, boolean stacked) {
			// TODO Auto-generated method stub
			
		}
	}

	@Override
	public MechanismInstance createInstance(TileEntitySafe tsafe) {
		return new keymech(tsafe);
	}

	@Override
	public ItemStack decraftGetMechStack(NBTTagCompound safeMechData) {
		// TODO Auto-generated method stub
		return null;
	}
}