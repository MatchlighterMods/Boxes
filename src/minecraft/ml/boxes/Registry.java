package ml.boxes;

import ml.boxes.api.safe.ItemMechanism;
import ml.boxes.block.BlockBox;
import ml.boxes.block.BlockMeta;
import ml.boxes.block.MetaType;
import ml.boxes.item.ItemBox;
import ml.boxes.item.ItemBoxBlocks;
import ml.boxes.item.ItemKey;
import ml.boxes.item.ItemMechCombo;
import ml.boxes.item.ItemMechKey;
import ml.boxes.item.ItemResources;
import ml.boxes.item.ItemType;
import ml.boxes.network.packets.PacketComboEntered;
import ml.boxes.network.packets.PacketDescribeCrate;
import ml.boxes.network.packets.PacketDescribeDisplay;
import ml.boxes.network.packets.PacketDescribeSafe;
import ml.boxes.network.packets.PacketTipClick;
import ml.boxes.network.packets.PacketUpdateData;
import ml.boxes.recipe.RecipeBox;
import ml.boxes.recipe.RecipeComboMech;
import ml.boxes.recipe.RecipeKey;
import ml.boxes.recipe.RecipeKeyMech;
import ml.boxes.recipe.RecipeSafe;
import ml.boxes.tile.TileEntityBox;
import ml.boxes.tile.TileEntityCrate;
import ml.boxes.tile.TileEntityDisplayCase;
import ml.boxes.tile.TileEntitySafe;
import ml.boxes.tile.safe.MechCombo;
import ml.boxes.tile.safe.MechKey;
import ml.boxes.tile.safe.MechRegistry;
import ml.core.network.PacketHandler;
import ml.core.util.DyeUtils;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

public class Registry {

	public static BoxesConfig config;
	
	// Blocks //
	public static BlockBox BlockBox;
	public static BlockMeta BlockMeta;
	
	public static void registerBlocks() {
		BlockBox = new BlockBox(Registry.config.boxBlockID);
		GameRegistry.registerBlock(BlockBox, ItemBox.class, "box");
		
		BlockMeta = new BlockMeta(Registry.config.generalBlockID);
		GameRegistry.registerBlock(BlockMeta, ItemBoxBlocks.class, "boxesMeta");
		
	}
	
	// Items //
	public static ItemResources itemResources;
	public static ItemKey itemKey;
	
	public static ItemMechCombo itemMechCombination;
	public static ItemMechanism itemMechKey;
	
	public static void registerItems() {
		itemResources = new ItemResources(Registry.config.materialsItemID-256);
		GameRegistry.registerItem(itemResources, "cardboard");
		
		itemKey = new ItemKey(Registry.config.keyItemID-256);
		GameRegistry.registerItem(itemKey, "key");
		
		registerSafeMechs();
	}
	
	// SafeMechanisms //
	public static void registerSafeMechs() {
		itemMechCombination = new ItemMechCombo(Registry.config.comboMechanismItemID-256, new MechCombo());
		MechRegistry.registerMechansism(itemMechCombination.safeMechanism);
		GameRegistry.registerItem(itemMechCombination, "comboMech");
		
		itemMechKey = new ItemMechKey(Registry.config.keyMechanismItemID-256, new MechKey());
		MechRegistry.registerMechansism(itemMechKey.safeMechanism);
		GameRegistry.registerItem(itemMechKey, "keyMech");
	}
	
	// TileEntities //
	public static void registerTileEntities() {
		GameRegistry.registerTileEntity(TileEntityBox.class, "box");
		GameRegistry.registerTileEntity(TileEntityCrate.class, "crate");
		GameRegistry.registerTileEntity(TileEntitySafe.class, "safe");
		GameRegistry.registerTileEntity(TileEntityDisplayCase.class, "disp_case");
	}
	
	// Packets //
	public static void registerPackets() {
		PacketHandler pkh = new PacketHandler();
		NetworkRegistry.instance().registerChannel(pkh, Boxes.netChannel);
		
		// Packets
		pkh.addHandler(PacketUpdateData.class);
		pkh.addHandler(PacketTipClick.class);
		
		pkh.addHandler(PacketDescribeCrate.class);
		pkh.addHandler(PacketDescribeSafe.class);
		pkh.addHandler(PacketDescribeSafe.PacketLockSafe.class);
		pkh.addHandler(PacketDescribeDisplay.class);
		pkh.addHandler(PacketComboEntered.class);
	}
	
	// Recipes //
	public static void registerRecipes() {
		GameRegistry.addRecipe(new ItemStack(Registry.itemResources, 1, 0), "ppp", "sws", "ppp", 'p', Item.paper, 's', Item.silk, 'w', Item.bucketWater);
		GameRegistry.addRecipe(ItemType.ISFromType(ItemType.Label, 3), "ppp", " s ", 'p', Item.paper, 's', Item.slimeBall);
		
		GameRegistry.addRecipe(new RecipeBox());
		
		if (config.crate_allowCrafting) {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Registry.BlockMeta, 1, MetaType.Crate.ordinal()), "wsw", "scs", "wsw", 'w', "logWood", 's', "plankWood", 'c', Block.chest));
		}
		
		if (config.lockbox_allowCrafting) {
			GameRegistry.addRecipe(new RecipeSafe());
			GameRegistry.addRecipe(new RecipeKey(" m", "nm", "nm", 'n', Item.goldNugget, 'm', Item.ingotGold));
			GameRegistry.addRecipe(new RecipeKey("k", 'k', itemKey));
			GameRegistry.addRecipe(new RecipeComboMech("iii", "ddd", "iii", 'i', Item.ingotIron, 'd', DyeUtils.getAllDyeStacks()));
			GameRegistry.addRecipe(new RecipeKeyMech("gkg", "n g", "n g", 'g', Item.ingotGold, 'n', Item.goldNugget, 'k', itemKey));
		}
	}
	
	// Renderers // -- Do NOT SideOnly these. Apparently the PathFinder uses them... ?!
	public static int MetaBlockRenderID;
	

}
