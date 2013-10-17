package ml.boxes.inventory;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import ml.boxes.data.Box;
import ml.boxes.data.ItemBoxContainer;
import ml.boxes.network.packets.PacketTipClick;
import ml.core.vec.GeoMath;
import ml.core.vec.Rectangle;
import ml.core.vec.Vector2i;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class ContentTip {

	protected final Slot boxSlot;

	protected Rectangle tipBounds = new Rectangle(0, 0, 0, 0);
	protected Rectangle gcBounds;
	protected int hvrSltIndex;

	protected Vector2i targetSize = new Vector2i(0, 0);

	protected boolean renderContents;
	public boolean interacting = false;

	protected int mousex = 0;
	protected int mousey = 0;

	private ItemStack origStack;

	public ContentTip(Slot slt, Rectangle gcRect) {
		boxSlot = slt;
		gcBounds = gcRect;
		origStack = boxSlot.getStack();
	}

	@SideOnly(Side.CLIENT)
	public void tick(Minecraft mc){

		renderContents = true;
		if (targetSize.x != tipBounds.width || targetSize.y != tipBounds.height){
			renderContents = false;
			if (targetSize.x > tipBounds.width){
				tipBounds.width += 16;
				if (targetSize.x < tipBounds.width)
					tipBounds.width = targetSize.x;
			} else if (targetSize.x < tipBounds.width) {
				tipBounds.width -= 16;
				if (targetSize.x > tipBounds.width)
					tipBounds.width = targetSize.x;
			}

			if (targetSize.y > tipBounds.height){
				tipBounds.height += 16;
				if (targetSize.y < tipBounds.height)
					tipBounds.height = targetSize.y;
			} else if (targetSize.y < tipBounds.height) {
				tipBounds.height -= 16;
				if (targetSize.y > tipBounds.height)
					tipBounds.height = targetSize.y;
			}

			tipBounds.xCoord = gcBounds.xCoord + boxSlot.xDisplayPosition + (16-tipBounds.width)/2;
			tipBounds.yCoord = gcBounds.yCoord + boxSlot.yDisplayPosition - tipBounds.height;
		}
	}

	@SideOnly(Side.CLIENT)
	public void renderTick(Minecraft mc, int mx, int my){
		hvrSltIndex = getSlotAtPosition(mx, my);
		mousex = mx;
		mousey = my;

		GL11.glPushMatrix();
		GL11.glTranslatef(tipBounds.xCoord, tipBounds.yCoord, 0F);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		renderBackground(mc, mx, my);

		if (renderContents){
			if (interacting){
				renderIteractable(mc, mx, my);
			} else {
				renderPreview(mc, mx, my);
			}
		}

		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glPopMatrix();
	}

	@SideOnly(Side.CLIENT)
	protected abstract void renderPreview(Minecraft mc, int mx, int my);

	@SideOnly(Side.CLIENT)
	protected abstract void renderIteractable(Minecraft mc, int mx, int my);

	@SideOnly(Side.CLIENT)
	protected abstract void renderBackground(Minecraft mc, int mx, int my);

	@SideOnly(Side.CLIENT)
	public boolean handleMouseClick(int mx, int my, int btn){
		Minecraft mc = FMLClientHandler.instance().getClient();
		if (isPointInTip(mx, my)){
			if (btn == 2){
				clientSlotClick(mc, hvrSltIndex, 0, 3, mc.thePlayer);
			} else {
				clientSlotClick(mc, hvrSltIndex, btn, 0, mc.thePlayer);
			}
			return true;
		}
		return false;
	}

	@SideOnly(Side.CLIENT)
	public boolean handleKeyPress(char chr, int kc){
		Minecraft mc = FMLClientHandler.instance().getClient();
		//if (isPointInTip(mousex, mousey)){
		if (mc.thePlayer.inventory.getItemStack() == null && hvrSltIndex >= 0)
		{
			for (int var2 = 0; var2 < 9; ++var2)
			{
				if (kc == 2 + var2)
				{
					clientSlotClick(mc, hvrSltIndex, var2, 2, mc.thePlayer);
					return true;
				}
			}
		}
		//return true;
		//}
		return false;
	}

	@SideOnly(Side.CLIENT)
	protected ItemStack clientSlotClick(Minecraft mc, int slotNum, int arg, int action, EntityPlayer par4EntityPlayer){
		ItemStack ret = slotClick(slotNum, arg, action, par4EntityPlayer);
		if (mc.currentScreen instanceof GuiContainerCreative){
			mc.thePlayer.inventoryContainer.detectAndSendChanges();
		} else {
			mc.thePlayer.sendQueue.addToSendQueue((new PacketTipClick(mc.thePlayer, boxSlot.slotNumber, hvrSltIndex, arg, action)).convertToPkt250());
		}
		return ret;
	}

	public boolean revalidate(int mx, int my){
		GuiScreen oc = FMLClientHandler.instance().getClient().currentScreen;
		return oc instanceof GuiContainer &&
				((GuiContainer)oc).inventorySlots.inventorySlots.contains(boxSlot) &&
				(boxSlot != null && (GeoMath.pointInRect(mx, my, gcBounds.xCoord + boxSlot.xDisplayPosition, gcBounds.yCoord + boxSlot.yDisplayPosition, 16, 16) || 
						(interacting && tipBounds.isPointInside(mx, my))) &&
						boxSlot.getHasStack() && ItemStack.areItemStackTagsEqual(origStack, boxSlot.getStack())
						) && (interacting || getIIB().getBox().canOpenContentPreview());
	}

	protected ItemBoxContainer getIIB(){
		return new ItemBoxContainer(boxSlot.getStack());
	}

	public abstract int getSlotAtPosition(int pX, int pY);

	public ItemStack getStackAtPosition(int pX, int pY){
		int sltNum = getSlotAtPosition(pX, pY);
		Box bd = getIIB().getBox();
		if (sltNum >= 0 && sltNum < bd.getSizeInventory()){
			return bd.getStackInSlot(sltNum);
		}
		return null;
	}

	public boolean isPointInTip(int pX, int pY){
		return tipBounds.isPointInside(pX, pY);
	}

	@Deprecated
	public ItemStack slotClickOld(int slotNum, int arg, int action, EntityPlayer par4EntityPlayer)
	{
		ItemBoxContainer ibox = getIIB();

		ItemStack var5 = null;
		InventoryPlayer invPl = par4EntityPlayer.inventory;
		Slot var7;
		ItemStack var8;
		int var10;
		ItemStack var11;

		if ((action == 0 || action == 1) && (arg == 0 || arg == 1))
		{
			if (action == 0)
			{
				if (slotNum < 0)
				{
					return null;
				}

				var7 = ibox.getBox().getSlots().get(slotNum);

				if (var7 != null)
				{
					var8 = var7.getStack();
					ItemStack var13 = invPl.getItemStack();

					if (var8 != null)
					{
						var5 = var8.copy();
					}

					if (var8 == null)
					{
						if (var13 != null && var7.isItemValid(var13))
						{
							var10 = arg == 0 ? var13.stackSize : 1;

							if (var10 > var7.getSlotStackLimit())
							{
								var10 = var7.getSlotStackLimit();
							}

							var7.putStack(var13.splitStack(var10));

							if (var13.stackSize == 0)
							{
								invPl.setItemStack((ItemStack)null);
							}
						}
					}
					else if (var7.canTakeStack(par4EntityPlayer))
					{
						if (var13 == null)
						{
							var10 = arg == 0 ? var8.stackSize : (var8.stackSize + 1) / 2;
							var11 = var7.decrStackSize(var10);
							invPl.setItemStack(var11);

							if (var8.stackSize == 0)
							{
								var7.putStack((ItemStack)null);
							}

							var7.onPickupFromSlot(par4EntityPlayer, invPl.getItemStack());
						}
						else if (var7.isItemValid(var13))
						{
							if (var8.itemID == var13.itemID && var8.getItemDamage() == var13.getItemDamage() && ItemStack.areItemStackTagsEqual(var8, var13))
							{
								var10 = arg == 0 ? var13.stackSize : 1;

								if (var10 > var7.getSlotStackLimit() - var8.stackSize)
								{
									var10 = var7.getSlotStackLimit() - var8.stackSize;
								}

								if (var10 > var13.getMaxStackSize() - var8.stackSize)
								{
									var10 = var13.getMaxStackSize() - var8.stackSize;
								}

								var13.splitStack(var10);

								if (var13.stackSize == 0)
								{
									invPl.setItemStack((ItemStack)null);
								}

								var8.stackSize += var10;
							}
							else if (var13.stackSize <= var7.getSlotStackLimit())
							{
								var7.putStack(var13);
								invPl.setItemStack(var8);
							}
						}
						else if (var8.itemID == var13.itemID && var13.getMaxStackSize() > 1 && (!var8.getHasSubtypes() || var8.getItemDamage() == var13.getItemDamage()) && ItemStack.areItemStackTagsEqual(var8, var13))
						{
							var10 = var8.stackSize;

							if (var10 > 0 && var10 + var13.stackSize <= var13.getMaxStackSize())
							{
								var13.stackSize += var10;
								var8 = var7.decrStackSize(var10);

								if (var8.stackSize == 0)
								{
									var7.putStack((ItemStack)null);
								}

								var7.onPickupFromSlot(par4EntityPlayer, invPl.getItemStack());
							}
						}
					}

					var7.onSlotChanged();
				}
			}
		}
		else if (action == 2 && arg >= 0 && arg < 9 && arg != boxSlot.getSlotIndex()) //Move to hotbar
		{
			var7 = ibox.getBox().getSlots().get(slotNum);

			if (var7.canTakeStack(par4EntityPlayer))
			{
				var8 = invPl.getStackInSlot(arg);
				boolean var9 = var8 == null || var7.inventory == invPl && var7.isItemValid(var8);
				var10 = -1;

				if (!var9)
				{
					var10 = invPl.getFirstEmptyStack();
					var9 |= var10 > -1;
				}

				if (var7.getHasStack() && var9)
				{
					var11 = var7.getStack();
					invPl.setInventorySlotContents(arg, var11);

					if ((var7.inventory != invPl || !var7.isItemValid(var8)) && var8 != null)
					{
						if (var10 > -1)
						{
							invPl.addItemStackToInventory(var8);
							var7.decrStackSize(var11.stackSize);
							var7.putStack((ItemStack)null);
							var7.onPickupFromSlot(par4EntityPlayer, var11);
						}
					}
					else
					{
						var7.decrStackSize(var11.stackSize);
						var7.putStack(var8);
						var7.onPickupFromSlot(par4EntityPlayer, var11);
					}
				}
				else if (!var7.getHasStack() && var8 != null && var7.isItemValid(var8))
				{
					invPl.setInventorySlotContents(arg, (ItemStack)null);
					var7.putStack(var8);
				}
			}
		}
		else if (action == 3 && par4EntityPlayer.capabilities.isCreativeMode && invPl.getItemStack() == null && slotNum >= 0)
		{
			var7 = ibox.getBox().getSlots().get(slotNum);

			if (var7 != null && var7.getHasStack())
			{
				var8 = var7.getStack().copy();
				var8.stackSize = var8.getMaxStackSize();
				invPl.setItemStack(var8);
			}
		}

		ibox.saveData();
		ibox.boxClose();
		origStack = ibox.stack;

		return var5;
	}

	/* Actions
	 * 0 - Standard action. Arg: mouseButton
	 * 1 - Slot merge into my inventory
	 * 2 - Move to Hotbar. Arg: targetSlot
	 * 3 - Creative pick stack
	 * 4 - Drop Item. Arg: 0 Drop One, 1 Drop All
	 * 5 - Stack drag over
	 * 6 - Merge into held stack. Arg: IteratorDir 0+, 1-
	 */
	private int field_94535_f = -1;
	private int field_94536_g = 0;
	private final Set field_94537_h = new HashSet();
	public ItemStack slotClick(int slotNum, int arg, int action, EntityPlayer par4EntityPlayer) { // TODO Replace with a CustomSlotClick
		ItemBoxContainer ibox = getIIB();

		ItemStack itemstack = null;
		InventoryPlayer inventoryplayer = par4EntityPlayer.inventory;
		int l;
		ItemStack itemstack1;

		if (action == 5) {
			int i1 = this.field_94536_g;
			this.field_94536_g = Container.func_94532_c(arg);

			if ((i1 != 1 || this.field_94536_g != 2) && i1 != this.field_94536_g) {
				this.func_94533_d();
			} else if (inventoryplayer.getItemStack() == null) {
				this.func_94533_d();
			} else if (this.field_94536_g == 0) {
				this.field_94535_f = Container.func_94529_b(arg);

				if (Container.func_94528_d(this.field_94535_f)) {
					this.field_94536_g = 1;
					this.field_94537_h.clear();
				} else {
					this.func_94533_d();
				}
			} else if (this.field_94536_g == 1) {
				Slot slot = ibox.getBox().getSlots().get(slotNum);

				if (slot != null && Container.func_94527_a(slot, inventoryplayer.getItemStack(), true) && slot.isItemValid(inventoryplayer.getItemStack()) && inventoryplayer.getItemStack().stackSize > this.field_94537_h.size() && this.func_94531_b(slot)) {
					this.field_94537_h.add(slot);
				}
			} else if (this.field_94536_g == 2) {
				if (!this.field_94537_h.isEmpty()) {
					itemstack1 = inventoryplayer.getItemStack().copy();
					l = inventoryplayer.getItemStack().stackSize;
					Iterator iterator = this.field_94537_h.iterator();

					while (iterator.hasNext()) {
						Slot slot1 = (Slot)iterator.next();

						if (slot1 != null && Container.func_94527_a(slot1, inventoryplayer.getItemStack(), true) && slot1.isItemValid(inventoryplayer.getItemStack()) && inventoryplayer.getItemStack().stackSize >= this.field_94537_h.size() && this.func_94531_b(slot1)) {
							ItemStack itemstack2 = itemstack1.copy();
							int j1 = slot1.getHasStack() ? slot1.getStack().stackSize : 0;
							Container.func_94525_a(this.field_94537_h, this.field_94535_f, itemstack2, j1);

							if (itemstack2.stackSize > itemstack2.getMaxStackSize()) {
								itemstack2.stackSize = itemstack2.getMaxStackSize();
							}

							if (itemstack2.stackSize > slot1.getSlotStackLimit()) {
								itemstack2.stackSize = slot1.getSlotStackLimit();
							}

							l -= itemstack2.stackSize - j1;
							slot1.putStack(itemstack2);
						}
					}

					itemstack1.stackSize = l;

					if (itemstack1.stackSize <= 0) {
						itemstack1 = null;
					}

					inventoryplayer.setItemStack(itemstack1);
				}

				this.func_94533_d();
			} else {
				this.func_94533_d();
			}
		} else if (this.field_94536_g != 0) {
			this.func_94533_d();
		} else {
			Slot slot2;
			int k1;
			ItemStack itemstack3;

			if ((action == 0 || action == 1) && (arg == 0 || arg == 1)) {
				if (slotNum == -999) {
					if (inventoryplayer.getItemStack() != null && slotNum == -999) {
						if (arg == 0) {
							par4EntityPlayer.dropPlayerItem(inventoryplayer.getItemStack());
							inventoryplayer.setItemStack((ItemStack)null);
						}

						if (arg == 1) {
							par4EntityPlayer.dropPlayerItem(inventoryplayer.getItemStack().splitStack(1));

							if (inventoryplayer.getItemStack().stackSize == 0) {
								inventoryplayer.setItemStack((ItemStack)null);
							}
						}
					}
				} else if (action == 1) {
//					if (slotNum < 0)
//					{
//						return null;
//					}
//
//					slot2 = ibox.getBoxData().getSlots().get(slotNum);
//
//					if (slot2 != null && slot2.canTakeStack(par4EntityPlayer))
//					{
//						itemstack1 = this.transferStackInSlot(par4EntityPlayer, slotNum);
//
//						if (itemstack1 != null)
//						{
//							l = itemstack1.itemID;
//							itemstack = itemstack1.copy();
//
//							if (slot2 != null && slot2.getStack() != null && slot2.getStack().itemID == l)
//							{
//								this.retrySlotClick(slotNum, arg, true, par4EntityPlayer);
//							}
//						}
//					}
				} else {
					if (slotNum < 0) {
						return null;
					}

					slot2 = ibox.getBox().getSlots().get(slotNum);

					if (slot2 != null) {
						itemstack1 = slot2.getStack();
						ItemStack itemstack4 = inventoryplayer.getItemStack();

						if (itemstack1 != null) {
							itemstack = itemstack1.copy();
						}

						if (itemstack1 == null) {
							if (itemstack4 != null && slot2.isItemValid(itemstack4)) {
								k1 = arg == 0 ? itemstack4.stackSize : 1;

								if (k1 > slot2.getSlotStackLimit()) {
									k1 = slot2.getSlotStackLimit();
								}

								slot2.putStack(itemstack4.splitStack(k1));

								if (itemstack4.stackSize == 0) {
									inventoryplayer.setItemStack((ItemStack)null);
								}
							}
						} else if (slot2.canTakeStack(par4EntityPlayer)) {
							if (itemstack4 == null) {
								k1 = arg == 0 ? itemstack1.stackSize : (itemstack1.stackSize + 1) / 2;
								itemstack3 = slot2.decrStackSize(k1);
								inventoryplayer.setItemStack(itemstack3);

								if (itemstack1.stackSize == 0) {
									slot2.putStack((ItemStack)null);
								}

								slot2.onPickupFromSlot(par4EntityPlayer, inventoryplayer.getItemStack());
							} else if (slot2.isItemValid(itemstack4)) {
								if (itemstack1.itemID == itemstack4.itemID && itemstack1.getItemDamage() == itemstack4.getItemDamage() && ItemStack.areItemStackTagsEqual(itemstack1, itemstack4)) {
									k1 = arg == 0 ? itemstack4.stackSize : 1;

									if (k1 > slot2.getSlotStackLimit() - itemstack1.stackSize) {
										k1 = slot2.getSlotStackLimit() - itemstack1.stackSize;
									}

									if (k1 > itemstack4.getMaxStackSize() - itemstack1.stackSize) {
										k1 = itemstack4.getMaxStackSize() - itemstack1.stackSize;
									}

									itemstack4.splitStack(k1);

									if (itemstack4.stackSize == 0) {
										inventoryplayer.setItemStack((ItemStack)null);
									}

									itemstack1.stackSize += k1;
								} else if (itemstack4.stackSize <= slot2.getSlotStackLimit()) {
									slot2.putStack(itemstack4);
									inventoryplayer.setItemStack(itemstack1);
								}
							} else if (itemstack1.itemID == itemstack4.itemID && itemstack4.getMaxStackSize() > 1 && (!itemstack1.getHasSubtypes() || itemstack1.getItemDamage() == itemstack4.getItemDamage()) && ItemStack.areItemStackTagsEqual(itemstack1, itemstack4)) {
								k1 = itemstack1.stackSize;

								if (k1 > 0 && k1 + itemstack4.stackSize <= itemstack4.getMaxStackSize()) {
									itemstack4.stackSize += k1;
									itemstack1 = slot2.decrStackSize(k1);

									if (itemstack1.stackSize == 0) {
										slot2.putStack((ItemStack)null);
									}

									slot2.onPickupFromSlot(par4EntityPlayer, inventoryplayer.getItemStack());
								}
							}
						}

						slot2.onSlotChanged();
					}
				}
			} else if (action == 2 && arg >= 0 && arg < 9) {
				slot2 = ibox.getBox().getSlots().get(slotNum);

				if (slot2.canTakeStack(par4EntityPlayer)) {
					itemstack1 = inventoryplayer.getStackInSlot(arg);
					boolean flag = itemstack1 == null || slot2.inventory == inventoryplayer && slot2.isItemValid(itemstack1);
					k1 = -1;

					if (!flag) {
						k1 = inventoryplayer.getFirstEmptyStack();
						flag |= k1 > -1;
					}

					if (slot2.getHasStack() && flag) {
						itemstack3 = slot2.getStack();
						inventoryplayer.setInventorySlotContents(arg, itemstack3);

						if ((slot2.inventory != inventoryplayer || !slot2.isItemValid(itemstack1)) && itemstack1 != null) {
							if (k1 > -1) {
								inventoryplayer.addItemStackToInventory(itemstack1);
								slot2.decrStackSize(itemstack3.stackSize);
								slot2.putStack((ItemStack)null);
								slot2.onPickupFromSlot(par4EntityPlayer, itemstack3);
							}
						} else {
							slot2.decrStackSize(itemstack3.stackSize);
							slot2.putStack(itemstack1);
							slot2.onPickupFromSlot(par4EntityPlayer, itemstack3);
						}
					} else if (!slot2.getHasStack() && itemstack1 != null && slot2.isItemValid(itemstack1)) {
						inventoryplayer.setInventorySlotContents(arg, (ItemStack)null);
						slot2.putStack(itemstack1);
					}
				}
			} else if (action == 3 && par4EntityPlayer.capabilities.isCreativeMode && inventoryplayer.getItemStack() == null && slotNum >= 0) {
				slot2 = ibox.getBox().getSlots().get(slotNum);

				if (slot2 != null && slot2.getHasStack()) {
					itemstack1 = slot2.getStack().copy();
					itemstack1.stackSize = itemstack1.getMaxStackSize();
					inventoryplayer.setItemStack(itemstack1);
				}
			} else if (action == 4 && inventoryplayer.getItemStack() == null && slotNum >= 0) {
				slot2 = ibox.getBox().getSlots().get(slotNum);

				if (slot2 != null && slot2.getHasStack()) {
					itemstack1 = slot2.decrStackSize(arg == 0 ? 1 : slot2.getStack().stackSize);
					slot2.onPickupFromSlot(par4EntityPlayer, itemstack1);
					par4EntityPlayer.dropPlayerItem(itemstack1);
				}
			} else if (action == 6 && slotNum >= 0) {
				slot2 = ibox.getBox().getSlots().get(slotNum);
				itemstack1 = inventoryplayer.getItemStack();

				if (itemstack1 != null && (slot2 == null || !slot2.getHasStack() || !slot2.canTakeStack(par4EntityPlayer))) {
					l = arg == 0 ? 0 : ibox.getBox().getSlots().size() - 1;
					k1 = arg == 0 ? 1 : -1;

					for (int l1 = 0; l1 < 2; ++l1) {
						for (int i2 = l; i2 >= 0 && i2 < ibox.getBox().getSlots().size() && itemstack1.stackSize < itemstack1.getMaxStackSize(); i2 += k1) {
							Slot slot3 = ibox.getBox().getSlots().get(i2);

							if (slot3.getHasStack() && Container.func_94527_a(slot3, itemstack1, true) && slot3.canTakeStack(par4EntityPlayer) && this.func_94530_a(itemstack1, slot3) && (l1 != 0 || slot3.getStack().stackSize != slot3.getStack().getMaxStackSize())) {
								int j2 = Math.min(itemstack1.getMaxStackSize() - itemstack1.stackSize, slot3.getStack().stackSize);
								ItemStack itemstack5 = slot3.decrStackSize(j2);
								itemstack1.stackSize += j2;

								if (itemstack5.stackSize <= 0) {
									slot3.putStack((ItemStack)null);
								}

								slot3.onPickupFromSlot(par4EntityPlayer, itemstack5);
							}
						}
					}
				}
				//this.detectAndSendChanges();
			}
		}

		ibox.saveData();
		ibox.boxClose();
		origStack = ibox.stack;

		return itemstack;
	}

	protected void func_94533_d()
	{
		this.field_94536_g = 0;
		this.field_94537_h.clear();
	}

	public boolean func_94531_b(Slot par1Slot)
	{
		return true;
	}

	public boolean func_94530_a(ItemStack par1ItemStack, Slot par2Slot)
	{
		return true;
	}
}
