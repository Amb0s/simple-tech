package ambos.simpletech.block;

import ambos.simpletech.SimpleTech;

import net.minecraft.core.block.Block;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.stitcher.IconCoordinate;
import net.minecraft.client.render.stitcher.TextureRegistry;
import net.minecraft.client.render.block.model.BlockModelStandard;

public class BlockModelAllocator<T extends Block> extends BlockModelStandard<T> {
    public BlockModelAllocator(Block block) {
        super(block);
    }

    @Override
    public void renderBlockOnInventory(Tessellator tessellator, int metadata, float brightness, float alpha, Integer lightmapCoordinate) {
        super.renderBlockOnInventory(tessellator, 3, brightness, alpha, lightmapCoordinate);
    }

    @Override
    public IconCoordinate getBlockTextureFromSideAndMetadata(Side side, int data) {
        int direction = SimpleTech.get3DDirectionFromMeta(data);

        if (direction > 5) {
            return this.atlasIndices[Side.WEST.getId()]; // Defaults to top/bottom texture.
        } else if (side.getId() == SimpleTech.getOppositeDirectionById(direction)) {
            if (side.getId() == Side.TOP.getId() || side.getId() == Side.BOTTOM.getId()) {
                return this.atlasIndices[Side.TOP.getId()]; // Returns back top/bottom texture.
            }
            return this.atlasIndices[Side.NORTH.getId()]; // Returns back texture.
        } else if (side.getId() == direction) {
            if (side.getId() == Side.TOP.getId() || side.getId() == Side.BOTTOM.getId()) {
                return this.atlasIndices[Side.BOTTOM.getId()]; // Returns front top/bottom texture.
            }
            return this.atlasIndices[Side.SOUTH.getId()]; // Returns front texture.
        } else {
            if (side.getId() == Side.TOP.getId() || side.getId() == Side.BOTTOM.getId()) {
                return this.atlasIndices[Side.WEST.getId()]; // Returns top/bottom texture.
            } else {
                return this.atlasIndices[Side.EAST.getId()]; // Returns side texture.
            }
        }
    }
}
