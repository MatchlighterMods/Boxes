package ml.boxes;

import net.minecraftforge.common.Configuration;
import ml.core.Config;

public class BoxesConfig extends Config {

	public @prop(category=Configuration.CATEGORY_BLOCK) int boxBlockID = 540;
	public @prop(category=Configuration.CATEGORY_BLOCK) int generalBlockID = 541;
	
	public @prop(category=Configuration.CATEGORY_ITEM) int cardboardItemID = 3000;
	public @prop(category=Configuration.CATEGORY_ITEM) int keyItemID = 3001;
	
	public @prop(comment="Set to true to require the use of the Shift key to show the content tip") boolean shiftForTip = false;
	public @prop(comment="The number of Milliseconds that you need to hover over a box item befor it shows its contents tip") Integer tipReactionTime = 200;
	
	public @prop(comment="When set to true, boxes will render like a map when opened in hand", category="appearance") boolean enableMapStyleRendering = true;
	public @prop(comment="When set to true, map-style rendering will make the camera look down into the box", category="appearance") boolean mapRenderingView = true;
	
	public @prop(comment="For rendering items in crates. Higher settings can significantly impact performance\n" +
			"0) Always render items in 2D. 1) Render above and below 3D. 2) Render all 3D", category="appearance") int crateItemRenderMode = 0;
	public @prop(comment="For rendering blocks in crates. Does not impact performance.\n" +
			"0) Always render blocks in 2D. 1) Render above and below 3D. 2) Render all 3D", category="appearance") int crateBlockRenderMode = 1;

	@Override
	public String getFailMsg() {
		return "Boxes config load error";
	}
}
