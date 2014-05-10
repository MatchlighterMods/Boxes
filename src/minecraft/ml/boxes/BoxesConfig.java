package ml.boxes;

import ml.core.data.Config;
import net.minecraftforge.common.Configuration;

public class BoxesConfig extends Config {
	
	public BoxesConfig(Configuration fcfg) {
		super(fcfg);
	}

	public @Prop(category=Configuration.CATEGORY_BLOCK) int boxBlockID = 540;
	public @Prop(category=Configuration.CATEGORY_BLOCK) int generalBlockID = 541;
	
	@Category(value=Configuration.CATEGORY_ITEM, comment="Note that ItemIDs are NOT shifted by 256 like in many other mods. The IDs you set below will be the exact IDs as ingame.")
	public @Prop(category=Configuration.CATEGORY_ITEM) int materialsItemID = 3000;
	
	
	//Boxes
	@Category("boxes")
	public @Prop(comment="Set to true to require the use of the Shift key to show the content tip") boolean shiftForTip = false;
	public @Prop(comment="The number of Milliseconds that you need to hover over a box item before it shows its contents tip") int tipReactionTime = 200;
	
	private static String[] defaultClassBlacklist = {
		"backpack.item.ItemBackpackBase", //Backpacks Mod
		"thermalexpansion.block.strongbox.ItemBlockStrongbox", //ThermalExpansion Strongboxes
		"mrtjp.projectred.exploration.ItemBackpack" //ProjectRed BackPacks
	};
	
	public @Prop(comment="Setting this to false will ignore the blacklist option and use the default list.") boolean boxes_blacklistCustom = false;
	public @Prop(comment="Java class names for items that will be disallowed in boxes. Delete to restore/update default.") String[] boxes_blacklist = defaultClassBlacklist;
	
	@Category("boxes.appearance")
	public @Prop(comment="When set to true, boxes will render like a map when opened in hand") boolean enableMapStyleRendering = true;
	public @Prop(comment="When set to true, map-style rendering will make the camera look down into the box") boolean mapRenderingView = false;
	
	@Category("boxes.tweaks")
	public @Prop() boolean allowCardboardDungeonSpawn = true;
	public @Prop() boolean allowCardboardBlackSmithSpawn = true;
	
	
	//Crates
	@Category("crate")
	public @Prop() boolean crate_allowCrafting = true;
	public @Prop(comment="For rendering items in crates. Higher settings can significantly impact performance\n" +
			"0) Always render items in 2D. 1) Render above and below 3D. 2) Render all 3D") int crate_ItemRenderMode = 0;
	public @Prop(comment="For rendering blocks in crates. Does not impact performance.\n" +
			"0) Always render blocks in 2D. 1) Render above and below 3D. 2) Render all 3D") int crate_BlockRenderMode = 1;
	public @Prop(comment="0) Use Minecraft's Fancy/Fast setting. 1) Fast 2) Fancy") int crate_RenderMode = 0;
	
	
	//Safes
	@Category(value="lockbox")
	public @Prop() boolean lockbox_allowCrafting = true;
	public @Prop(comment="Use steel for safes if it is available") boolean lockbox_useSteel = true;
	public @Prop() int keyItemID = 3001;
	public @Prop() int keyMechanismItemID = 3002;
	public @Prop() int comboMechanismItemID = 3003;
	
	
	//DisplayCases
	@Category(value="dispcase")
	public @Prop() boolean dispcase_allowCrafting = true;
	
	
	@Override
	public Config load() {
		super.load();
		
		if (!boxes_blacklistCustom)
			boxes_blacklist = defaultClassBlacklist;
		
		return this;
	}
	
}
