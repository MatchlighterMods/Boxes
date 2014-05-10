package ml.boxes.client;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import ml.boxes.Boxes;
import ml.boxes.Registry;
import ml.boxes.client.gui.GuiBox;
import ml.boxes.data.Box;
import ml.boxes.data.ItemBoxContainer;
import ml.boxes.inventory.ContainerBox;
import ml.boxes.inventory.ContentTip;
import ml.boxes.item.ItemBox;
import ml.core.vec.GeoMath;
import ml.core.vec.Rectangle;
import ml.core.vec.Vector2i;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ContentTipHandler implements ITickHandler {

	public static List<String> inventoryBlacklist = new ArrayList<String>();
	static {
		//inventoryBlacklist.add(InventoryCrafting.class.getName());
		inventoryBlacklist.add(InventoryCraftResult.class.getName());
	}
	
	public static ContentTip openTip;
	
	private static Slot hoverSlot;
	private static long tickerTime = 0;

	private static Rectangle gcBounds = new Rectangle(0, 0, 0, 0);
	
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {

	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		if (type.contains(TickType.RENDER) && !Boxes.neiInstalled){ //NEI Provides a better place for doing this. Use it if we can
			Minecraft mc = FMLClientHandler.instance().getClient();
			if (mc.currentScreen instanceof GuiContainer){
				Vector2i m = GeoMath.getScaledMouse();
				GL11.glPushMatrix();
				GL11.glTranslatef(0F, 0F, 200F);
				renderContentTip(mc, m.x, m.y, (Float)tickData[0]);
				GL11.glPopMatrix();
			}
		}else if (type.contains(TickType.CLIENT)){
			updateCurrentTip();
		}
	}
	
	// Do calculations for the tip
	private static void updateCurrentTip(){
		Minecraft mc = FMLClientHandler.instance().getClient();
		if (mc.currentScreen instanceof GuiContainer){
			GuiContainer asGuiContainer = (GuiContainer)mc.currentScreen;

			int guiXSize = ObfuscationReflectionHelper.getPrivateValue(GuiContainer.class, (GuiContainer)mc.currentScreen, "field_" + "74194_b", "xSize", "b");
			int guiYSize = ObfuscationReflectionHelper.getPrivateValue(GuiContainer.class, (GuiContainer)mc.currentScreen, "field_" + "74195_c", "ySize", "c");

			Vector2i m = GeoMath.getScaledMouse();

			gcBounds.width = asGuiContainer.width;
			gcBounds.height = asGuiContainer.height;
			
			gcBounds.xCoord = (asGuiContainer.width - guiXSize) / 2;
			gcBounds.yCoord = (asGuiContainer.height - guiYSize) / 2;
			
			if (openTip == null || !openTip.revalidate(m.x, m.y)){
				openTip = null;
				
				boolean thoverSlot = false;
				//for (Object objSlt : asGuiContainer.inventorySlots.inventorySlots){
					Slot slt = asGuiContainer.getSlotAtPosition(m.x, m.y); //(Slot)objSlt;
					//if (GeoMath.pointInRect(m.x, m.y, gcBounds.xCoord + slt.xDisplayPosition, gcBounds.yCoord + slt.yDisplayPosition, 16, 16)){
						thoverSlot = true;
						if (hoverSlot != slt) {
							tickerTime = Minecraft.getSystemTime();
						}
						hoverSlot = slt;
					//}
				//}
				if (!thoverSlot)
					hoverSlot = null;
			}
			
			if (hoverSlot != null &&
					hoverSlot.getHasStack() &&
					(hoverSlot.getStack().getItem() instanceof ItemBox) &&
					(!Registry.config.shiftForTip || GuiScreen.isShiftKeyDown()) &&
					(Minecraft.getSystemTime() - tickerTime > Registry.config.tipReactionTime || GuiScreen.isShiftKeyDown()) &&
					!(asGuiContainer instanceof GuiBox && ((ContainerBox)asGuiContainer.inventorySlots).box instanceof ItemBoxContainer && mc.thePlayer.inventory.currentItem == hoverSlot.getSlotIndex()) && //((ItemIBox)((ContainerBox)asGuiContainer.inventorySlots).box).stack == hoverSlot.getStack()
					openTip == null
					)
			{
				ItemBoxContainer iib = getItemIBox();
				Box bd = iib.getBox();
				
				if (bd.canOpenContentTip() && (bd.canOpenContentPreview() || canBeInteractive(mc))){
					openTip = bd.createContentTip(hoverSlot, gcBounds);
				}
			}
			if (openTip != null){
				openTip.interacting = canBeInteractive(mc);
				openTip.tick(mc);
			}
		}
	}
	
	public static boolean revalidateCurrentTip(int mx, int my){
		if (openTip != null && openTip.revalidate(mx, my)){
			return true;
		}
		openTip = null;
		return false;
	}
	
	public static boolean canBeInteractive(Minecraft mc){
		return (Boxes.neiInstalled && GuiScreen.isShiftKeyDown() &&
				!inventoryBlacklist.contains(hoverSlot.inventory.getClass().getName()) &&
				!(hoverSlot instanceof SlotCrafting) ); // && hoverSlot.inventory instanceof InventoryPlayer);
	}

	private static ItemBoxContainer getItemIBox(){
		return new ItemBoxContainer(hoverSlot.getStack());
	}

	//Render the tip
	public static void renderContentTip(Minecraft mc, int mx, int my, float tickTime){
		if (revalidateCurrentTip(mx, my)){
			openTip.renderTick(mc, mx, my);
		}
	}
	
	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.RENDER, TickType.CLIENT);
	}

	@Override
	public String getLabel() {
		return "Boxes";
	}
}
