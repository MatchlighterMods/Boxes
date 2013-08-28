package ml.boxes.client;

import ml.boxes.Boxes;
import ml.boxes.CommonProxy;
import ml.boxes.Icons;
import ml.boxes.client.gui.GuiBox;
import ml.boxes.client.gui.GuiCombination;
import ml.boxes.client.gui.GuiSafe;
import ml.boxes.client.render.NullBlockRenderer;
import ml.boxes.client.render.item.BoxItemRenderer;
import ml.boxes.client.render.item.MetaItemRenderer;
import ml.boxes.client.render.tile.BoxTESR;
import ml.boxes.client.render.tile.CrateTESR;
import ml.boxes.client.render.tile.DisplayCaseTESR;
import ml.boxes.client.render.tile.SafeTESR;
import ml.boxes.inventory.ContainerBox;
import ml.boxes.inventory.ContainerSafe;
import ml.boxes.tile.TileEntityBox;
import ml.boxes.tile.TileEntityCrate;
import ml.boxes.tile.TileEntityDisplayCase;
import ml.boxes.tile.TileEntitySafe;
import ml.core.texture.maps.BasicCustomTextureMap;
import net.minecraft.entity.player.EntityPlayer;
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
		switch (ID) {
		case 1:
		case 2: //Box
			ContainerBox container = (ContainerBox)getServerGuiElement(ID, player, world, x, y, z);
			if (container != null)
				return new GuiBox(container, player);
			break;
		case 3: //Safe
			return new GuiSafe((ContainerSafe)getServerGuiElement(ID, player, world, x, y, z));
			//break;
		}
		return null;
	}

	@Override
	public void load(){		
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBox.class, BoxTESR.instance);
		Boxes.nullRendererID = RenderingRegistry.getNextAvailableRenderId();
		RenderingRegistry.registerBlockHandler(new NullBlockRenderer());
		
		MinecraftForgeClient.registerItemRenderer(Boxes.config.boxBlockID, new BoxItemRenderer());
		TickRegistry.registerTickHandler(new ContentTipHandler(), Side.CLIENT);
		
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCrate.class, CrateTESR.INSTANCE);
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySafe.class, SafeTESR.INSTANCE);
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDisplayCase.class, DisplayCaseTESR.INSTANCE);
		MinecraftForgeClient.registerItemRenderer(Boxes.config.generalBlockID, new MetaItemRenderer());
		
		BasicCustomTextureMap.GUI.addProvider(new Icons());
	}
	
}
