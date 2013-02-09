package ml.boxes;

import ml.boxes.block.BlockBox;
import ml.boxes.item.ItemBox;
import ml.boxes.item.ItemCardboard;
import ml.boxes.network.PacketHandler;
import ml.boxes.recipe.RecipeBox;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid="Boxes", name="Boxes", dependencies="required-after:Forge@[6.5,)")
@NetworkMod(versionBounds="[1.0,)", clientSideRequired=true, serverSideRequired=false, channels={"Boxes"}, packetHandler=PacketHandler.class)
public class Boxes {
	
	@SidedProxy(serverSide="ml.boxes.CommonProxy", clientSide="ml.boxes.client.ClientProxy")
	public static CommonProxy proxy;
	@Instance("Boxes")
	public static Boxes instance;
	public static Boolean neiInstalled = false;
	
	public static BlockBox BlockBox;
	public static ItemCardboard ItemCardboard;
	
	public static int boxRendererID = -1;
//	public static int boxBlockID = 540;
//	public static int cardboardItemID = 3000;
//	
//	public static Boolean shiftForTip = false;
//	public static int tipReactionTime = 100;
	
	public static CreativeTabs BoxTab = new BoxesCreativeTab("boxes");
	
	public static BoxesConfig config = new BoxesConfig();
	
	@PreInit
	public void PreInit(FMLPreInitializationEvent evt){
		Configuration cfg = new Configuration(evt.getSuggestedConfigurationFile());
		
//		try {
//			cfg.load();
//			boxBlockID = cfg.get(Configuration.CATEGORY_BLOCK, "BoxBlockID"	, boxBlockID).getInt();
//			cardboardItemID = cfg.get(Configuration.CATEGORY_ITEM, "CardboardItemID", cardboardItemID).getInt();
//			
//			Property ShiftForTip = cfg.get(Configuration.CATEGORY_GENERAL, "requireShiftForContentTip", shiftForTip);
//			ShiftForTip.comment = "Set to true to require the use of the Shift key to show the content tip";
//			shiftForTip = ShiftForTip.getBoolean(shiftForTip);
//			
//			Property TipReactionTime = cfg.get(Configuration.CATEGORY_GENERAL, "tipReactionTime", tipReactionTime);
//			TipReactionTime.comment = "The number of Milliseconds that you need to hover over a box item befor it shows its contents tip";
//			tipReactionTime = TipReactionTime.getInt(tipReactionTime);
//			
//		} catch (Exception e) {
//			FMLLog.log(Level.SEVERE, e, "Boxes had an error loading its configuration.");
//		} finally {
//			cfg.save();
//		}
		
		config.load(cfg);
	}
	
	@Init
	public void Init(FMLInitializationEvent evt){
		GameRegistry.registerTileEntity(TileEntityBox.class, "box");
		
		this.BlockBox = new BlockBox(config.boxBlockID);
		GameRegistry.registerBlock(this.BlockBox, ItemBox.class, "box");
		
		this.ItemCardboard = new ItemCardboard(config.cardboardItemID-256);
		GameRegistry.registerItem(ItemCardboard, "cardboard");
		
		LanguageRegistry.instance().addStringLocalization("item.box.name", "en_US", "Box");
		LanguageRegistry.instance().addStringLocalization("item.cardboard.name", "en_US", "Cardboard Sheet");
		LanguageRegistry.instance().addStringLocalization("itemGroup.boxes", "en_US", "Boxes");
		
		NetworkRegistry.instance().registerGuiHandler(instance, proxy);
		
		GameRegistry.addRecipe(new ItemStack(ItemCardboard, 1), "ppp", 'p', Item.paper);
		//GameRegistry.addRecipe(new RecipeBox());
		
		proxy.load();
	}
	
}
