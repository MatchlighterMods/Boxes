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
	public @Prop(category=Configuration.CATEGORY_ITEM) int keyItemID = 3001;
	public @Prop(category=Configuration.CATEGORY_ITEM) int mechsItemID = 3002;
	
	
	//Boxes
	@Category(category="boxes", comment="")
	public @Prop(comment="Set to true to require the use of the Shift key to show the content tip") boolean shiftForTip = false;
	public @Prop(comment="The number of Milliseconds that you need to hover over a box item before it shows its contents tip") int tipReactionTime = 200;
	
	public @Prop(comment="Java class names for items that will be disallowed in boxes.") String[] boxes_blacklist = {
		"backpack.item.ItemBackpackBase", //Backpacks Mod
		"thermalexpansion.block.strongbox.ItemBlockStrongbox", //ThermalExpansion Strongboxes
		"mrtjp.projectred.exploration.ItemBackpack" //ProjectRed BackPacks
	};
	
	
	@Category(category="boxes.appearance")
	public @Prop(comment="When set to true, boxes will render like a map when opened in hand") boolean enableMapStyleRendering = true;
	public @Prop(comment="When set to true, map-style rendering will make the camera look down into the box") boolean mapRenderingView = false;
	
	@Category(category="boxes.tweaks")
	public @Prop() boolean allowCardboardDungeonSpawn = true;
	public @Prop() boolean allowCardboardBlackSmithSpawn = true;
	
	//Crates
	@Category(category="crate")
	public @Prop() boolean crate_allowCrafting = true;
	@Renamed("appearance.crateItemRenderMode")
	public @Prop(comment="For rendering items in crates. Higher settings can significantly impact performance\n" +
			"0) Always render items in 2D. 1) Render above and below 3D. 2) Render all 3D") int crate_ItemRenderMode = 0;
	@Renamed("appearance.crateBlockRenderMode")
	public @Prop(comment="For rendering blocks in crates. Does not impact performance.\n" +
			"0) Always render blocks in 2D. 1) Render above and below 3D. 2) Render all 3D") int crate_BlockRenderMode = 1;
	public @Prop(comment="0) Use Minecraft's Fancy/Fast setting. 1) Fast 2) Fancy") int crate_RenderMode = 0;
	
	//Safes
	@Category(category="lockbox")
	public @Prop() boolean lockbox_allowCrafting = true;
	public @Prop(comment="Use steel for safes if it is available") boolean lockbox_useSteel = true;
	
	//DisplayCases
	@Category(category="dispcase")
	public @Prop() boolean dispcase_allowCrafting = true;
	
}
