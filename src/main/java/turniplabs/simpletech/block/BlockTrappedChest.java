package turniplabs.simpletech.block;

import net.minecraft.core.block.BlockChest;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.sound.SoundType;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;

import java.util.Random;

public class BlockTrappedChest extends BlockChest {
    private boolean activated;

    public BlockTrappedChest(String key, int id, Material material) {
        super(key, id, material);
        this.activated = false;
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
        return this.activated;
    }

    @Override
    public void onBlockRemoval(World world, int x, int y, int z) {
        if (this.activated) {
            world.notifyBlockChange(x, y, z, this.id);
        }

        super.onBlockRemoval(world, x, y, z);
    }

    @Override
    public boolean blockActivated(World world, int x, int y, int z, EntityPlayer player) {
        this.changeState(world, x, y, z);

        world.scheduleBlockUpdate(x, y, z, this.id, this.tickRate());
        world.playSoundEffect(SoundType.GUI_SOUNDS,x + 0.5, y + 0.5, z + 0.5,
                "random.click", 0.3f, 0.6f);

        return super.blockActivated(world, x, y, z, player);
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random rand) {
        if (!world.isClientSide) {
            if (this.activated) {
                this.changeState(world, x, y, z);
            }
        }
    }

    private void changeState(World world, int x, int y, int z) {
        // Reverses state.
        this.activated = !activated;

        // Updates block's neighbors.
        world.notifyBlockChange(x, y, z, id);
    }
}
