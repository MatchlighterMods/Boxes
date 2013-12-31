package ml.boxes;

import ml.core.data.Config;
import ml.core.data.Config.Prop.Renamed;
import net.minecraftforge.common.Configuration;

public class BoxesConfig extends Config {
	
	public BoxesConfig(Configuration fcfg) {
		super(fcfg);
	}

	public @Prop(category=Configuration.CATEGORY_BLOCK) int boxBlockID = 540;
	public @Prop(category=Configuration.CATEGORY_BLOCK) int generalBlockID = 541;
	
	public @Renamed("cardboardItemID") @Prop(category=Configuration.CATEGORY_ITEM) int materialsItemID = 3000;
	public @Prop(category="boxes.tweaks") boolean allowCardboardDungeonSpawn = true;
	public @Prop(category="boxes.tweaks") boolean allowCardboardBlackSmithSpawn = true;
	
	public @Prop(category=Configuration.CATEGORY_ITEM) int keyItemID = 3001;
	public @Prop(category=Configuration.CATEGORY_ITEM) int mechsItemID = 3002;
	
	@Renamed("general.")
	public @Prop(comment="Set to true to require the use of the Shift key to show the content tip", category="boxes") boolean shiftForTip = false;
	@Renamed("general.")
	public @Prop(comment="The number of Milliseconds that you need to hover over a box item befor it shows its contents tip", category="boxes") int tipReactionTime = 200;
	
	@Renamed("appearance.")
	public @Prop(comment="When set to true, boxes will render like a map when opened in hand", category="boxes.appearance") boolean enableMapStyleRendering = true;
	@Renamed("appearance.")
	public @Prop(comment="When set to true, map-style rendering will make the camera look down into the box", category="boxes.appearance") boolean mapRenderingView = false;
	
	
	//Crates
	public @Prop(category="crate") boolean crate_allowCrafting = true;
	@Renamed("appearance.crateItemRenderMode")
	public @Prop(comment="For rendering items in crates. Higher settings can significantly impact performance\n" +
			"0) Always render items in 2D. 1) Render above and below 3D. 2) Render all 3D", category="crate") int crate_ItemRenderMode = 0;
	@Renamed("appearance.crateBlockRenderMode")
	public @Prop(comment="For rendering blocks in crates. Does not impact performance.\n" +
			"0) Always render blocks in 2D. 1) Render above and below 3D. 2) Render all 3D", category="crate") int crate_BlockRenderMode = 1;
	public @Prop(comment="0) Use Minecraft's Fancy/Fast setting. 1) Fast 2) Fancy", category="crate") int crate_RenderMode = 0;
	
	//Safes
	public @Prop(category="lockbox") boolean lockbox_allowCrafting = true;
	public @Prop(category="lockbox", comment="Use steel for safes if it is available") boolean lockbox_useSteel = true;
	
	//DisplayCases
	public @Prop(category="dispcase") boolean dispcase_allowCrafting = true;
	
}
