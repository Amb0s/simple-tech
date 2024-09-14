package ambos.simpletech.block;

import net.minecraft.core.util.helper.Side;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.World;

public class BlockJumpPad extends Block {
    public BlockJumpPad(String key, int id, Material material) {
        super(key, id, material);
    }

    @Override
    public boolean isSolidRender() {
        return false;
    }

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        return world.isBlockOpaqueCube(x, y - 1, z);
    }

    public void jump(Entity entity) {
        if ((entity instanceof EntityLiving || entity instanceof EntityItem) && entity.yd < 1.0D) {
            entity.yd = 0.0D;
            entity.fallDistance = 0.0F;
            entity.push(0.0D, 1.0D, 0.0D);
        }
    }

    @Override
    public void onEntityWalking(World world, int x, int y, int z, Entity entity) {
        this.jump(entity);
    }

    @Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
        if (entity.y > (double) y) {
            this.jump(entity);
        }
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
}
