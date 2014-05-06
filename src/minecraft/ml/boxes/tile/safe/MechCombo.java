package ml.boxes.tile.safe;

import java.util.Arrays;
import java.util.List;

import ml.boxes.Boxes;
import ml.boxes.Registry;
import ml.boxes.api.safe.ISafe;
import ml.boxes.api.safe.SafeMechanism;
import ml.core.ChatUtils;
import ml.core.item.StackUtils;
import ml.core.util.StringUtils;
import ml.core.world.WorldRenderUtils;
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
		return "mechanism.combo";
	}
	
	@Override
	public void addInfoForStack(NBTTagCompound mechTag, List lst) {
		lst.add("Combination:");
		int[] combo = mechTag.getIntArray(comboTagName);
		String inf = "";
		for (int i=0; i<combo.length; i++) {
			int c = combo[i];
			inf += " "+ChatUtils.getColorStringFromDye(c)+StringUtils.getLColorName(c);
		}
		lst.add(inf);
	}
	
	@Override
	public void addInfoForSafe(NBTTagCompound mechTag, List lst) {
		addInfoForStack(mechTag, lst);
	}
	
	@Override
	public NBTTagCompound writeNBTPacket(ISafe safe) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setIntArray("dispCombo", new int[]{4,8,3}); //safe.getMechTag().getIntArray(comboTagName)); //TODO Make Real
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

	private ItemStack renderStack;
	
	@Override
	@SideOnly(Side.CLIENT)
	public void render(NBTTagCompound mechTag, RenderPass pass, boolean stacked) {
		switch(pass){
		case SafeDoor:
			if (renderStack == null) {
				renderStack = new ItemStack(Registry.itemMechCombination);
				StackUtils.setTag(renderStack, new int[]{14,10,5}, MechCombo.comboTagName); //TODO Random display combo
			}
			
			GL11.glTranslatef(5F, stacked ? 20F:8F, 0.75F);
			GL11.glScalef(12F, 12F, 12F);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			
			WorldRenderUtils.renderItemIntoWorldCenteredAt(renderStack, true);
			
			break;
		}
	}
	
	public int[] getCombo(NBTTagCompound tag) {
		return tag.getIntArray(MechCombo.comboTagName);
	}

	@Override
	public boolean stackIsMech(ItemStack itm) {
		return itm.getItem() == Registry.itemMechCombination;
	}

	@Override
	public ItemStack toItemStack(NBTTagCompound mechData) {
		return StackUtils.create(Registry.itemMechCombination, 1, 0, mechData);
	}
}
