package turniplabs.simpletech.block;

import net.minecraft.core.block.BlockChest;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.sound.SoundType;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;
import turniplabs.simpletech.SimpleTech;

import java.util.Random;

public class BlockTrappedChest extends BlockChest {
    public BlockTrappedChest(String key, int id, Material material) {
        super(key, id, material);
        this.withTexCoords(9, 1, 9, 1, 11, 1, 10, 1, 10,
                1, 10, 1);
        this.setTickOnLoad(true);
    }

    @Override
    public int tickRate() {
        return 20;
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
    public boolean renderAsNormalBlock() {
        return false;
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
    public void onBlockRemoval(World world, int x, int y, int z) {
        if (SimpleTech.getRedstoneFromMetadata(world.getBlockMetadata(x, y, z)) > 0) {
            this.notifyNeighbors(world, x, y, z);
        }

        super.onBlockRemoval(world, x, y, z);
    }

    @Override
    public boolean blockActivated(World world, int x, int y, int z, EntityPlayer player) {
        this.setState(world, x, y, z, (byte) 1);

        world.scheduleBlockUpdate(x, y, z, this.id, this.tickRate());
        world.playSoundEffect(SoundType.GUI_SOUNDS,x + 0.5, y + 0.5, z + 0.5,
                "random.click", 0.3f, 0.6f);

        return super.blockActivated(world, x, y, z, player);
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random rand) {
        if (!world.isClientSide) {
            if (SimpleTech.getRedstoneFromMetadata(world.getBlockMetadata(x, y, z)) > 0) {
                this.setState(world, x, y, z, (byte) 0);
            }
        }
    }

    private void setState(World world, int x, int y, int z, byte redstone) {
        int direction = SimpleTech.getDirectionFromMetadata(world.getBlockMetadata(x, y, z));

        // Recreates metadata using the redstone signal and the block direction values.
        world.setBlockMetadataWithNotify(x, y, z, SimpleTech.createMetadata(direction, redstone));

        // Updates block's neighbors.
        this.notifyNeighbors(world, x, y, z);

        world.markBlocksDirty(x, y, z, x, y, z);
    }

    private void notifyNeighbors(World world, int x, int y, int z) {
        world.notifyBlocksOfNeighborChange(x, y, z, this.id);
        world.notifyBlocksOfNeighborChange(x, y - 1, z, this.id);
    }
}
