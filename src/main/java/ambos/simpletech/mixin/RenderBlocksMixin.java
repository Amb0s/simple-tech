package ambos.simpletech.mixin;

import net.minecraft.client.render.RenderBlocks;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.TextureFX;
import net.minecraft.core.Global;
import net.minecraft.core.block.Block;
import net.minecraft.core.util.helper.Side;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import ambos.simpletech.block.BlockAllocator;
import ambos.simpletech.block.BlockFan;
import ambos.simpletech.block.BlockRedstoneNotGate;

@Mixin(value = RenderBlocks.class, remap = false)
final class RenderBlocksMixin {
    @Redirect(method = "renderBlockOnInventory(Lnet/minecraft/core/block/Block;IFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/block/Block;getBlockTextureFromSideAndMetadata(Lnet/minecraft/core/util/helper/Side;I)I"), require = 0)
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

    @ModifyVariable(method = "renderBlockByRenderType", at = @At("HEAD"), ordinal = 0, require = 0)
    private int changeRenderType(int renderType, Block block) {
        if (block instanceof BlockRedstoneNotGate) {
            return 15; // Use the repeater renderer as a base.
        }

        return renderType;
    }

    @Inject(method = "renderBlockRepeater", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Tessellator;setColorOpaque_F(FFF)V", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true, require = 0)
    private void renderBlockRedstoneGate(Block block, int i, int j, int k, CallbackInfoReturnable<Boolean> cir,
            int l, int i1, int j1, Tessellator tessellator) {
        if (block instanceof BlockRedstoneNotGate) {
            /* Gets metadata, tesselator and block brightness (captures locals). */

            /*
             * Skips torch rendering instructions (injects just after 'setColorOpaque_F'
             * method call).
             */

            int k1 = block.getBlockTextureFromSideAndMetadata(Side.TOP, l);
            int l1 = k1 % Global.TEXTURE_ATLAS_WIDTH_TILES * TextureFX.tileWidthTerrain;
            int i2 = k1 / Global.TEXTURE_ATLAS_WIDTH_TILES * TextureFX.tileWidthTerrain;
            double d5 = (double) ((float) l1 / (float) (TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES));
            double d6 = (double) (((float) l1 + ((float) TextureFX.tileWidthTerrain - 0.01F))
                    / (float) (TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES));
            double d7 = (double) ((float) i2 / (float) (TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES));
            double d8 = (double) (((float) i2 + ((float) TextureFX.tileWidthTerrain - 0.01F))
                    / (float) (TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES));
            float f1 = 0.125F;
            float f2 = (float) (i + 1);
            float f3 = (float) (i + 1);
            float f4 = (float) (i + 0);
            float f5 = (float) (i + 0);
            float f6 = (float) (k + 0);
            float f7 = (float) (k + 1);
            float f8 = (float) (k + 1);
            float f9 = (float) (k + 0);
            float f10 = (float) j + f1;
            if (i1 == 2) {
                f2 = f3 = (float) (i + 0);
                f4 = f5 = (float) (i + 1);
                f6 = f9 = (float) (k + 1);
                f7 = f8 = (float) (k + 0);
            } else if (i1 == 3) {
                f2 = f5 = (float) (i + 0);
                f3 = f4 = (float) (i + 1);
                f6 = f7 = (float) (k + 0);
                f8 = f9 = (float) (k + 1);
            } else if (i1 == 1) {
                f2 = f5 = (float) (i + 1);
                f3 = f4 = (float) (i + 0);
                f6 = f7 = (float) (k + 1);
                f8 = f9 = (float) (k + 0);
            }

            tessellator.addVertexWithUV((double) f5, (double) f10, (double) f9, d5, d7);
            tessellator.addVertexWithUV((double) f4, (double) f10, (double) f8, d5, d8);
            tessellator.addVertexWithUV((double) f3, (double) f10, (double) f7, d6, d8);
            tessellator.addVertexWithUV((double) f2, (double) f10, (double) f6, d6, d7);

            cir.setReturnValue(true);
        }
    }
}
