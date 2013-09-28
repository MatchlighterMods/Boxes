package ml.boxes.tile.safe;

import java.util.Arrays;
import java.util.List;

import ml.boxes.Boxes;
import ml.boxes.Registry;
import ml.boxes.api.safe.ISafe;
import ml.boxes.api.safe.SafeMechanism;
import ml.boxes.client.render.tile.SafeTESR;
import ml.boxes.tile.TileEntitySafe;
import ml.core.ChatUtils;
import ml.core.StringUtils;
import ml.core.item.StackUtils;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

public class MechCombo extends SafeMechanism {

	public static final int COMBO_LENGTH = 3;
	
	public void addInfoForSafe(NBTTagCompound mechTag, List infos) {
		StringBuilder bldr = new StringBuilder();
		bldr.append("Combination:");
		for (int i=0; i<COMBO_LENGTH; i++) {
			int c = mechTag.getInteger("digi_"+i);
			
			bldr.append(" ");
			bldr.append(ChatUtils.getColorStringFromDye(c));
			bldr.append(StringUtils.getLColorName(c));
		}
		infos.add(bldr.toString());
	}

	@Override
	public NBTTagCompound saveNBT() {
		NBTTagCompound tag = new NBTTagCompound();
		for (int i=0; i<combination.length; i++) {
			tag.setInteger("digi_"+i, combination[i]);
		}
		tag.setIntArray("dispCombo", dispCombination);
		return tag;
	}
	
	@Override
	public void loadNBT(NBTTagCompound mechKey) {
		dispCombination = mechKey.getIntArray("dispCombo");
		
		for (int i=0; i<COMBO_LENGTH; i++) {
			combination[i] = mechKey.getInteger("digi_"+i);
		}
		
		if (combination.length != COMBO_LENGTH) combination = new int[3];
		if (dispCombination.length != COMBO_LENGTH) dispCombination = new int[3];
	}
	
	@Override
	public NBTTagCompound writeNBTPacket(ISafe safe) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setIntArray("dispCombo", safe.getMechTag().getIntArray("dispCombo"));
		return tag;
	}

	@Override
	public void beginUnlock(ISafe safe, EntityPlayer epl) {
		TileEntity sfte = (TileEntity)safe;
		if (sfte.worldObj.isRemote)
			epl.openGui(Boxes.instance, 3, sfte.worldObj, sfte.xCoord, sfte.yCoord, sfte.zCoord);
	}

	@Override
	public boolean canConnectWith(ISafe self, ISafe remote) {
		return Arrays.equals(self.getMechTag().getIntArray("combination"), remote.getMechTag().getIntArray("combination"));
	}

	@Override
	public void render(ISafe safe, RenderPass pass, boolean stacked) {
		switch(pass){
		case SafeDoor:
			GL11.glTranslatef(5F, stacked ? 16F:8F, 1F);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			
			TileEntityRenderer.instance.renderEngine.bindTexture(SafeTESR.texDial);
			SafeTESR.INSTANCE.sModel.renderPart("ComboBack");
			for (int i=0; i<3; i++){
				GL11.glPushMatrix();
				GL11.glTranslatef(-0.75F*(float)i, 0F, 0F);
				GL11.glRotatef(-36*(dispCombination[i]), 1F, 0, 0);
				SafeTESR.INSTANCE.sModel.renderPart("Wheel_Sides");
				SafeTESR.INSTANCE.sModel.renderPart("Wheel_Num");
				GL11.glPopMatrix();
			}
			break;
		}
	}
}
