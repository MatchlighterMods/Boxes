package ml.boxes.window;

import ml.boxes.client.Icons;
import ml.boxes.network.packets.PacketDescribeSafe;
import ml.boxes.tile.TileEntitySafe;
import ml.core.enums.NaturalSide;
import ml.core.gui.GuiRenderUtils;
import ml.core.gui.controls.inventory.ControlPlayerInventory;
import ml.core.gui.controls.inventory.ControlSlot;
import ml.core.gui.controls.inventory.ControlSlotGrid;
import ml.core.gui.controls.tabs.ControlTabManager;
import ml.core.gui.controls.tabs.ControlTabManager.GuiTab;
import ml.core.gui.core.SlotCycler;
import ml.core.gui.core.Window;
import ml.core.gui.event.EventGuiClosing;
import ml.core.gui.event.GuiEvent;
import ml.core.gui.event.mouse.EventMouseDown;
import ml.core.texture.maps.BasicCustomTextureMap;
import ml.core.vec.Vector2i;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;

public class WindowSafe extends Window {

	public TileEntitySafe tes;
	public IInventory sinv;
	protected ControlPlayerInventory playerGrid;
	protected ControlSlotGrid safeGrid;
	protected SlotCycler scycler;
	
	protected static ResourceLocation bgRes = new ResourceLocation("Boxes:textures/gui/safeGui_lg.png");
	
	public WindowSafe(EntityPlayer epl, Side side, TileEntitySafe sf) {
		super(epl, side);
		
		tes = sf;
		sinv = tes.isConnected() ? new InventoryLargeChest("", ((TileEntitySafe)tes.getConnected()).inventory, tes.inventory) : tes.inventory;
		
		setSize(178, 132 + 18*sinv.getSizeInventory()/9);
		sinv.openChest();
	}

	@Override
	public void initControls() {
		playerGrid = new ControlPlayerInventory(this, player, new Vector2i(8, getSize().y-84));
		ControlTabManager ctm = new ControlTabManager(this, NaturalSide.Right);
		ctm.tabMargin = 20;
		ctm.addChild(new TabLock(ctm));
		
		safeGrid = new ControlSlotGrid(this, new Vector2i(7, 25), 9, sinv) {
			@Override
			protected ControlSlot makeControlSlot(Slot slot) {
				ControlSlot cs = super.makeControlSlot(slot);
				cs.renderBackground = false;
				return cs;
			}
		};
		scycler = new SlotCycler(playerGrid, safeGrid);
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer epl) {
		return sinv.isUseableByPlayer(epl);
	}
	
	@Override
	public void handleEvent(GuiEvent evt) {
		if (evt instanceof EventGuiClosing) {
			sinv.closeChest();
		}
		super.handleEvent(evt);
	}
	
	@Override
	public void drawBackground() {
		GL11.glTranslatef(this.getLocalPosition().x, this.getLocalPosition().y, 0);
		
		bindTexture(bgRes);
		//GuiRenderUtils.drawSlicedRect(0, 16, this.getSize().x, safeGrid.getSize().y+16, 0, 16, 178, 124, 8);
		
		GuiRenderUtils.drawTexturedModalRect(0, 0, 0, 0, this.getSize().x, 25+safeGrid.getSize().y);
		GuiRenderUtils.drawTexturedModalRect(0, 25+safeGrid.getSize().y, 0, 133, this.getSize().x, 7);
		
		bindStyleTexture("window");
		GuiRenderUtils.drawSlicedRect(0,this.getSize().y-16-playerGrid.getSize().y, this.getSize().x, playerGrid.getSize().y+16, 0, 0, 256, 256, 6);
	}
	
	@Override
	public ItemStack transferStackFromSlot(EntityPlayer epl, Slot slot) {
		scycler.tryCycleSlot(slot);
		return null;
	}
	
	protected class TabLock extends GuiTab {

		public TabLock(ControlTabManager ctm) {
			super(ctm);
			this.tabColor = 0xDD3333;
		}
		
		@Override
		public void handleEvent(GuiEvent evt) {
			if (evt instanceof EventMouseDown && evt.origin == this) {
				EventMouseDown evmc = (EventMouseDown)evt;
				getGui().getMinecraft().thePlayer.closeScreen();
				PacketDispatcher.sendPacketToServer(new PacketDescribeSafe.PacketLockSafe(tes).convertToPkt250());
			}
			super.handleEvent(evt);
		}
		
		private Vector2i trg = new Vector2i(24,24);
		@Override
		public Vector2i getTargetSize() {
			return trg.set(treeHasHover() ? 24+8+getGui().getMinecraft().fontRenderer.getStringWidth("Lock") : defaultSize, defaultSize);
		}
		
		@Override
		public void drawBackground() {
			super.drawBackground();
			bindTexture(BasicCustomTextureMap.GUI.resourceLoc);
			GuiRenderUtils.drawTexturedModelRectFromIcon(3, 5, Icons.LOCK, 14, 14);
			
			if (treeHasHover() && getSize().equals(getTargetSize())) {
				getGui().getMinecraft().fontRenderer.drawString("Lock", 24, (24-getGui().getMinecraft().fontRenderer.FONT_HEIGHT)/2+1, 0xFFFFFF, true);
			}
		}
		
	}

}
