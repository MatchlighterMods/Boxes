package ml.boxes;

import ml.boxes.block.BlockBox;
import ml.boxes.block.BlockMeta;
import ml.boxes.item.ItemBox;
import ml.boxes.item.ItemBoxBlocks;
import ml.boxes.item.ItemKey;
import ml.boxes.item.ItemMechs;
import ml.boxes.item.ItemResources;
import cpw.mods.fml.common.registry.GameRegistry;

public class Registry {

	// Blocks //
	public static BlockBox BlockBox;
	public static BlockMeta BlockMeta;
	
	public static void registerBlocks() {
		BlockBox = new BlockBox(Boxes.config.boxBlockID);
		GameRegistry.registerBlock(BlockBox, ItemBox.class, "box");
		
		BlockMeta = new BlockMeta(Boxes.config.generalBlockID);
		GameRegistry.registerBlock(BlockMeta, ItemBoxBlocks.class, "boxesMeta");
		
	}
	
	// Items //
	public static ItemMechs ItemMechs;
	public static ItemResources ItemResources;
	public static ItemKey ItemKey;
	
	public static void registerItems() {
		ItemResources = new ItemResources(Boxes.config.materialsItemID-256);
		GameRegistry.registerItem(ItemResources, "cardboard");
		
		ItemKey = new ItemKey(Boxes.config.keyItemID-256);
		GameRegistry.registerItem(ItemKey, "key");
		
		ItemMechs = new ItemMechs(Boxes.config.mechsItemID-256);
		GameRegistry.registerItem(ItemMechs, "mechs");
	}

}
