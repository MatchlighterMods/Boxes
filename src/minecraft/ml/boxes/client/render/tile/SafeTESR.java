package ml.boxes.client.render.tile;

import ml.boxes.api.safe.SafeMechanism;
import ml.boxes.api.safe.SafeMechanism.RenderPass;
import ml.boxes.tile.TileEntitySafe;
import ml.boxes.tile.safe.MechRegistry;
import ml.core.block.BlockUtils;
import ml.core.item.StackUtils;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import net.minecraftforge.common.ForgeDirection;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SafeTESR extends TileEntitySpecialRenderer {
	
	public static ResourceLocation texMain = new ResourceLocation("Boxes:textures/models/safe.png");
	public static ResourceLocation texDial = new ResourceLocation("Boxes:textures/models/dials.png");

	public IModelCustom sModel = AdvancedModelLoader.loadModel("/assets/boxes/models/safe.obj");
	public static SafeTESR INSTANCE = new SafeTESR();

	@Override
	public void renderTileEntityAt(TileEntity te, double d0, double d1,
			double d2, float tickTime) {

		TileEntitySafe tes = (TileEntitySafe)te;
		if (tes.linkedDir != ForgeDirection.DOWN){
			boolean renderTall = tes.linkedDir == ForgeDirection.UP;

			GL11.glPushMatrix();
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			GL11.glTranslatef((float)d0, (float)d1, (float)d2);

			BlockUtils.glRotateForFaceDir(tes.facing);

			GL11.glScalef(0.0625F, 0.0625F, 0.0625F);
			this.bindTexture(texMain);
			sModel.renderPart("Safe_Base");
			sModel.renderPart(renderTall ? "Safe_Tall":"Safe_Small");
			if (renderTall) sModel.renderPart("Safe_Shelf");
			
			GL11.glPushMatrix();
			tes.mech.render(tes.getMechTag(), RenderPass.SafeBody, renderTall);
			GL11.glPopMatrix();
			
			this.bindTexture(texMain);
			float doorAng1 = tes.prevDoorAng + (tes.doorAng-tes.prevDoorAng)*tickTime;
			doorAng1 = (float)Math.sin(doorAng1*3.14/2);
			int doorAng = (int)(120 * doorAng1);
			
			GL11.glTranslatef(14.5F, 0F, 1.5F);
			GL11.glRotatef(-doorAng, 0F, 1F, 0F);
			GL11.glTranslatef(-14.5F, 0F, -1.5F);

			sModel.renderPart(renderTall ? "Door_Tall":"Door_Small");

			tes.mech.render(tes.getMechTag(), RenderPass.SafeDoor, renderTall);

			GL11.glPopMatrix();
		}
	}

	public void renderAsItem(ItemStack is){
		
		NBTTagCompound stackTag = StackUtils.getStackTag(is);
		SafeMechanism sm = MechRegistry.getMechForId(stackTag.getString("mech_id"));
		
		GL11.glScalef(0.0625F, 0.0625F, 0.0625F);
		this.bindTexture(texMain);
		sModel.renderPart("Safe_Base");
		sModel.renderPart("Safe_Small");
		sModel.renderPart("Door_Small");
		
		sm.render(stackTag.getCompoundTag("mech_data"), RenderPass.SafeBody, false);
		sm.render(stackTag.getCompoundTag("mech_data"), RenderPass.SafeDoor, false);
	}

}
