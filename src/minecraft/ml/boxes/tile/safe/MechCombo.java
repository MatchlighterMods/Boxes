package ml.boxes.tile.safe;

import java.util.Arrays;

import ml.boxes.Boxes;
import ml.boxes.client.render.tile.SafeTESR;
import ml.boxes.tile.TileEntitySafe;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import org.lwjgl.opengl.GL11;

public class MechCombo extends SafeMechanism {
	public class combomech extends MechanismInstance {
	
		public static final int COMBO_LENGTH = 3;
		
		public int[] combination;
		public int[] dispCombination;
		
		public combomech(TileEntitySafe tsafe) {
			super(tsafe);
			combination = new int[COMBO_LENGTH];
		}
		
		@Override
		public NBTTagCompound saveNBT() {
			NBTTagCompound tag = new NBTTagCompound();
			tag.setIntArray("combination", combination);
			tag.setIntArray("dispCombo", dispCombination);
			return tag;
		}
		@Override
		public void loadNBT(NBTTagCompound mechKey) {
			combination = mechKey.getIntArray("combination");
			dispCombination = mechKey.getIntArray("dispCombo");
			
			if (combination.length != COMBO_LENGTH) combination = new int[3];
			if (dispCombination.length != COMBO_LENGTH) dispCombination = new int[3];
		}
		
		@Override
		public NBTTagCompound writeNBTPacket() {
			NBTTagCompound tag = new NBTTagCompound();
			tag.setIntArray("dispCombo", dispCombination);
			return tag;
		}
	
		@Override
		public void beginUnlock(EntityPlayer epl) {
			if (safe.worldObj.isRemote)
				epl.openGui(Boxes.instance, 3, safe.worldObj, safe.xCoord, safe.yCoord, safe.zCoord);
		}
	
		@Override
		public boolean canConnectWith(MechanismInstance tmech) {
			return Arrays.equals(((combomech)tmech).combination, this.combination);
		}
	
		@Override
		public void render(RenderPass pass, boolean stacked) {
			switch(pass){
			case SafeDoor:
				GL11.glTranslatef(5F, stacked ? 16F:8F, 1F);
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				
				TileEntityRenderer.instance.renderEngine.bindTexture("/mods/Boxes/textures/models/dials.png");
				SafeTESR.instance.sModel.renderPart("ComboBack");
				for (int i=0; i<3; i++){
					GL11.glPushMatrix();
					GL11.glTranslatef(-0.75F*(float)i, 0F, 0F);
					GL11.glRotatef(-36*(dispCombination[i]), 1F, 0, 0);
					SafeTESR.instance.sModel.renderPart("Wheel_Sides");
					SafeTESR.instance.sModel.renderPart("Wheel_Num");
					GL11.glPopMatrix();
				}
				break;
			}
		}

		@Override
		public SafeMechanism getSingleton() {
			return MechCombo.this;
		}
	}

	@Override
	public MechanismInstance createInstance(TileEntitySafe tsafe) {
		return new combomech(tsafe);
	}

	@Override
	public ItemStack decraftGetMechStack(NBTTagCompound safeMechData) {
		// TODO Auto-generated method stub
		return null;
	}
}