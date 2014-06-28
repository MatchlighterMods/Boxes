package ml.boxes;

import ml.boxes.api.ContentBlacklist;
import ml.boxes.integ.BoxesReflectionBlacklist;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;

@Mod(modid="Boxes", name="Boxes", dependencies="required-after:Forge@[6.5,); required-after:MLCore")
@NetworkMod(clientSideRequired=true, serverSideRequired=false) //, channels={PacketHandler.defChan}, packetHandler=PacketHandler.class)
public class Boxes {
	
	@SidedProxy(serverSide="ml.boxes.CommonProxy", clientSide="ml.boxes.client.ClientProxy")
	public static CommonProxy proxy;
	
	@Instance("Boxes")
	public static Boxes instance;
	public static Boolean neiInstalled = false;
	
	public static CreativeTabs BoxTab = new BoxesCreativeTab("boxes");
	
	public static String netChannel = "Boxes";

	@EventHandler
	public void PreInit(FMLPreInitializationEvent evt){
		Configuration cfg = new Configuration(evt.getSuggestedConfigurationFile());
		Registry.config = (BoxesConfig)new BoxesConfig(cfg).load();
	}
	
	@EventHandler
	public void Init(FMLInitializationEvent evt){
		
		Registry.registerTileEntities();
		Registry.registerBlocks();
		Registry.registerItems();
		Registry.registerRecipes();
		
		NetworkRegistry.instance().registerGuiHandler(instance, proxy);
		Registry.registerPackets();
		
		initDungeonLoot();
		initIntegration();
		
		proxy.load();
	}
	
	@EventHandler
	public void PostInit(FMLPostInitializationEvent e) {

	}
	
	private void initDungeonLoot(){
		if (Registry.config.allowCardboardDungeonSpawn)
			ChestGenHooks.addItem(ChestGenHooks.DUNGEON_CHEST, new WeightedRandomChestContent(new ItemStack(Registry.itemResources), 1, 3, 100));
		if (Registry.config.allowCardboardBlackSmithSpawn)
			ChestGenHooks.addItem(ChestGenHooks.VILLAGE_BLACKSMITH, new WeightedRandomChestContent(new ItemStack(Registry.itemResources), 1, 5, 12));
		
		ChestGenHooks.addItem(ChestGenHooks.BONUS_CHEST, new WeightedRandomChestContent(new ItemStack(Registry.itemResources), 1, 4, 7));
	}
	
	private void initIntegration() {
		ContentBlacklist.addFilter(ContentBlacklist.LIST_BOX, new BoxesReflectionBlacklist());
	}
	
}
