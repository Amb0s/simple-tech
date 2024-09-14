package ambos.simpletech.block;

import net.minecraft.core.block.Block;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.WorldSource;
import net.minecraft.client.render.LightmapHelper;
import net.minecraft.core.block.BlockRedstoneRepeater;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.stitcher.IconCoordinate;
import net.minecraft.client.render.stitcher.TextureRegistry;
import net.minecraft.client.render.block.model.BlockModelTorch;
import net.minecraft.client.render.block.model.BlockModelStandard;
import net.minecraft.client.render.block.model.BlockModelDispatcher;

public class BlockModelRedstoneNotGate<T extends Block> extends BlockModelStandard<T> {
    private static final BlockModelTorch<Block> modelTorchActive = (BlockModelTorch<Block>) BlockModelDispatcher.getInstance().getDispatch(Block.torchRedstoneActive);
    private static final BlockModelTorch<Block> modelTorchIdle = (BlockModelTorch<Block>) BlockModelDispatcher.getInstance().getDispatch(Block.torchRedstoneIdle);

    private static final IconCoordinate torch1     = TextureRegistry.getTexture("minecraft:block/torch_redstone_idle"),
                                        torch2     = TextureRegistry.getTexture("minecraft:block/torch_redstone_active");
    private static final IconCoordinate repeater1  = TextureRegistry.getTexture("minecraft:block/repeater_idle_top"),
                                        repeater2  = TextureRegistry.getTexture("minecraft:block/repeater_active_top");
    private static final IconCoordinate stone      = TextureRegistry.getTexture("minecraft:block/polished_stone_top");

    public boolean isPowered;
    public BlockModelRedstoneNotGate(Block block, boolean isPowered) {
        super(block);
        isPowered = isPowered;
    }

    @Override
    public boolean shouldItemRender3d() {
        return false;
    }

    @Override
    public boolean shouldSideBeRendered(WorldSource blockAccess, int x, int y, int z, int side) {
        return true;
    }

    @Override
    public boolean render(Tessellator tessellator, int x, int y, int z) {
        this.block.setBlockBoundsBasedOnState(renderBlocks.blockAccess, x, y, z);
        int meta = renderBlocks.blockAccess.getBlockMetadata(x, y, z);
        int i1 = meta & 0x3;
        switch (i1) {
            case 0:
                renderBlocks.uvRotateTop = 0;
                break;
            case 2:
                renderBlocks.uvRotateTop = 3;
                break;
            case 3:
                renderBlocks.uvRotateTop = 2;
                break;
            case 1:
                renderBlocks.uvRotateTop = 1;
                break;
        }
        int j1 = (meta & 0xC) >> 2;
        renderStandardBlock(tessellator, this.block, x, y, z);
        resetRenderBlocks();
        float brightness = 1.0F;
        if (LightmapHelper.isLightmapEnabled()) {
            int lmc = this.block.getLightmapCoord(renderBlocks.blockAccess, x, y, z);
            if (Block.lightEmission[this.block.id] > 0)
                lmc = LightmapHelper.setBlocklightValue(lmc, 15);
                tessellator.setLightmapCoord(lmc);
        } else {
            brightness = getBlockBrightness(renderBlocks.blockAccess, x, y, z);
            if (Block.lightEmission[this.block.id] > 0)
                brightness = 1.0F;
        }
        tessellator.setColorOpaque_F(brightness, brightness, brightness);
        double d = -0.1875D;
        double d1 = 0.0D;
        double d2 = 0.0D;
        switch (i1) {
            case 0:
                d2 = BlockRedstoneRepeater.field_22024_a[j1];
                break;
            case 2:
                d2 = -BlockRedstoneRepeater.field_22024_a[j1];
                break;
            case 3:
                d1 = BlockRedstoneRepeater.field_22024_a[j1];
                break;
            case 1:
                d1 = -BlockRedstoneRepeater.field_22024_a[j1];
                break;
        }
        BlockModelTorch<Block> modelTorch = isPowered ? modelTorchActive : modelTorchIdle;
        modelTorch.renderTorchAtAngle(tessellator, x + d1, y + d, z + d2, 0.0D, 0.0D);
        return true;
    }

    @Override
    public IconCoordinate getBlockTextureFromSideAndMetadata(Side side, int data) {
        if (side == Side.BOTTOM) {
            return !this.isPowered ? torch1 : torch2;
        } else if (side == Side.TOP) {
            return !this.isPowered ? repeater1 : repeater2;
        } else {
            return stone;
        }
    }
}
