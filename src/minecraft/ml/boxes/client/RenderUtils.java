package ml.boxes.client;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;

public class RenderUtils {

	public static RenderItem itemRenderer = new RenderItem();
	public static int zLevel = 0;
	
	public static void drawTexturedModalRect(int x, int y, int u, int v, int width, int height)
    {
        float var7 = 0.00390625F;
        float var8 = 0.00390625F;
        Tessellator var9 = Tessellator.instance;
        var9.startDrawingQuads();
        var9.addVertexWithUV((double)(x + 0), (double)(y + height), (double)zLevel, (double)((float)(u + 0) * var7), (double)((float)(v + height) * var8));
        var9.addVertexWithUV((double)(x + width), (double)(y + height), (double)zLevel, (double)((float)(u + width) * var7), (double)((float)(v + height) * var8));
        var9.addVertexWithUV((double)(x + width), (double)(y + 0), (double)zLevel, (double)((float)(u + width) * var7), (double)((float)(v + 0) * var8));
        var9.addVertexWithUV((double)(x + 0), (double)(y + 0), (double)zLevel, (double)((float)(u + 0) * var7), (double)((float)(v + 0) * var8));
        var9.draw();
    }
	
    public static void drawGradientRect(int par1, int par2, int par3, int par4, int par5, int par6)
    {
        float var7 = (float)(par5 >> 24 & 255) / 255.0F;
        float var8 = (float)(par5 >> 16 & 255) / 255.0F;
        float var9 = (float)(par5 >> 8 & 255) / 255.0F;
        float var10 = (float)(par5 & 255) / 255.0F;
        float var11 = (float)(par6 >> 24 & 255) / 255.0F;
        float var12 = (float)(par6 >> 16 & 255) / 255.0F;
        float var13 = (float)(par6 >> 8 & 255) / 255.0F;
        float var14 = (float)(par6 & 255) / 255.0F;
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        Tessellator var15 = Tessellator.instance;
        var15.startDrawingQuads();
        var15.setColorRGBA_F(var8, var9, var10, var7);
        var15.addVertex((double)par3, (double)par2, (double)zLevel);
        var15.addVertex((double)par1, (double)par2, (double)zLevel);
        var15.setColorRGBA_F(var12, var13, var14, var11);
        var15.addVertex((double)par1, (double)par4, (double)zLevel);
        var15.addVertex((double)par3, (double)par4, (double)zLevel);
        var15.draw();
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }
	
	public static void drawStackAt(Minecraft mc, int x, int y, ItemStack is){
		GL11.glEnable(GL11.GL_DEPTH_TEST);
        itemRenderer.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.renderEngine, is, x, y);
        itemRenderer.renderItemOverlayIntoGUI(mc.fontRenderer, mc.renderEngine, is, x, y);
	}
	
	public static void drawSpecialStackAt(Minecraft mc, int x, int y, ItemStack is, String str){
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		ItemStack tis = is.copy();
		tis.stackSize = 1;
        itemRenderer.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.renderEngine, tis, x, y);
        itemRenderer.renderItemOverlayIntoGUI(mc.fontRenderer, mc.renderEngine, tis, x, y);
        
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        mc.fontRenderer.drawStringWithShadow(str, x + 19 - 2 - mc.fontRenderer.getStringWidth(str), y + 6 + 3, 16777215);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
	}
}
