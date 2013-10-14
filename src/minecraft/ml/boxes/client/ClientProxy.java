package ml.boxes.client;

import ml.boxes.Boxes;
import ml.boxes.CommonProxy;
import ml.boxes.Registry;
import ml.boxes.client.gui.GuiBox;
import ml.boxes.client.render.item.BoxItemRenderer;
import ml.boxes.client.render.item.MetaItemRenderer;
import ml.boxes.client.render.tile.BoxTESR;
import ml.boxes.client.render.tile.BoxesBlockRenderer;
import ml.boxes.client.render.tile.CrateTESR;
import ml.boxes.client.render.tile.DisplayCaseTESR;
import ml.boxes.client.render.tile.SafeTESR;
import ml.boxes.inventory.ContainerBox;
import ml.boxes.inventory.ContainerCombo;
import ml.boxes.inventory.ContainerDisplayCase;
import ml.boxes.inventory.ContainerSafe;
import ml.boxes.tile.TileEntityBox;
import ml.boxes.tile.TileEntityCrate;
import ml.boxes.tile.TileEntityDisplayCase;
import ml.boxes.tile.TileEntitySafe;
import ml.boxes.window.WindowSafe;
import ml.core.texture.maps.BasicCustomTextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		
		int aID = ID>>4, subID = ID & 15;
		
		if (aID == 0 && world.getBlockTileEntity(x, y, z) instanceof TileEntitySafe) {
			return new WindowSafe(player, Side.CLIENT).getGui();
		}
		
		Container cont = (Container)getServerGuiElement(ID, player, world, x, y, z);
		if (cont instanceof ContainerBox) {
			return new GuiBox((ContainerBox)cont, player);
			
		} else if (cont instanceof ContainerSafe) {
			//return new GuiSafe((ContainerSafe)getServerGuiElement(ID, player, world, x, y, z));
			
		} else if (cont instanceof ContainerDisplayCase) {
			//return new GuiDisplayCase((ContainerDisplayCase)getServerGuiElement(ID, player, world, x, y, z));
			
		} else if (cont instanceof ContainerCombo) {
			//return new GuiCombination((ContainerCombo)cont);
			
		}
		
		return null;
	}

	@Override
	public void load(){		
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBox.class, BoxTESR.instance);
		
		MinecraftForgeClient.registerItemRenderer(Boxes.config.boxBlockID, new BoxItemRenderer());
		TickRegistry.registerTickHandler(new ContentTipHandler(), Side.CLIENT);
		
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCrate.class, CrateTESR.INSTANCE);
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySafe.class, SafeTESR.INSTANCE);
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDisplayCase.class, DisplayCaseTESR.INSTANCE);
		MinecraftForgeClient.registerItemRenderer(Boxes.config.generalBlockID, new MetaItemRenderer());
		
		Registry.MetaBlockRenderID = RenderingRegistry.getNextAvailableRenderId();
		RenderingRegistry.registerBlockHandler(Registry.MetaBlockRenderID, new BoxesBlockRenderer());
		
		BasicCustomTextureMap.GUI.addProvider(new Icons());
	}
	
}
