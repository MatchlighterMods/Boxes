package ml.boxes.api.box;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public interface IContentTip {

	@SideOnly(Side.CLIENT)
	public void tick(Minecraft mc);

	@SideOnly(Side.CLIENT)
	public void renderTick(Minecraft mc, int mx, int my);

	@SideOnly(Side.CLIENT)
	public boolean handleMouseClick(int mx, int my, int btn);

	@SideOnly(Side.CLIENT)
	public boolean handleKeyPress(char chr, int kc);

	public boolean revalidate();

	public ItemStack getStackAtPosition(int pX, int pY);
	
	public boolean isPointInside(int x, int y);
	
	public boolean isVisible();

}