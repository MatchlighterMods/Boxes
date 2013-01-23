package ml.boxes;

import java.util.logging.Level;

import ml.boxes.block.BlockBox;
import ml.boxes.client.ClientPacketHandler;
import ml.boxes.item.ItemBox;
import ml.boxes.item.ItemCardboard;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.NetworkMod.SidedPacketHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid="Boxes", name="Boxes", dependencies="required-after:Forge@[6.5,)")
@NetworkMod(versionBounds="[1.0,)", clientSideRequired=true, serverSideRequired=false, serverPacketHandlerSpec=@SidedPacketHandler(channels={"Boxes"}, packetHandler=ServerPacketHandler.class),
clientPacketHandlerSpec=@SidedPacketHandler(packetHandler = ClientPacketHandler.class, channels={"Boxes"}))
public class Boxes {
	
	@SidedProxy(serverSide="ml.boxes.CommonProxy", clientSide="ml.boxes.client.ClientProxy")
	public static CommonProxy proxy;
	@Instance("Boxes")
	public static Boxes instance;
	public static Boolean neiInstalled = false;
	
	public static BlockBox BlockBox;
	public static ItemCardboard ItemCardboard;
	
	public static int boxRendererID = -1;
	public static int boxBlockID = 540;
	public static int cardboardItemID = 3000;
	
	public static Boolean shiftForTip;
	
	public static CreativeTabs BoxTab = new BoxesCreativeTab("boxes");
	
	@PreInit
	public void PreInit(FMLPreInitializationEvent evt){
		Configuration config = new Configuration(evt.getSuggestedConfigurationFile());
		try {
			config.load();
			boxBlockID = config.get(Configuration.CATEGORY_BLOCK, "BoxBlockID"	, boxBlockID).getInt();
			cardboardItemID = config.get(Configuration.CATEGORY_ITEM, "CardboardItemID", cardboardItemID).getInt();
			
			Property ShiftForTip = config.get(Configuration.CATEGORY_GENERAL, "requireShiftForContentTip", false);
			ShiftForTip.comment = "Set to true to require the use of the Shift key to show the content tip";
			shiftForTip = ShiftForTip.getBoolean(false);
			
		} catch (Exception e) {
			FMLLog.log(Level.SEVERE, e, "Boxes had an error loading its configuration.");
		} finally {
			config.save();
		}
	}
	
	@Init
	public void Init(FMLInitializationEvent evt){
		GameRegistry.registerTileEntity(TileEntityBox.class, "box");
		
		this.BlockBox = new BlockBox(boxBlockID);
		GameRegistry.registerBlock(this.BlockBox, ItemBox.class, "box");
		
		this.ItemCardboard = new ItemCardboard(cardboardItemID-256);
		GameRegistry.registerItem(ItemCardboard, "cardboard");
		
		LanguageRegistry.instance().addStringLocalization("item.box.name", "en_US", "Box");
		LanguageRegistry.instance().addStringLocalization("item.cardboard.name", "en_US", "Cardboard Sheet");
		LanguageRegistry.instance().addStringLocalization("itemGroup.boxes", "en_US", "Boxes");
		
		NetworkRegistry.instance().registerGuiHandler(instance, proxy);
		
		GameRegistry.addRecipe(new ItemStack(ItemCardboard, 1), "ppp", 'p', Item.paper);
		GameRegistry.addRecipe(new RecipeBox());
		
		proxy.load();
	}
	
}
