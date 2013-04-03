package ml.boxes.client.render.item;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.jcraft.jorbis.Block;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.ReflectionHelper;
import ml.boxes.Boxes;
import ml.boxes.block.MetaType;
import ml.boxes.client.render.tile.BoxTERenderer;
import ml.boxes.client.render.tile.CrateTESR;
import ml.boxes.data.ItemIBox;
import ml.boxes.inventory.ContainerBox;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Timer;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.client.IItemRenderer.ItemRendererHelper;

public class MetaItemRenderer implements IItemRenderer {

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		switch (type) {
		case ENTITY:
		case EQUIPPED:
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
		case EQUIPPED:
			break;
		case INVENTORY:
			GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
			break;
		}
		
		switch (MetaType.fromMeta(item.getItemDamage())){
		case Crate:
			CrateTESR.instance.render();
			break;
		}
		
		GL11.glPopMatrix();

	}

}
