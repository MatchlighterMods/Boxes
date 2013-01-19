package ml.boxes.client;

import ml.boxes.BoxData;
import ml.boxes.Boxes;
import ml.boxes.CommonProxy;
import ml.boxes.TileEntityBox;
import ml.boxes.inventory.ContainerBox;
import ml.boxes.item.ItemBox;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

public class ClientProxy extends CommonProxy {

	private BoxTERenderer BoxTESR;
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		switch (ID) {
		case 1:
		case 2: //Box
			ContainerBox container = (ContainerBox)getServerGuiElement(ID, player, world, x, y, z);
			if (container != null)
				return new GuiBox(container , player);
			break;
		}
		return null;
	}

	@Override
	public void load(){
		
		MinecraftForgeClient.preloadTexture("/ml/boxes/gfx/box.png");
		MinecraftForgeClient.preloadTexture("/ml/boxes/gfx/boxColor.png");
		MinecraftForgeClient.preloadTexture("/ml/boxes/gfx/sprites.png");
		
		BoxTESR = new BoxTERenderer();
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBox.class, BoxTESR);
		Boxes.boxRendererID = RenderingRegistry.getNextAvailableRenderId();
		MinecraftForgeClient.registerItemRenderer(Boxes.boxBlockID, new BoxRenderer());
		TickRegistry.registerTickHandler(new ContentTipHandler(), Side.CLIENT);
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
			GL11.glPushMatrix();
			switch (type){
			case ENTITY:
				GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
				BoxTESR.setBoxFlaps(5, 2, 0, 0);
				break;
			case EQUIPPED:
				BoxTESR.setBoxFlaps(5, 2, 0, 0);
				break;
			case INVENTORY:
				GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
				BoxTESR.setBoxFlaps(0, 0, 0, 0);
				break;
			}
			BoxData box = ItemBox.getDataFromIS(item);
			BoxTESR.renderBox(ItemDye.dyeColors[item.getItemDamage()]);
			GL11.glPopMatrix();
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
