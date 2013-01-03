package ml.boxes;

import java.util.logging.Level;

import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid="Boxes", name="Boxes", dependencies="required-after:Forge@[6.5,)")
@NetworkMod(channels={"Boxes"}, versionBounds="[1.0,)", clientSideRequired=true, serverSideRequired=false, packetHandler=NetworkHandler.class)
public class Boxes {
	
	@SidedProxy(serverSide="ml.boxes.CommonProxy", clientSide="ml.boxes.client.ClientProxy")
	public static CommonProxy proxy;
	@Instance("Boxes")
	public static Boxes instance;
	
	public static BlockBox BlockBox;
	
	public static int boxRendererID = -1;
	public static int boxBlockID = 540;
	
	@PreInit
	public void PreInit(FMLPreInitializationEvent evt){
		Configuration config = new Configuration(evt.getSuggestedConfigurationFile());
		try {
			config.load();
			boxBlockID = config.get(Configuration.CATEGORY_BLOCK, "BlockID"	, boxBlockID).getInt();
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
		LanguageRegistry.instance().addStringLocalization("block.box.name", "en_US", "Box");
		
		
		proxy.load();
	}
	
}
