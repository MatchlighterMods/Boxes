package ml.boxes.window;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import ml.core.gui.GuiRenderUtils;
import ml.core.gui.controls.GuiControl;
import ml.core.gui.controls.button.ControlButton;
import ml.core.gui.core.GuiElement;
import ml.core.gui.core.Window;
import ml.core.gui.event.EventButtonPressed;
import ml.core.gui.event.EventKeyPressed;
import ml.core.gui.event.EventMouseDown;
import ml.core.gui.event.EventMouseMove;
import ml.core.gui.event.EventMouseUp;
import ml.core.gui.event.GuiEvent;
import ml.core.vec.Vector2i;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemDye;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class WindowCombo extends Window {
	
	public WindowCombo(EntityPlayer epl, Side side) {
		super(epl, side);
		
		setSize(96, 72);
	}

	@Override
	public void initControls() {
		new ControlWheel(this, new Vector2i(12, 4));
		new ControlWheel(this, new Vector2i(38, 4));
		new ControlWheel(this, new Vector2i(64, 4));
	}

	@Override
	public boolean canInteractWith(EntityPlayer epl) {
		return true;
	}

	@Override
	public void handleEvent(GuiEvent evt) {
		if (evt instanceof EventKeyPressed) {
			EventKeyPressed ekp = (EventKeyPressed)evt;
			if (ekp.key == Keyboard.KEY_RETURN) {
				//TODO Send Unlock Packet
			}
		}
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
			}
			value %= 16;
			while (value < 0) value += 16;
		}
		
	}
}
