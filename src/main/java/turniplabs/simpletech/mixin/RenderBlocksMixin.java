package turniplabs.simpletech.mixin;

import net.minecraft.client.render.RenderBlocks;
import net.minecraft.core.block.Block;
import net.minecraft.core.util.helper.Side;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import turniplabs.simpletech.block.BlockAllocator;
import turniplabs.simpletech.block.BlockFan;

@Mixin(value = RenderBlocks.class, remap = false)
final class RenderBlocksMixin {
    @Redirect(method = "renderBlockOnInventory(Lnet/minecraft/core/block/Block;IFF)V", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/core/block/Block;getBlockTextureFromSideAndMetadata(Lnet/minecraft/core/util/helper/Side;I)I"),
            require = 0)
    private int changeBlockInventoryRender(Block block, Side side, int meta) {
        if (block instanceof BlockFan) {
            if (side == Side.SOUTH) {
                return block.atlasIndices[Side.SOUTH.getId()]; // Returns front texture.
            }

            if (side == Side.BOTTOM) {
                return block.atlasIndices[Side.TOP.getId()]; // Returns top/bottom texture.
            }
        }

        if (block instanceof BlockAllocator) {
            if (side == Side.TOP || side == Side.BOTTOM) {
                return block.atlasIndices[Side.WEST.getId()]; // Returns top/bottom texture.
            }

            if (side == Side.SOUTH) {
                return block.atlasIndices[Side.SOUTH.getId()]; // Returns front texture.
            }

            if (side == Side.NORTH) {
                return block.atlasIndices[Side.NORTH.getId()]; // Returns back texture.
            }
        }

        return block.getBlockTextureFromSideAndMetadata(side, meta);
    }
}
