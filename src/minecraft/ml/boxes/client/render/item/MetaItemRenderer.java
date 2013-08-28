package ml.boxes.client.render.item;

import ml.boxes.block.MetaType;
import ml.boxes.client.render.tile.CrateTESR;
import ml.boxes.client.render.tile.DisplayCaseTESR;
import ml.boxes.client.render.tile.SafeTESR;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class MetaItemRenderer implements IItemRenderer {

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		switch (type) {
		case ENTITY:
		case EQUIPPED:
		case EQUIPPED_FIRST_PERSON:
		case INVENTORY:
			return true;
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
			break;
		case EQUIPPED_FIRST_PERSON:
		case EQUIPPED:
			break;
		case INVENTORY:
			GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
			break;
		}
		
		switch (MetaType.fromMeta(item.getItemDamage())){
		case Crate:
			CrateTESR.INSTANCE.render();
			break;
		case Safe:
			GL11.glTranslatef(0.5F, 0.5F, 0.5F);
			GL11.glRotatef(type==ItemRenderType.INVENTORY ? -90F : 90F, 0F, 1F, 0F);
			GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
			
			SafeTESR.INSTANCE.renderAsItem();
			break;
		case DisplayCase:
			DisplayCaseTESR.INSTANCE.render(10);
			break;
		}
		
		GL11.glPopMatrix();

	}

}
