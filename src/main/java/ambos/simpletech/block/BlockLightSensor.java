package ambos.simpletech.block;

import ambos.simpletech.SimpleTech;
import ambos.simpletech.block.entity.TileEntityLightSensor;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.block.BlockTileEntity;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;

public class BlockLightSensor extends BlockTileEntity {
    public static final int invertedOffset = 0;
    public static final int redstoneOffset = 4;

    public BlockLightSensor(String key, int id, Material material) {
        super(key, id, material);
    }

    public boolean isInverted(World world, int x, int y, int z) {
        return SimpleTech.getInvertedFromMetadata(world.getBlockMetadata(x, y, z), invertedOffset) != 0;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean isSolidRender() {
        return false;
    }

    @Override
    public boolean canProvidePower() {
        return true;
    }

    @Override
    public TileEntity getNewBlockEntity() {
        return new TileEntityLightSensor();
    }

    @Override
    public boolean isPoweringTo(WorldSource blockAccess, int x, int y, int z, int side) {
        return SimpleTech.getRedstoneFromMetadata(blockAccess.getBlockMetadata(x, y, z), redstoneOffset) > 0;
    }

    @Override
    public void setBlockBoundsBasedOnState(WorldSource world, int x, int y, int z) {
        // Sets block shape when placed.
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.25f, 1.0f);
    }

    @Override
    public void setBlockBoundsForItemRender() {
        // Sets block shape when rendered inside containers.
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.25f, 1.0f);
    }

    @Override
    public boolean onBlockRightClicked(World world, int x, int y, int z, EntityPlayer player, Side side, double xPlaced, double yPlaced) {
        int metadata = world.getBlockMetadata(x, y, z);
        int isInverted = !isInverted(world, x, y, z) ? 1 : 0;

        // Recreates metadata using the inverted state and the old metadata value.
        world.setBlockMetadataWithNotify(x, y, z, SimpleTech.getMetaWithInverted(metadata, isInverted, invertedOffset));

        return true;
    }

    public void updateSensor(World world, int x, int y, int z, boolean powering) {
        int metadata = world.getBlockMetadata(x, y, z);
        int redstone = powering ? 1 : 0;

        // Recreates metadata using the redstone signal and the old metadata value.
        world.setBlockMetadataWithNotify(x, y, z, SimpleTech.getMetaWithRedstone(metadata, redstone, redstoneOffset));

        // Updates block's neighbors.
        world.notifyBlocksOfNeighborChange(x, y, z, this.id);
        world.notifyBlocksOfNeighborChange(x, y - 1, z, this.id);

        world.markBlocksDirty(x, y, z, x, y, z);
    }
}
