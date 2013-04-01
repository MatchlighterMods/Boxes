package ml.boxes;

import ml.boxes.block.BlockBox;
import ml.boxes.block.BlockSafe;
import ml.boxes.item.ItemBox;
import ml.boxes.item.ItemCardboard;
import ml.boxes.network.PacketHandler;
import ml.boxes.recipe.RecipeBox;
import ml.boxes.tile.TileEntityBox;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ChestGenHooks;
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

@Mod(modid="Boxes", name="Boxes", dependencies="required-after:Forge@[6.5,);required-after:MLCore@[0.2,)")
@NetworkMod(versionBounds="[0.2,)", clientSideRequired=true, serverSideRequired=false, channels={"Boxes"}, packetHandler=PacketHandler.class)
public class Boxes {
	
	@SidedProxy(serverSide="ml.boxes.CommonProxy", clientSide="ml.boxes.client.ClientProxy")
	public static CommonProxy proxy;
	@Instance("Boxes")
	public static Boxes instance;
	public static Boolean neiInstalled = false;
	
	public static BlockBox BlockBox;
	public static BlockSafe BlockSafe;
	public static ItemCardboard ItemCardboard;
	
	public static int boxRendererID = -1;
	public static CreativeTabs BoxTab = new BoxesCreativeTab("boxes");
	
	public static BoxesConfig config = new BoxesConfig();
	
	@PreInit
	public void PreInit(FMLPreInitializationEvent evt){
		Configuration cfg = new Configuration(evt.getSuggestedConfigurationFile());
		config.load(cfg);
	}
	
	@Init
	public void Init(FMLInitializationEvent evt){
		GameRegistry.registerTileEntity(TileEntityBox.class, "box");
		
		this.BlockBox = new BlockBox(config.boxBlockID);
		GameRegistry.registerBlock(this.BlockBox, ItemBox.class, "box");
		this.BlockSafe = new BlockSafe(config.safeBlockID);
		GameRegistry.registerBlock(this.BlockSafe, "safe");
		
		this.ItemCardboard = new ItemCardboard(config.cardboardItemID-256);
		GameRegistry.registerItem(ItemCardboard, "cardboard");
		
		LanguageRegistry.instance().addStringLocalization("item.box.name", "en_US", "Box");
		LanguageRegistry.instance().addStringLocalization("tile.safe.name", "en_US", "Lockbox");
		LanguageRegistry.instance().addStringLocalization("item.cardboard.name", "en_US", "Cardboard Sheet");
		LanguageRegistry.instance().addStringLocalization("itemGroup.boxes", "en_US", "Boxes");
		
		NetworkRegistry.instance().registerGuiHandler(instance, proxy);
		
		GameRegistry.addRecipe(new ItemStack(ItemCardboard, 1), "ppp", 'p', Item.paper);
		GameRegistry.addRecipe(new RecipeBox());
		
		initDungeonLoot();
		
		proxy.load();
	}
	
	public void initDungeonLoot(){
		
	}
	
}
