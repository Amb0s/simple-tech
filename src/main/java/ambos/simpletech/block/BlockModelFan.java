package ambos.simpletech.block;

import ambos.simpletech.SimpleTech;

import net.minecraft.core.block.Block;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.stitcher.IconCoordinate;
import net.minecraft.client.render.stitcher.TextureRegistry;
import net.minecraft.client.render.block.model.BlockModelStandard;

public class BlockModelFan<T extends Block> extends BlockModelStandard<T> {
    public BlockModelFan(Block block) {
        super(block);
    }

    @Override
    public void renderBlockOnInventory(Tessellator tessellator, int metadata, float brightness, float alpha, Integer lightmapCoordinate) {
        super.renderBlockOnInventory(tessellator, 3, brightness, alpha, lightmapCoordinate);
    }

    @Override
    public IconCoordinate getBlockTextureFromSideAndMetadata(Side side, int data) {
        int direction = SimpleTech.get3DDirectionFromMeta(data);
        if (direction > Direction.EAST.getId()) {
            return this.atlasIndices[Side.TOP.getId()]; // Defaults to top/bottom texture.
        } else if (side.getId() == direction) {
            return this.atlasIndices[Side.SOUTH.getId()]; // Returns front texture.
        } else {
            if (side.getId() == Side.TOP.getId() || side.getId() == Side.BOTTOM.getId()) {
                return this.atlasIndices[Side.TOP.getId()]; // Returns top/bottom texture.
            } else {
                return this.atlasIndices[Side.EAST.getId()]; // Returns one of the sides texture.
            }
        }
    }
}
