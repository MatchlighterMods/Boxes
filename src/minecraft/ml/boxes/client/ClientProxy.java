package ml.boxes.client;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.client.IItemRenderer.ItemRendererHelper;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import ml.boxes.Boxes;
import ml.boxes.CommonProxy;
import ml.boxes.TileEntityBox;

public class ClientProxy extends CommonProxy {

	private BoxTERenderer BoxTESR;
	
	@Override
	public void load(){
		
		MinecraftForgeClient.preloadTexture("/ml/Boxes/gfx/box.png");
		
		BoxTESR = new BoxTERenderer();
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBox.class, BoxTESR);
		Boxes.boxRendererID = RenderingRegistry.getNextAvailableRenderId();
		MinecraftForgeClient.registerItemRenderer(Boxes.boxBlockID, new BoxRenderer());
	}
	
	public class BoxRenderer implements IItemRenderer, ISimpleBlockRenderingHandler {

		@Override
		public boolean handleRenderType(ItemStack item, ItemRenderType type) {
			switch (type) {
			case ENTITY:
			case EQUIPPED:
			case INVENTORY:
				return true;
			case FIRST_PERSON_MAP:
			}
			return false;
		}

		@Override
		public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item,
				ItemRendererHelper helper) {
			switch (helper) {
			case ENTITY_ROTATION:
			case ENTITY_BOBBING:
			case EQUIPPED_BLOCK:
			case BLOCK_3D:
			case INVENTORY_BLOCK:
				return true;
			}
			return false;
		}

		@Override
		public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
			BoxTESR.setBoxFlaps(0, 0, 0, 0);
			BoxTESR.renderBox();
		}

		//To prevent default rendering.
		@Override
		public void renderInventoryBlock(Block block, int metadata, int modelID,
				RenderBlocks renderer) {}

		@Override
		public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z,
				Block block, int modelId, RenderBlocks renderer) {
			return true;
		}

		@Override
		public boolean shouldRender3DInInventory() {
			return true;
		}

		@Override
		public int getRenderId() {
			return Boxes.boxRendererID;
		}

	}
	
}
