package turniplabs.simpletech.block;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;
import turniplabs.simpletech.SimpleTech;

import java.util.Random;

public class BlockRedstoneNotGate extends Block {
    private final boolean isPowered;

    public BlockRedstoneNotGate(String key, int id, Material material, boolean isPowered) {
        super(key, id, material);
        this.isPowered = isPowered;
    }

    @Override
    public void setBlockBoundsBasedOnState(World world, int x, int y, int z) {
        // Sets block shape when placed.
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.125f, 1.0f);
    }

    @Override
    public void setBlockBoundsForItemRender() {
        // Sets block shape when rendered inside containers.
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.125f, 1.0f);
    }

    @Override
    public boolean isSolidRender() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        return !world.canPlaceOnSurfaceOfBlock(x, y - 1, z) ? false : super.canPlaceBlockAt(world, x, y, z);
    }

    @Override
    public ItemStack[] getBreakResult(World world, EnumDropCause dropCause, int x, int y, int z, int meta, TileEntity tileEntity) {
        return new ItemStack[]{new ItemStack(SimpleTech.notGate)};
    }

    @Override
    public boolean canBlockStay(World world, int x, int y, int z) {
        return !world.canPlaceOnSurfaceOfBlock(x, y - 1, z) ? false : super.canBlockStay(world, x, y, z);
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random rand) {
        int l = world.getBlockMetadata(x, y, z);
        boolean isPowering = this.unknown(world, x, y, z, l);
        if (this.isPowered && !isPowering) {
            world.setBlockAndMetadataWithNotify(x, y, z, SimpleTech.notGateIdle.id, l);
        } else if (!this.isPowered) {
            world.setBlockAndMetadataWithNotify(x, y, z, SimpleTech.notGateActive.id, l);
        }
    }

    @Override
    public int getBlockTextureFromSideAndMetadata(Side side, int j) {
        if (side == Side.BOTTOM) {
            return !this.isPowered ? texCoordToIndex(3, 7) : texCoordToIndex(3, 6); // Defaults to top/bottom texture.
         } else if (side == Side.TOP) {
            return !this.isPowered ? texCoordToIndex(3, 8) : texCoordToIndex(3, 9); // Defaults to top/bottom texture.
         } else {
            return texCoordToIndex(5, 0);
         }
    }

    @Override
    public boolean shouldSideBeRendered(WorldSource blockAccess, int x, int y, int z, int side) {
        return side != 0 && side != 1; // Don't render bottom and top textures to avoid z-fighting with modified renderer.
    }

    @Override
    public boolean isIndirectlyPoweringTo(World world, int x, int y, int z, int side) {
        return this.isPoweringTo(world, x, y, z, side);
    }

    @Override
    public boolean isPoweringTo(WorldSource blockAccess, int x, int y, int z, int side) {
        if (!this.isPowered && side != 5) {
            return true;
        } else {
            int direction = blockAccess.getBlockMetadata(x, y, z) & 3;
            if (direction == 0 && side == 3) {
                return false;
            } else if (direction == 1 && side == 4) {
                return false;
            } else if (direction == 1 && side == 5) {
                return false;
            } else if (direction == 2 && side == 2) {
                return false;
            } else {
                return !(direction == 3 && side == 5);
            }
        }
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int blockId) {
        if (!this.canBlockStay(world, x, y, z)) {
            this.dropBlockWithCause(world, EnumDropCause.WORLD, x, y, z, world.getBlockMetadata(x, y, z), null);
            world.setBlockWithNotify(x, y, z, 0);
        } else {
            int i1 = world.getBlockMetadata(x, y, z);
            boolean flag = this.unknown(world, x, y, z, i1);
            if (this.isPowered && !flag) {
                world.scheduleBlockUpdate(x, y, z, this.id, 1);
            } else if (!this.isPowered && flag) {
                world.scheduleBlockUpdate(x, y, z, this.id, 1);
            }
        }
    }

    private boolean unknown(World world, int i, int j, int k, int metadata) {
        int direction = metadata & 3;
        switch (direction) {
            case 0:
                return world.isBlockIndirectlyProvidingPowerTo(i, j, k + 1, 3) ||
                        world.getBlockId(i, j, k + 1) == Block.wireRedstone.id &&
                                world.getBlockMetadata(i, j, k + 1) > 0;
            case 1:
                return world.isBlockIndirectlyProvidingPowerTo(i - 1, j, k, 4) ||
                        world.getBlockId(i - 1, j, k) == Block.wireRedstone.id &&
                                world.getBlockMetadata(i - 1, j, k) > 0;
            case 2:
                return world.isBlockIndirectlyProvidingPowerTo(i, j, k - 1, 2) ||
                        world.getBlockId(i, j, k - 1) == Block.wireRedstone.id &&
                                world.getBlockMetadata(i, j, k - 1) > 0;
            case 3:
                return world.isBlockIndirectlyProvidingPowerTo(i + 1, j, k, 5) ||
                        world.getBlockId(i + 1, j, k) == Block.wireRedstone.id &&
                                world.getBlockMetadata(i + 1, j, k) > 0;
            default:
                return false;
        }
    }

    @Override
    public boolean canProvidePower() {
        return false;
    }

    @Override
    public void onBlockPlaced(World world, int x, int y, int z, Side side, EntityLiving entity, double sideHeight) {
        int l = entity.getHorizontalPlacementDirection(side).getHorizontalIndex();
        world.setBlockMetadataWithNotify(x, y, z, l);
        boolean flag = this.unknown(world, x, y, z, l);
        if (flag) {
            world.scheduleBlockUpdate(x, y, z, this.id, 1);
        }
    }

    @Override
    public void onBlockAdded(World world, int i, int j, int k) {
        world.notifyBlocksOfNeighborChange(i + 1, j, k, this.id);
        world.notifyBlocksOfNeighborChange(i - 1, j, k, this.id);
        world.notifyBlocksOfNeighborChange(i, j, k + 1, this.id);
        world.notifyBlocksOfNeighborChange(i, j, k - 1, this.id);
        world.notifyBlocksOfNeighborChange(i, j - 1, k, this.id);
        world.notifyBlocksOfNeighborChange(i, j + 1, k, this.id);
    }
}
