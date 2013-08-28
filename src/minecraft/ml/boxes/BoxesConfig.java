package ml.boxes;

import ml.core.Config;
import ml.core.Config.Prop.Renamed;
import net.minecraftforge.common.Configuration;

public class BoxesConfig extends Config {

	public @Prop(category=Configuration.CATEGORY_BLOCK) int boxBlockID = 540;
	public @Prop(category=Configuration.CATEGORY_BLOCK) int generalBlockID = 541;
	
	@Renamed("cardboardItemID")
	public @Prop(category=Configuration.CATEGORY_ITEM) int materialsItemID = 3000;
	public @Prop(category=Configuration.CATEGORY_ITEM) int keyItemID = 3001;
	public @Prop(category=Configuration.CATEGORY_ITEM) int mechsItemID = 3002;
	
	public @Prop(comment="Set to true to require the use of the Shift key to show the content tip") boolean shiftForTip = false;
	public @Prop(comment="The number of Milliseconds that you need to hover over a box item befor it shows its contents tip") int tipReactionTime = 200;
	
	public @Prop(comment="When set to true, boxes will render like a map when opened in hand", category="appearance") boolean enableMapStyleRendering = true;
	public @Prop(comment="When set to true, map-style rendering will make the camera look down into the box", category="appearance") boolean mapRenderingView = false;
	
	@Renamed("appearance.crateItemRenderMode")
	public @Prop(comment="For rendering items in crates. Higher settings can significantly impact performance\n" +
			"0) Always render items in 2D. 1) Render above and below 3D. 2) Render all 3D", category="crate") int crate_ItemRenderMode = 0;
	@Renamed("appearance.crateBlockRenderMode")
	public @Prop(comment="For rendering blocks in crates. Does not impact performance.\n" +
			"0) Always render blocks in 2D. 1) Render above and below 3D. 2) Render all 3D", category="crate") int crate_BlockRenderMode = 1;
	public @Prop(comment="0) Use Minecraft's Fancy/Fast setting. 1) Fast 2) Fancy", category="crate") int crate_RenderMode = 0;
	
	@Override
	public String getFailMsg() {
		return "Boxes config load error";
	}
}
