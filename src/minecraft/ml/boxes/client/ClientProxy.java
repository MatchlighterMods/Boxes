package ml.boxes.client;

import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.MinecraftForgeClient;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import ml.boxes.Boxes;
import ml.boxes.CommonProxy;
import ml.boxes.TileEntityBox;

public class ClientProxy extends CommonProxy {

	public BoxTERenderer BoxTESR;
	
	@Override
	public void load(){
		
		MinecraftForgeClient.preloadTexture("/ml/Boxes/gfx/box.png");
		
		BoxTESR = new BoxTERenderer();
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBox.class, BoxTESR);
		Boxes.boxRendererID = RenderingRegistry.getNextAvailableRenderId();
		MinecraftForgeClient.registerItemRenderer(Boxes.boxBlockID, new BoxRenderer());
	}
}
