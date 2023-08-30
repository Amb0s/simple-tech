package turniplabs.simpletech.block;

import net.minecraft.core.block.BlockTileEntity;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import turniplabs.simpletech.SimpleTech;
import turniplabs.simpletech.block.entity.TileEntityFan;

import java.util.Random;

public class BlockFan extends BlockTileEntity {
   private final boolean isPowered;

   public BlockFan(String key, int id, Material material, boolean isPowered) {
      super(key, id, material);
      this.isPowered = isPowered;
   }

   @Override
   public ItemStack[] getBreakResult(World world, EnumDropCause dropCause, int x, int y, int z, int meta,
                                     TileEntity tileEntity) {
      // Only drops unpowered fan when broken.
      // Should use BlockBuilder.setBlockDrop instead?
      return dropCause != EnumDropCause.IMPROPER_TOOL ? new ItemStack[]{new ItemStack(SimpleTech.unpoweredFan)} : null;
   }

   @Override
   public int tickRate() {
      return 2;
   }

   @Override
   public int getBlockTextureFromSideAndMetadata(Side side, int j) {
      int direction = SimpleTech.get3DDirectionFromMeta(j);
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

   @Override
   public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
      // Particle rendering.
      if (this.isPowered) {
         int dx = -SimpleTech.getDirectionX(world, x, y, z);
         int dy = -SimpleTech.getDirectionY(world, x, y, z);
         int dz = -SimpleTech.getDirectionZ(world, x, y, z);

         for (int i = 1; i < 3; ++i) {
            double rx = rand.nextDouble() - 0.5;
            double ry = rand.nextDouble() - 0.5;
            double rz = rand.nextDouble() - 0.5;
            world.spawnParticle("smoke",
                    (double) (x + dx) + 0.5 + rx,
                    (double) (y + dy) + 0.5 + ry,
                    (double) (z + dz) + 0.5 + rz,
                    0.2 * (double) dx,
                    0.2 * (double) dy,
                    0.2 * (double) dz
            );
         }
      }
   }

   @Override
   public void onNeighborBlockChange(World world, int x, int y, int z, int l) {
      int direction;

      // If it's currently powered by redstone...
      if (world.isBlockIndirectlyGettingPowered(x, y, z) || world.isBlockIndirectlyGettingPowered(x, y + 1, z)) {
         // If it wasn't already powered...
         if (!this.isPowered) {
            // Replaces the unpowered fan by its powered counterpart.
            direction = world.getBlockMetadata(x, y, z);
            world.setBlockAndMetadataWithNotify(x, y, z, SimpleTech.POWERED_FAN_ID, direction);
         }

         world.scheduleBlockUpdate(x, y, z, this.id, this.tickRate());
      } else if (this.isPowered) {
         // Replaces the powered fan by its unpowered counterpart.
         direction = world.getBlockMetadata(x, y, z);
         world.setBlockAndMetadataWithNotify(x, y, z, SimpleTech.UNPOWERED_FAN_ID, direction);
      }
   }

   @Override
   public void onBlockPlaced(World world, int x, int y, int z, Side side, EntityLiving entity, double sideHeight) {
      Direction placementDirection = entity.getPlacementDirection(side).getOpposite();
      world.setBlockMetadataWithNotify(x, y, z, placementDirection.getId());
   }

   @Override
   protected TileEntity getNewBlockEntity() {
      return new TileEntityFan();
   }
}
