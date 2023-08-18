package turniplabs.simpletech.block;

import net.minecraft.core.block.BlockTileEntity;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;
import turniplabs.simpletech.SimpleTech;
import turniplabs.simpletech.block.entity.TileEntityLightSensor;

public class BlockLightSensor extends BlockTileEntity {
    private boolean inverted;
    public BlockLightSensor(String key, int id, Material material) {
        super(key, id, material);
        this.inverted = false;
    }

    public boolean isInverted() {
        return inverted;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
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
        // Debug code to display metadata.
        boolean debug = false;
        if (debug) {
            System.out.println("METADATA: " + Integer.toString(
                    blockAccess.getBlockMetadata(x, y, z), 2));
            System.out.println("DIRECTION: " + Integer.toString(
                    SimpleTech.getDirectionFromMetadata(blockAccess.getBlockMetadata(x, y, z)), 2));
            System.out.println("REDSTONE: " + Integer.toString(SimpleTech.getRedstoneFromMetadata(
                    blockAccess.getBlockMetadata(x, y, z)), 2));
            System.out.println("METADATA (recombined): " + Integer.toString(SimpleTech.createMetadata(
                    SimpleTech.getDirectionFromMetadata(blockAccess.getBlockMetadata(x, y, z)),
                    SimpleTech.getRedstoneFromMetadata(blockAccess.getBlockMetadata(x, y, z))), 2));
        }

        return SimpleTech.getRedstoneFromMetadata(blockAccess.getBlockMetadata(x, y, z)) > 0;
    }

    @Override
    public void setBlockBoundsBasedOnState(World world, int x, int y, int z) {
        // Sets block shape when placed.
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.25f, 1.0f);
    }

    @Override
    public void setBlockBoundsForItemRender() {
        // Sets block shape when rendered inside containers.
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.25f, 1.0f);
    }

    @Override
    public boolean blockActivated(World world, int x, int y, int z, EntityPlayer player) {
        this.inverted = !this.inverted;
        return true;
    }

    public void updateSensor(World world, int x, int y, int z, byte redstone) {
        int direction = SimpleTech.getDirectionFromMetadata(world.getBlockMetadata(x, y, z));

        // Recreates metadata using the redstone signal and the block direction values.
        world.setBlockMetadataWithNotify(x, y, z, SimpleTech.createMetadata(direction, redstone));

        // Updates block's neighbors.
        world.notifyBlocksOfNeighborChange(x, y, z, this.id);
        world.notifyBlocksOfNeighborChange(x, y - 1, z, this.id);

        world.markBlocksDirty(x, y, z, x, y, z);
    }
}
