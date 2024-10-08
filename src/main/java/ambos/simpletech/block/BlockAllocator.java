package ambos.simpletech.block;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockTileEntity;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.entity.TileEntityChest;
import net.minecraft.core.block.entity.TileEntityFurnace;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.entity.vehicle.EntityMinecart;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.IInventory;
import net.minecraft.core.player.inventory.InventoryLargeChest;
import net.minecraft.core.sound.SoundCategory;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.util.phys.AABB;
import net.minecraft.core.world.World;

import java.util.List;
import java.util.Random;

import ambos.simpletech.IPlayerDisplayer;
import ambos.simpletech.SimpleTech;
import ambos.simpletech.block.entity.TileEntityAllocator;

public class BlockAllocator extends BlockTileEntity {
    private final boolean allowFiltering;
    private final boolean subItemFiltering;
    private Random random = new Random();

    public BlockAllocator(String key, int id, Material material, boolean allowFiltering, boolean subItemFiltering) {
        super(key, id, material);
        this.setTicking(true);
        this.allowFiltering = allowFiltering;
        this.subItemFiltering = subItemFiltering;
    }

    @Override
    protected TileEntity getNewBlockEntity() {
        return new TileEntityAllocator();
    }

    @Override
    public boolean onBlockRightClicked(World world, int x, int y, int z, EntityPlayer player, Side side, double xPlaced, double yPlaced) {
        if (!this.allowFiltering) {
            return false;
        } else if (world.isClientSide) {
            return true;
        } else {
            TileEntityAllocator allocator = (TileEntityAllocator) world.getBlockTileEntity(x, y, z);
            ((IPlayerDisplayer) player).simple_tech$displayGUIAllocator(allocator);
            return true;
        }
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random rand) {
        if (world.isBlockIndirectlyGettingPowered(x, y, z) || world.isBlockIndirectlyGettingPowered(x, y + 1, z)) {
            this.allocateItems(world, x, y, z, rand);
        }
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int blockId) {
        if (blockId > 0 && Block.blocksList[blockId].canProvidePower() &&
                (world.isBlockIndirectlyGettingPowered(x, y, z) ||
                        world.isBlockIndirectlyGettingPowered(x, y + 1, z))) {
            world.scheduleBlockUpdate(x, y, z, this.id, this.tickRate());
        }
    }

    @Override
    public int tickRate() {
        return 1;
    }

    @Override
    public void onBlockPlaced(World world, int x, int y, int z, Side side, EntityLiving entity, double sideHeight) {
        Direction placementDirection = entity.getPlacementDirection(side).getOpposite();
        world.setBlockMetadataWithNotify(x, y, z, placementDirection.getId());
    }

    @Override
    public void onBlockRemoved(World world, int x, int y, int z, int data) {
        if (world.getBlockTileEntity(x, y, z) != null) {
            TileEntityAllocator te = (TileEntityAllocator) world.getBlockTileEntity(x, y, z);
            if (te.getStackInSlot(0) != null) {
                dispenseItem(world, x, y, z, 0, 0, 0, te.getStackInSlot(0), random);
            }
        }
    }

    public int getRandomItemFromContainer(IInventory inventory, Random rand, World world, int x, int y, int z) {
        if (inventory == null) {
            return -1;
        } else {
            int i = -1;
            int j = 1;

            byte startAt = 0;

            if (inventory instanceof TileEntityFurnace) {
                startAt = 2;
            }

            for (int k = startAt; k < inventory.getSizeInventory(); ++k) {
                if (inventory.getStackInSlot(k) != null && this.passesFilter(world, x, y, z,
                        inventory.getStackInSlot(k)) && rand.nextInt(j) == 0) {
                    i = k;
                    ++j;
                }
            }

            return i;
        }
    }

    protected IInventory containerAtPos(World world, int x, int y, int z) {
        TileEntity tile = world.getBlockTileEntity(x, y, z);
        return !(tile instanceof IInventory) ? null : this.getDoubleChest(world, x, y, z);
    }

    protected boolean blockingCubeAtPos(World world, int x, int y, int z) {
        int blockID = world.getBlockId(x, y, z);
        boolean isOpaque = Block.translucent[blockID];

        return !(isOpaque || blockID == Block.glass.id ||
                blockID == Block.cactus.id ||
                blockID == Block.cake.id ||
                blockID == Block.blockSnow.id ||
                blockID == Block.mobspawner.id ||
                blockID == Block.fencePlanksOak.id ||
                blockID == 0);
    }

    private void putItemInContainer(IInventory inventory, ItemStack item, int index) {
        if (item != null) {
            if (index >= 0) {
                ItemStack stack = inventory.getStackInSlot(index);

                if (stack != null) {
                    stack.stackSize += item.stackSize;
                    inventory.setInventorySlotContents(index, stack);
                } else {
                    inventory.setInventorySlotContents(index, item);
                }
            }
        }
    }

    private void dispenseItem(World world, int x, int y, int z, int dx, int dy, int dz, ItemStack item, Random rand) {
        double d = (double) x + (double) dx * 0.5D + 0.5D;
        double d1 = (double) y + (double) dy * 0.5D + 0.5D;
        double d2 = (double) z + (double) dz * 0.5D + 0.5D;
        double d3 = rand.nextDouble() * 0.1D + 0.2D;

        EntityItem entityItem = new EntityItem(world, d, d1, d2, item);

        // Item movement.
        entityItem.xd = (double) dx * d3;
        entityItem.yd = (double) dy * d3;
        entityItem.zd = (double) dz * d3;
        entityItem.xd += rand.nextGaussian() * (double) 0.0075F * 6.0D;
        entityItem.yd += rand.nextGaussian() * (double) 0.0075F * 6.0D;
        entityItem.xd += rand.nextGaussian() * (double) 0.0075F * 6.0D;

        world.entityJoinedWorld(entityItem);
        world.playSoundEffect(null, SoundCategory.GUI_SOUNDS, x, y, z, "random.click", 1.0f, 1.0f);

        // Particle rendering.
        for (int i = 0; i < 10; ++i) {
            double d4 = rand.nextDouble() * 0.2D + 0.01D;
            double d5 = d + (double) dx * 0.01D + (rand.nextDouble() - 0.5D) * (double) dz * 0.5D;
            double d6 = d1 + (rand.nextDouble() - 0.5D) * 0.5D;
            double d7 = d2 + (double) dz * 0.01D + (rand.nextDouble() - 0.5D) * (double) dx * 0.5D;
            double d8 = (double) dx * d4 + rand.nextGaussian() * 0.01D;
            double d9 = -0.03D + rand.nextGaussian() * 0.01D;
            double d10 = (double) dz * d4 + rand.nextGaussian() * 0.01D;

            world.spawnParticle("smoke", d5, d6, d7, d8, d9, d10, 0);
        }
    }

    private boolean outputItem(World world, int x, int y, int z, int dx, int dy, int dz, ItemStack item, Random rand) {
        IInventory outputContainer = this.containerAtPos(world, x + dx, y + dy, z + dz);

        if (outputContainer == null) {
            List<Entity> index = world.getEntitiesWithinAABB(EntityMinecart.class, AABB.getBoundingBoxFromPool(
                    x + dx, y + dy, z + dz, x + dx + 1, y + dy + 1, z + dz + 1));

            if (!index.isEmpty() && (!(index.get(0) instanceof IInventory) ||
                    ((EntityMinecart) index.get(0)).minecartType == 1)) {
                outputContainer = (IInventory) index.get(0);
            }
        }

        if (outputContainer == null) {
            if (!this.blockingCubeAtPos(world, x + dx, y + dy, z + dz)) {
                this.dispenseItem(world, x, y, z, dx, dy, dz, item, rand);

                return true;
            }
        } else {
            int index1 = this.getFirstFreeInventorySlotOfKind(outputContainer, item);

            if (index1 >= 0) {
                this.putItemInContainer(outputContainer, item, index1);
                return true;
            }
        }

        return false;
    }

    private IInventory getDoubleChest(World world, int x, int y, int z) {
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
        if (!(tileEntity instanceof TileEntityChest)) {
            return tileEntity instanceof IInventory ? (IInventory) tileEntity : null;
        } else {
            int blockId = world.getBlockId(x, y, z);

            IInventory chest1 = (IInventory) world.getBlockTileEntity(x, y, z);
            IInventory chest2;

            if (world.getBlockId(x + 1, y, z) == blockId) {
                chest2 = (IInventory) world.getBlockTileEntity(x + 1, y, z);

                return new InventoryLargeChest("", chest1, chest2);
            } else if (world.getBlockId(x - 1, y, z) == blockId) {
                chest2 = (IInventory) world.getBlockTileEntity(x - 1, y, z);

                return new InventoryLargeChest("", chest2, chest1);
            } else if (world.getBlockId(x, y, z + 1) == blockId) {
                chest2 = (IInventory) world.getBlockTileEntity(x, y, z + 1);

                return new InventoryLargeChest("", chest1, chest2);
            } else if (world.getBlockId(x, y, z - 1) == blockId) {
                chest2 = (IInventory) world.getBlockTileEntity(x, y, z - 1);

                return new InventoryLargeChest("", chest2, chest1);
            } else {
                return chest1;
            }
        }
    }

    private void allocateItems(World world, int x, int y, int z, Random rand) {
        int dx = SimpleTech.getDirectionX(world, x, y, z);
        int dy = SimpleTech.getDirectionY(world, x, y, z);
        int dz = SimpleTech.getDirectionZ(world, x, y, z);

        IInventory inputContainer = this.containerAtPos(world, x - dx, y - dy, z - dz);

        List<Entity> entities;

        if (inputContainer == null) {
            entities = world.getEntitiesWithinAABB(EntityMinecart.class, AABB.getBoundingBoxFromPool(
                    x - dx, y - dy, z - dz, x - dx + 1, y - dy + 1, z - dz + 1));

            if (!entities.isEmpty() && (!(entities.get(0) instanceof IInventory) ||
                    ((EntityMinecart) entities.get(0)).minecartType == 1)) {
                inputContainer = (IInventory) entities.get(0);
            }
        }

        int itemIndex;
        if (inputContainer == null) {
            entities = world.getEntitiesWithinAABB(EntityItem.class, AABB.getBoundingBoxFromPool(
                    x - dx, y - dy, z - dz, x - dx + 1, y - dy + 1, z - dz + 1));

            for (itemIndex = 0; itemIndex < entities.size(); ++itemIndex) {
                if (entities.get(itemIndex) instanceof EntityItem) {
                    EntityItem itemType = (EntityItem) entities.get(itemIndex);

                    if (itemType.isAlive() && this.passesFilter(world, x, y, z, itemType.item) &&
                            this.outputItem(world, x, y, z, dx, dy, dz, itemType.item, rand)) {
                        itemType.outOfWorld();
                    }
                }
            }
        } else {
            itemIndex = this.getRandomItemFromContainer(inputContainer, rand, world, x, y, z);

            if (itemIndex >= 0) {
                int itemDamage = inputContainer.getStackInSlot(itemIndex).getItemDamageForDisplay();
                int itemSize = inputContainer.getStackInSlot(itemIndex).stackSize;

                ItemStack item = new ItemStack(inputContainer.getStackInSlot(itemIndex)
                        .getItem(), itemSize, itemDamage);

                if (this.outputItem(world, x, y, z, dx, dy, dz, item, rand)) {
                    inputContainer.decrStackSize(itemIndex, itemSize);
                }
            }
        }
    }

    private int getFirstFreeInventorySlotOfKind(IInventory inventory, ItemStack item) {
        int inventorySize = inventory.getSizeInventory();

        if (inventory instanceof TileEntityFurnace) {
            --inventorySize;
        }

        for (int i = 0; i < inventorySize; ++i) {
            boolean canStack = false;

            if (inventory.getStackInSlot(i) != null && inventory.getStackInSlot(i).itemID == item.itemID &&
                    (!item.getItem().getHasSubtypes() ||
                            inventory.getStackInSlot(i).getItemDamageForDisplay() == item.getItemDamageForDisplay())) {
                canStack = inventory.getStackInSlot(i).stackSize <= item.getMaxStackSize() - item.stackSize;
            }

            if (inventory.getStackInSlot(i) == null || canStack) {
                return i;
            }
        }

        return -1;
    }

    private boolean passesFilter(World world, int x, int y, int z, ItemStack item) {
        if (!this.allowFiltering) {
            return true;
        } else {
            TileEntityAllocator tileentityallocator = (TileEntityAllocator) world.getBlockTileEntity(x, y, z);
            ItemStack filterItem = tileentityallocator.getStackInSlot(0);
            if (filterItem == null) {
                return true;
            } else {
                boolean filterSubItems = true;
                if (this.subItemFiltering) {
                    filterSubItems = filterItem.getItemDamageForDisplay() == item.getItemDamageForDisplay();
                }

                return filterItem.itemID == item.getItem().id && filterSubItems;
            }
        }
    }
}
