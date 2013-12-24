package ml.boxes.tile.safe;

import java.util.Arrays;
import java.util.List;

import ml.boxes.Boxes;
import ml.boxes.api.safe.ISafe;
import ml.boxes.api.safe.SafeMechanism;
import ml.boxes.client.render.tile.SafeTESR;
import ml.boxes.item.ItemMechs;
import ml.core.ChatUtils;
import ml.core.StringUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class MechCombo extends SafeMechanism {

	public static final int COMBO_LENGTH = 3;
	public static final String comboTagName = "combination";
	
	@Override
	public String getMechId() {
		return "ml.combo";
	}
	
	@Override
	public String getUnlocalizedMechName() {
		return "item.mechanism.combo";
	}
	
	public static void addInfo(NBTTagCompound mechTag, List infos) {
		infos.add("Combination:");
		int[] combo = mechTag.getIntArray(comboTagName);
		String inf = "";
		for (int i=0; i<combo.length; i++) {
			int c = combo[i];
			inf += " "+ChatUtils.getColorStringFromDye(c)+StringUtils.getLColorName(c);
		}
		infos.add(inf);
	}
	
	@Override
	public void addInfoForSafe(NBTTagCompound mechTag, List lst) {
		addInfo(mechTag, lst);
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
		if (!sfte.worldObj.isRemote) {
			epl.openGui(Boxes.instance, 12, sfte.worldObj, sfte.xCoord, sfte.yCoord, sfte.zCoord);
		}
	}

	@Override
	public boolean canConnectWith(ISafe self, ISafe remote) {
		return Arrays.equals(self.getMechTag().getIntArray(comboTagName), remote.getMechTag().getIntArray(comboTagName));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void render(NBTTagCompound mechTag, RenderPass pass, boolean stacked) {
		switch(pass){
		case SafeDoor:
			GL11.glTranslatef(5F, stacked ? 16F:8F, 1F);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			
			Minecraft.getMinecraft().renderEngine.bindTexture(SafeTESR.texDial);
			SafeTESR.INSTANCE.sModel.renderPart("ComboBack");
			int[] dispCombination = mechTag.getIntArray("dispCombo");
			for (int i=0; i<dispCombination.length; i++){
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

	@Override
	public boolean itemMatches(ItemStack itm) {
		return (itm.getItem() instanceof ItemMechs && itm.getItemDamage() == ItemMechs.MECH_COMBO_META);
	}

	@Override
	public ItemStack itemFromMech(NBTTagCompound mechData) {
		// TODO Auto-generated method stub
		return null;
	}
}
