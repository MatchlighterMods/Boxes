package ml.boxes.client.render.item;

import ml.boxes.client.render.tile.BoxTESR;
import ml.boxes.data.ItemBoxContainer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BoxItemRenderer implements IItemRenderer {

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		switch (type) {
		case ENTITY:
		case EQUIPPED:
		case INVENTORY:
		case EQUIPPED_FIRST_PERSON:
			return true;
//		case FIRST_PERSON_HOLDING:
//			Minecraft mc = FMLClientHandler.instance().getClient();
//			EntityLivingBase holder = mc.thePlayer;
//			return (Boxes.config.enableMapStyleRendering &&
//					holder instanceof EntityPlayer &&
//					holder == mc.thePlayer &&
//					((EntityPlayer) holder).openContainer instanceof ContainerBox &&
//					((ContainerBox)((EntityPlayer)holder).openContainer).box instanceof ItemBoxContainer &&
//					mc.gameSettings.thirdPersonView == 0 &&
//					!mc.renderViewEntity.isPlayerSleeping() &&
//					!mc.gameSettings.hideGUI &&
//					!mc.playerController.enableEverythingIsScrewedUpMode()
//					);

		case FIRST_PERSON_MAP:
		}
		return false;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item,
			ItemRendererHelper helper) {
		switch (helper) {
		case ENTITY_ROTATION:
		case ENTITY_BOBBING:
		case EQUIPPED_BLOCK:
		case BLOCK_3D:
		case INVENTORY_BLOCK:
//		case HOLD_HANDS:
			return true;
		}
		return false;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		GL11.glPushMatrix();
		switch (type){
		case ENTITY:
			GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
			BoxTESR.instance.setBoxFlaps(5, 2, 0, 0);
			break;
//		case FIRST_PERSON_HOLDING:
//			GL11.glScalef(128F, 128F, 128F);
//			GL11.glTranslatef(0.5F, 0.5F, 0.5F);
//			GL11.glRotatef(-90F, 1F, 0F, 0F);
//			GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
//			BoxTESR.instance.setBoxFlaps(150, 150, 150, 150);
//			break;
		case EQUIPPED_FIRST_PERSON:
		case EQUIPPED:
			BoxTESR.instance.setBoxFlaps(5, 2, 0, 0);
			break;
		case INVENTORY:
			GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
			BoxTESR.instance.setBoxFlaps(0, 0, 0, 0);
			break;
		}
		BoxTESR.instance.renderBox(new ItemBoxContainer(item).getBox().boxColor);
		GL11.glPopMatrix();
	}
}
