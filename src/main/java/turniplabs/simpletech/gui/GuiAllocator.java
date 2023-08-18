package turniplabs.simpletech.gui;

import net.minecraft.client.gui.GuiContainer;
import net.minecraft.core.player.inventory.InventoryPlayer;
import org.lwjgl.opengl.GL11;
import turniplabs.simpletech.block.entity.TileEntityAllocator;
import turniplabs.simpletech.player.inventory.ContainerAllocator;

public class GuiAllocator extends GuiContainer {
	private TileEntityAllocator allocatorInv;

	public GuiAllocator(InventoryPlayer playerInventory, TileEntityAllocator allocator) {
		super(new ContainerAllocator(playerInventory, allocator));
		this.allocatorInv = allocator;
	}

	@Override
	protected void drawGuiContainerForegroundLayer() {
		this.fontRenderer.drawString("Allocator", 60, 6, 4210752);
		this.fontRenderer.drawString("Inventory", 8, this.ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f) {
		int i = this.mc.renderEngine.getTexture("/assets/simpletech/gui/allocator.png");

		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);

		this.mc.renderEngine.bindTexture(i);

		int j = (this.width - this.xSize) / 2;
		int k = (this.height - this.ySize) / 2;

		this.drawTexturedModalRect(j, k, 0, 0, this.xSize, this.ySize);
	}
}
