package ml.boxes.inventory;

import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;

//TODO Refine
/**
 * This is a little strange. {@link IContentTipProvider}s provide factories. Factories are created and associated with a GuiContainer/ContentTipManager duo.
 * {@link IContentTipProvider#createFactory(GuiContainer)} is called once upon the opening of a Gui. {@link IContentTipFactory#createContentTips(List)} will be called every second.
 * 
 * You can implement both {@link IContentTipProvider} and {@link IContentTipFactory} in the same class if you can generate tips without having to store additional data.
 * @author Matchlighter
 */
public interface IContentTipProvider {

	public IContentTipFactory createFactory(ContentTipManager ctm);
	
	public static interface IContentTipFactory {
		
		/**
		 * @param tips A list to add {@link ContentTip}s to. It also contains the already open tips, so you could use it to prevent from adding duplicate tips
		 */
		public void createContentTips(List<ContentTip> tips);
		
		public boolean revalidateTip(ContentTip tip);
		
	}
}
