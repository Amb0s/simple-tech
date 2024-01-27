package turniplabs.simpletech.block.entity;

import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.util.phys.AABB;
import net.minecraft.core.world.World;
import turniplabs.simpletech.SimpleTech;

import java.util.List;

public class TileEntityFan extends TileEntity {
    private final int range;

    public TileEntityFan(int range) {
        this.range = range;
    }

    public TileEntityFan() {
        // Always define the default constructor when working with tile entities.
        this(SimpleTech.FAN_RANGE);
    }

    @Override
    public void tick() {
        if (worldObj.isBlockIndirectlyGettingPowered(x, y, z) ||
                worldObj.isBlockIndirectlyGettingPowered(x, y + 1, z)) {
            this.blow(worldObj, x, y, z);
        }
    }

    private void blow(World world, int x, int y, int z) {
        int dx = -SimpleTech.getDirectionX(world, x, y, z);
        int dy = -SimpleTech.getDirectionY(world, x, y, z);
        int dz = -SimpleTech.getDirectionZ(world, x, y, z);

        int px = x;
        int py = y;
        int pz = z;

        for (int i = 0; i < this.range; ++i) {
            px += dx;
            py += dy;
            pz += dz;

            if (world.isBlockOpaqueCube(px, py, pz)) {
                break;
            }

            List<Entity> entities = world.getEntitiesWithinAABB(Entity.class, AABB.getBoundingBoxFromPool(
                    px, py, pz, px + 1, py + 1, pz + 1)
            );

            for (Entity entity : entities) {
                if (entity instanceof EntityItem) {
                    this.pushEntity(entity, dx, dy, dz);
                }
            }
        }
    }

    private void pushEntity(Entity entity, int dx, int dy, int dz) {
        double maxspeed = 0.4;
        double boost = 0.1;

        if (Math.abs(dx) != 0) {
            if (entity.xd * (double) dx < 0.0) {
                entity.xd = 0.0;
            }

            if (entity.xd * (double) dx <= maxspeed) {
                entity.xd += (double) dx * boost;
            }
        } else if (Math.abs(dy) != 0) {
            if (entity.yd * (double) dy < 0.0) {
                entity.yd = 0.0;
            }

            if (dy > 0) {
                boost *= 0.5;
            }

            if (entity.yd * (double) dy <= maxspeed) {
                entity.yd += (double) dy * boost;
            }
        } else if (Math.abs(dz) != 0) {
            if (entity.zd * (double) dz < 0.0) {
                entity.zd = 0.0;
            }

            if (entity.zd * (double) dz <= maxspeed) {
                entity.zd += (double) dz * boost;
            }
        }
    }
}
