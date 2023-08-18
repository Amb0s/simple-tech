package turniplabs.simpletech.player.inventory;

import net.minecraft.core.InventoryAction;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.player.inventory.Container;
import net.minecraft.core.player.inventory.IInventory;
import net.minecraft.core.player.inventory.slot.Slot;
import turniplabs.simpletech.block.entity.TileEntityAllocator;

import java.util.List;

public class ContainerAllocator extends Container {
	private final TileEntityAllocator allocator;

	public ContainerAllocator(IInventory playerInventory, TileEntityAllocator allocator) {
		this.allocator = allocator;

		// Adding allocator slot.
		this.addSlot(new Slot(allocator, 0, 80, 36));

		// Adding player inventory slots.
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for(int i = 0; i < 9; ++i) {
			this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
		}
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer entityPlayer) {
		return this.allocator.canInteractWith(entityPlayer);
	}

	@Override
	public List<Integer> getMoveSlots(InventoryAction inventoryAction, Slot slot, int i, EntityPlayer entityPlayer) {
		return null;
	}

	@Override
	public List<Integer> getTargetSlots(InventoryAction inventoryAction, Slot slot, int i, EntityPlayer entityPlayer) {
		return null;
	}
}
