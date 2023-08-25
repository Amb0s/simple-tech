package turniplabs.simpletech.block;

import net.minecraft.core.block.BlockChest;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.sound.SoundType;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;

import java.util.Random;

public class BlockTrappedChest extends BlockChest {
    public static final int redstoneOffset = 4;

    public BlockTrappedChest(String key, int id, Material material) {
        super(key, id, material);
        this.withTexCoords(9, 1, 9, 1, 11, 1, 10, 1, 10, 1, 10, 1);
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
        return BlockTrappedChest.getRedstoneFromMetadata(blockAccess.getBlockMetadata(x, y, z)) > 0;
    }

    @Override
    public void onBlockRemoval(World world, int x, int y, int z) {
        if (BlockTrappedChest.getRedstoneFromMetadata(world.getBlockMetadata(x, y, z)) > 0) {
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
            if (BlockTrappedChest.getRedstoneFromMetadata(world.getBlockMetadata(x, y, z)) > 0) {
                this.setState(world, x, y, z, (byte) 0);
            }
        }
    }

    private void setState(World world, int x, int y, int z, byte redstone) {
        int metadata = world.getBlockMetadata(x, y, z);

        // Recreates metadata using the redstone signal and the old metadata value.
        world.setBlockMetadataWithNotify(x, y, z, BlockTrappedChest.getMetaWithRedstone(metadata, redstone));

        // Updates block's neighbors.
        this.notifyNeighbors(world, x, y, z);

        world.markBlocksDirty(x, y, z, x, y, z);
    }

    private void notifyNeighbors(World world, int x, int y, int z) {
        world.notifyBlocksOfNeighborChange(x, y, z, this.id);
        world.notifyBlocksOfNeighborChange(x, y - 1, z, this.id);
    }

    public static int getRedstoneFromMetadata(int metadata) {
        return metadata >> 4;
    }

    public static int getMetaWithRedstone(int metadata, int redstone) {
        // https://www.geeksforgeeks.org/modify-bit-given-position/
        int mask = 1 << redstoneOffset;
        return (metadata & ~mask) | ((redstone << redstoneOffset) & mask);
    }
}
