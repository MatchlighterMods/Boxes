package ml.boxes.window;

import java.util.ArrayList;
import java.util.List;

import ml.boxes.network.packets.PacketComboEntered;
import ml.boxes.tile.TileEntitySafe;
import ml.core.gui.GuiRenderUtils;
import ml.core.gui.controls.GuiControl;
import ml.core.gui.controls.button.ControlButton;
import ml.core.gui.core.GuiElement;
import ml.core.gui.core.Window;
import ml.core.gui.event.EventButtonPressed;
import ml.core.gui.event.EventKeyPressed;
import ml.core.gui.event.GuiEvent;
import ml.core.gui.event.mouse.EventMouseDown;
import ml.core.gui.event.mouse.EventMouseMove;
import ml.core.gui.event.mouse.EventMouseScroll;
import ml.core.gui.event.mouse.EventMouseUp;
import ml.core.vec.Vector2i;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;

import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class WindowCombo extends Window {
	
	public TileEntitySafe tes;
	public List<ControlWheel> wheels = new ArrayList<WindowCombo.ControlWheel>();
	public ControlButton unlockBtn;
	
	public WindowCombo(EntityPlayer epl, Side side, TileEntitySafe tes) {
		super(epl, side);
		setSize(96, 96);
		this.tes = tes;
	}

	@Override
	public void initControls() {
		wheels.clear();
		wheels.add(new ControlWheel(this, new Vector2i(12, 4)));
		wheels.add(new ControlWheel(this, new Vector2i(38, 4)));
		wheels.add(new ControlWheel(this, new Vector2i(64, 4)));
		unlockBtn = new ControlButton(this, new Vector2i(12, 72), new Vector2i(72,20), "Unlock");
	}

	@Override
	public boolean canInteractWith(EntityPlayer epl) {
		return true;
	}

	@Override
	public void handleEvent(GuiEvent evt) {
		if ((evt instanceof EventKeyPressed && ((EventKeyPressed)evt).key == Keyboard.KEY_RETURN) ||
				(evt instanceof EventButtonPressed && evt.origin == unlockBtn)) {
			ArrayList<Integer> combo = new ArrayList<Integer>();
			for (ControlWheel cw : wheels) {
				combo.add(cw.value);
			}
			close();
			PacketDispatcher.sendPacketToServer(new PacketComboEntered(player, tes, StringUtils.join(combo, "-")).convertToPkt250());
		}
	}
	
	@Override
	public ItemStack transferStackFromSlot(EntityPlayer epl, Slot slot) {
		return null;
	}
	
	public static class ControlWheel extends GuiControl {

		protected ControlButton wheelPlus;
		protected ControlButton wheelMinus;
		public int value = 0;
		
		public ControlWheel(GuiElement parent, Vector2i position) {
			super(parent, position, new Vector2i(20, 64));
			
			wheelPlus = new ControlButton(this, new Vector2i(0, 0), new Vector2i(20, 20), "+");
			wheelMinus = new ControlButton(this, new Vector2i(0, 44), new Vector2i(20, 20), "-");
		}
		
		@Override
		@SideOnly(Side.CLIENT)
		public void drawBackground() {
			bindStyleTexture("swatch");
			int color = ItemDye.dyeColors[value];
			
			float red = ((color >> 16) & 0xFF)/255F;
			float green = ((color >> 8) & 0xFF)/255F;
			float blue = (color & 0xFF)/255F;

			GL11.glColor4f(red, green, blue, 1.0F);
			GuiRenderUtils.drawTexturedModalRect(0, 22, 0, 20, 20, 20, 20, 60);
		}
		
		private Vector2i dragStart;
		private int ivalue;
		
		private int bdir;
		private int tcks;
		
		@Override
		public void guiTick() {
			if (bdir != 0) {
				tcks += 1;
				tcks %= 5;
				if (tcks == 0) {
					value += bdir;
					value %= 16;
					while (value < 0) value += 16;
				}
			}
			super.guiTick();
		}
		
		@Override
		public void handleEvent(GuiEvent evt) {
			int dif = 0;
			if (evt instanceof EventButtonPressed) {
				if (evt.origin == wheelPlus) {
					value += 1;
				} else if (evt.origin == wheelMinus) {
					value -= 1;
				}
			} else if (evt instanceof EventMouseDown) {
				EventMouseDown emd = (EventMouseDown)evt;
				ivalue = value;
				tcks = 0;
				if (emd.origin == this) {
					Vector2i lcl = emd.localizePosition();
					if (lcl.x>0 && lcl.x<20 && lcl.y>22 && lcl.y<42) {
						dragStart = lcl;
					}
				} else if (emd.origin == wheelPlus) {
					bdir = 1;
				} else if (emd.origin == wheelMinus) {
					bdir = -1;
				}
				emd.cancel();
			} else if (evt instanceof EventMouseMove && dragStart != null) {
				EventMouseMove emm = (EventMouseMove)evt;
				value = ivalue + (getLocalMousePos().y - dragStart.y)/10;
			} else if (evt instanceof EventMouseUp) {
				bdir = 0;
				dragStart = null;
			} else if (evt instanceof EventMouseScroll && evt.origin == this) {
				value += ((EventMouseScroll)evt).scrollDelta > 0 ? 1 : -1;
			}
			value %= 16;
			while (value < 0) value += 16;
		}
		
	}
}
