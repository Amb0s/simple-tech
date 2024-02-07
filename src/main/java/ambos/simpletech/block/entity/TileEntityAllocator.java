package ambos.simpletech.block.entity;

import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.ListTag;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.IInventory;

public class TileEntityAllocator extends TileEntity implements IInventory {
    private ItemStack allocatorFilterItem;

    @Override
    public int getSizeInventory() {
        return 1;
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        return i == 0 ? this.allocatorFilterItem : null;
    }

    @Override
    public ItemStack decrStackSize(int i, int j) {
        if (i != 0) {
            return null;
        } else if (this.allocatorFilterItem != null) {
            ItemStack itemstack;
            if (this.allocatorFilterItem.stackSize <= j) {
                itemstack = this.allocatorFilterItem;
                this.allocatorFilterItem = null;
                return itemstack;
            } else {
                itemstack = this.allocatorFilterItem.splitStack(j);
                if (this.allocatorFilterItem.stackSize == 0) {
                    this.allocatorFilterItem = null;
                }

                return itemstack;
            }
        } else {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemStack) {
        if (i == 0) {
            this.allocatorFilterItem = itemStack;
            if (itemStack != null && itemStack.stackSize > this.getInventoryStackLimit()) {
                itemStack.stackSize = this.getInventoryStackLimit();
            }
        }
    }

    @Override
    public String getInvName() {
        return "Allocator";
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityPlayer) {
        return this.worldObj.getBlockTileEntity(this.x, this.y, this.z) == this &&
                entityPlayer.distanceToSqr((double) this.x + 0.5D, (double) this.y + 0.5D,
                        (double) this.z + 0.5D) <= 64.0D;
    }

    @Override
    public void sortInventory() {
    }

    @Override
    public void readFromNBT(CompoundTag nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        ListTag items = nbttagcompound.getList("Items");
        if (items.tagCount() != 0) {
            CompoundTag item = (CompoundTag) items.tagAt(0);
            int slot = item.getByte("Slot") & 255;
            if (slot == 0) {
                this.allocatorFilterItem = ItemStack.readItemStackFromNbt(item);
            }
        }
    }

    @Override
    public void writeToNBT(CompoundTag nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        ListTag items = new ListTag();
        if (this.allocatorFilterItem != null) {
            CompoundTag item = new CompoundTag();
            item.putByte("Slot", (byte) 0);
            this.allocatorFilterItem.writeToNBT(item);
            items.addTag(item);
        }

        nbttagcompound.put("Items", items);
    }
}
