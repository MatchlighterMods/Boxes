package ml.boxes.inventory;

import ml.boxes.data.BoxData;
import ml.boxes.data.BoxData.BoxSlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContentTip {

    /* Actions
     * 0 - Standard action. Arg: mouseButton
     * 1 - Slot merge into my inventory
     * 2 - Move to Hotbar. Arg: targetSlot
     * 3 - Creative pick stack
     */
    public ItemStack slotClick(BoxData bd, int slotNum, int arg, int action, EntityPlayer par4EntityPlayer)
    {
        ItemStack var5 = null;
        InventoryPlayer invPl = par4EntityPlayer.inventory;
        BoxSlot var7;
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

                var7 = (Slot)this.inventorySlots.get(slotNum);

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
        else if (action == 2 && arg >= 0 && arg < 9) //Move to hotbar
        {
            var7 = (Slot)this.inventorySlots.get(slotNum);

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
            var7 = (Slot)this.inventorySlots.get(slotNum);

            if (var7 != null && var7.getHasStack())
            {
                var8 = var7.getStack().copy();
                var8.stackSize = var8.getMaxStackSize();
                invPl.setItemStack(var8);
            }
        }

        return var5;
    }
    
}
