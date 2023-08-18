package turniplabs.simpletech;

import net.fabricmc.api.ModInitializer;
import net.minecraft.client.sound.block.BlockSounds;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.item.Item;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.helper.BlockHelper;
import turniplabs.halplibe.helper.EntityHelper;
import turniplabs.halplibe.helper.RecipeHelper;
import turniplabs.simpletech.block.*;
import turniplabs.simpletech.block.entity.TileEntityAllocator;
import turniplabs.simpletech.block.entity.TileEntityFan;
import turniplabs.simpletech.block.entity.TileEntityLightSensor;

public class SimpleTech implements ModInitializer {
    public static final String MOD_ID = "simpletech";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final int FAN_RANGE = 4;
    public static final int UNPOWERED_FAN_ID = 3789;
    public static final int POWERED_FAN_ID = 3790;
    public static final int JUMP_PAD_ID = 3791;
    public static final int TRAPPED_CHEST_ID = 3792;
    public static final int LIGHT_SENSOR_ID = 3793;
    public static final int ALLOCATOR_ID = 3794;

    // Blocks
    public static final Block unpoweredFan = BlockHelper.createBlock(
            MOD_ID, new BlockFan("block.fan.unpowered", UNPOWERED_FAN_ID, Material.stone,false,
                    "misc_top_bottom.png","misc_side.png","fan_front.png"),
            BlockSounds.STONE, 1.5f, 10.0f, 0.0f)
            .withTags(BlockTags.MINEABLE_BY_PICKAXE);
    public static final Block poweredFan = BlockHelper.createBlock(
            MOD_ID, new BlockFan("block.fan.powered", POWERED_FAN_ID, Material.stone,true,
                    "misc_top_bottom.png","misc_side.png","fan_front_powered.png"),
            BlockSounds.STONE, 1.5f, 10.0f, 0.0f)
            .withTags(BlockTags.NOT_IN_CREATIVE_MENU, BlockTags.MINEABLE_BY_PICKAXE);
    public static final Block jumpPad = BlockHelper.createBlock(
            MOD_ID, new BlockJumpPad("block.jumppad", JUMP_PAD_ID, Material.wood),
            "jump_pad.png", BlockSounds.WOOD, 1.0f, 2.5f, 0.0f)
            .withTags(BlockTags.MINEABLE_BY_AXE);
    public static final Block trappedChest = BlockHelper.createBlock(
                    MOD_ID, new BlockTrappedChest("chest.trapped", TRAPPED_CHEST_ID, Material.wood),
                    BlockSounds.WOOD, 2.5f, 5.0f, 0.0f)
            .withTags(BlockTags.FENCES_CONNECT, BlockTags.MINEABLE_BY_AXE);
    public static final Block lightSensor = BlockHelper.createBlock(
                    MOD_ID, new BlockLightSensor("block.lightsensor", LIGHT_SENSOR_ID, Material.wood),
                    "light_sensor.png", BlockSounds.WOOD, 1.0f, 2.5f, 0.0f)
            .withTags(BlockTags.MINEABLE_BY_AXE);
    public static final Block allocator = BlockHelper.createBlock(
                    MOD_ID, new BlockAllocator("block.allocator", ALLOCATOR_ID, Material.stone,
                            "misc_top_bottom.png", "misc_side.png", "allocator_front.png",
                            "allocator_back.png", "allocator_front_top_bottom.png",
                            "allocator_back_top_bottom.png", true, true),
                    BlockSounds.STONE, 1.5f, 10.0f, 0.0f)
            .withTags(BlockTags.MINEABLE_BY_PICKAXE);

    @Override
    public void onInitialize() {
        // Entities.
        EntityHelper.createTileEntity(TileEntityFan.class, "Fan");
        EntityHelper.createTileEntity(TileEntityLightSensor.class, "Light Sensor");
        EntityHelper.createTileEntity(TileEntityAllocator.class, "Allocator");

        // Recipes.
        RecipeHelper.Crafting.createRecipe(unpoweredFan, 1, new Object[]{
                        "CCC",
                        "CIC",
                        "CRC",
                        'C', Block.cobbleStone,
                        'I', Item.ingotIron,
                        'R', Item.dustRedstone
        });
        RecipeHelper.Crafting.createShapelessRecipe(jumpPad, 1, new Object[]{
                Item.slimeball, Block.slabPlanksOak
        });
        RecipeHelper.Crafting.createShapelessRecipe(trappedChest, 1, new Object[]{
                Item.dustRedstone, Block.chestPlanksOak
        });
        RecipeHelper.Crafting.createRecipe(lightSensor, 1, new Object[]{
                " G ",
                " Q ",
                " S ",
                'G', Block.glass,
                'Q', Item.quartz,
                'S', Block.slabPlanksOak
        });
        RecipeHelper.Crafting.createRecipe(allocator, 1, new Object[]{
                "CRC",
                "CGC",
                "CRC",
                'C', Block.cobbleStone,
                'R', Item.dustRedstone,
                'G', Item.ingotGold,
        });

        LOGGER.info("Simple Tech initialized.");
    }

    public static int getRedstoneFromMetadata(int metadata) {
        return metadata >> 4; // or 3?
    }

    public static int getDirectionFromMetadata(int metadata) {
        return metadata & 7;
    }

    public static int createMetadata(int direction, int redstone) {
        return (redstone << 4) | direction;
    }

    public static int getOppositeDirectionById(int i) {
        return Direction.getDirectionById(i).getOpposite().getId();
    }

    public static int getDirectionX(World world, int x, int y, int z) {
        int direction = world.getBlockMetadata(x, y, z);
        int dx = 0;

        if (direction == Direction.WEST.getId()) {
            dx = 1;
        }

        if (direction == Direction.EAST.getId()) {
            dx = -1;
        }

        return dx;
    }

    public static int getDirectionY(World world, int x, int y, int z) {
        int direction = world.getBlockMetadata(x, y, z);
        int dy = 0;

        if (direction == Direction.DOWN.getId()) {
            dy = 1;
        }

        if (direction == Direction.UP.getId()) {
            dy = -1;
        }

        return dy;
    }

    public static int getDirectionZ(World world, int x, int y, int z) {
        int direction = world.getBlockMetadata(x, y, z);
        int dz = 0;

        if (direction == Direction.NORTH.getId()) {
            dz = 1;
        }

        if (direction == Direction.SOUTH.getId()) {
            dz = -1;
        }

        return dz;
    }
}
