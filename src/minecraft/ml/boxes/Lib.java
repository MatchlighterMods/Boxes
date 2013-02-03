package ml.boxes;

import java.util.Map;

import org.lwjgl.input.Mouse;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class Lib {
	
	private static String[] suffixes = {"k", "M", "G", "T", "P"};
	
	public static boolean isRealPlayer(EntityPlayer pl){
		Package pkg = pl.getClass().getPackage();
		if (!pkg.getName().contains(".") || pkg.getName().contains("net.minecraft"))
			return true;
		return false;
	}

	public static Boolean pointInRect(int pntX, int pntY, int rectX, int rectY, int rectW, int rectH){
		return (pntX >= rectX && pntY >= rectY && pntX <= rectX+rectW && pntY <= rectY + rectH);
	}
	
	public static boolean pointInRect(int pntX, int pntY, rectangle r){
		return pointInRect(pntX, pntY, r.xCoord, r.yCoord, r.width, r.height);
	}
	
	public static XYPair determineBestGrid(int elements){
		if (elements == 0)
			return new XYPair(0, 0);
		int a = (int)Math.round(Math.sqrt(elements));
		int b = (int)Math.ceil(((float)elements)/a);
		return a>b ? new XYPair(a, b) : new XYPair(b, a);
	}
	
	public static class rectangle {
		public int xCoord;
		public int yCoord;
		public int width;
		public int height;
		
		public rectangle(int x, int y, int width, int height) {
			this.xCoord = x;
			this.yCoord = y;
			this.width = width;
			this.height = height;
		}
	}
	
	public static ItemStack getEquivVanillaDye(ItemStack is){
		for (int i=0; i<ItemDye.dyeColorNames.length; i++){
			if (OreDictionary.getOreID(is) == OreDictionary.getOreID(new ItemStack(Item.dyePowder, 1, i))){
				return new ItemStack(Item.dyePowder, 1, i);
			}
		}
		return null;
	}
	
	public static XYPair getScaledMouse(){
		Minecraft mc = FMLClientHandler.instance().getClient();
		ScaledResolution var13 = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
		int var14 = var13.getScaledWidth();
		int var15 = var13.getScaledHeight();
		int adjMouseX = Mouse.getX() * var14 / mc.displayWidth;
		int adjMouseY = var15 - Mouse.getY() * var15 / mc.displayHeight - 1;
		
		return new XYPair(adjMouseX, adjMouseY);
	}
	
	public static String toGroupedString(float n, int p){
		String suffix = "";
		for (int i=suffixes.length; i>0; i--){
			if (n / Math.pow(1000, i) >= 1){
				suffix = suffixes[i-1];
				n /= Math.pow(1000, i);
				break;
			}
		}
		n = (float)(Math.round(n*Math.pow(10, p))/Math.pow(10, p));
		String ns = ""+n;
		return ""+(ns.replaceAll("\\.?[0]*$", ""))+suffix;
	}
	
	public static class XYPair {
		public int X;
		public int Y;
		
		public XYPair(int x, int y) {
			this.X = x;
			this.Y = y;
		}
		
		@Override
		public String toString(){
			return "X: " + X + ", Y: " + Y;
		}
	}
}
