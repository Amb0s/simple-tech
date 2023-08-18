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
    @Redirect(method = "renderBlockOnInventory", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/core/block/Block;getBlockTextureFromSideAndMetadata(Lnet/minecraft/core/util/helper/Side;I)I"),
            require = 0)
    private int changeBlockInventoryRender(Block block, Side side, int meta) {
        if (block instanceof BlockFan) {
            if (side == Side.SOUTH) {
                return block.getBlockTextureFromSideAndMetadata(Side.BOTTOM, meta);
            }

            if (side == Side.BOTTOM) {
                return block.getBlockTextureFromSideAndMetadata(Side.TOP, meta);
            }
        }

        if (block instanceof BlockAllocator) {
            BlockAllocator allocator = ((BlockAllocator) block);
            if (side == Side.TOP) {
                return Block.texCoordToIndex(allocator.getTop()[0], allocator.getTop()[1]);
            }

            if (side == Side.BOTTOM) {
                return Block.texCoordToIndex(allocator.getTop()[0], allocator.getTop()[1]);
            }

            if (side == Side.SOUTH) {
                return Block.texCoordToIndex(allocator.getFront()[0], allocator.getFront()[1]);
            }

            if (side == Side.NORTH) {
                return Block.texCoordToIndex(allocator.getBack()[0], allocator.getBack()[1]);
            }
        }

        return block.getBlockTextureFromSideAndMetadata(side, meta);
    }
}
