package ml.boxes.window;

import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import ml.core.gui.core.Window;

public class WindowCombo extends Window {

	public WindowCombo(EntityPlayer epl, Side side) {
		super(epl, side);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void initControls() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean canInteractWith(EntityPlayer epl) {
		// TODO Auto-generated method stub
		return false;
	}

}
