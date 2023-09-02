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
import turniplabs.halplibe.helper.BlockBuilder;
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

    // Builders
    public static final BlockBuilder miscBuilder = new BlockBuilder(MOD_ID)
            .setTopBottomTexture("misc_top_bottom.png")
            .setEastTexture("misc_side.png")
            .setWestTexture("misc_side.png")
            .setNorthTexture("misc_side.png")
            .setHardness(1.5f)
            .setResistance(10.0f)
            .setLuminance(0)
            .setBlockSound(BlockSounds.STONE);

    // Blocks
    public static final Block unpoweredFan = miscBuilder
            .setSouthTexture("fan_front.png")
            .setTags(BlockTags.MINEABLE_BY_PICKAXE)
            .build(new BlockFan("fan.unpowered", UNPOWERED_FAN_ID, Material.stone, false));
    public static final Block poweredFan = miscBuilder
            .setSouthTexture("fan_front_powered.png")
            .setTags(BlockTags.NOT_IN_CREATIVE_MENU, BlockTags.MINEABLE_BY_PICKAXE)
            .build(new BlockFan("fan.powered", POWERED_FAN_ID, Material.stone, true));
    public static final Block jumpPad = new BlockBuilder(MOD_ID)
            .setTextures("jump_pad.png")
            .setHardness(1.0f)
            .setResistance(2.5f)
            .setLuminance(0)
            .setBlockSound(BlockSounds.WOOD)
            .setTags(BlockTags.MINEABLE_BY_AXE)
            .build(new BlockJumpPad("jumppad", JUMP_PAD_ID, Material.wood));
    public static final Block trappedChest = new BlockBuilder(MOD_ID)
            .setHardness(2.5f)
            .setResistance(5.0f)
            .setLuminance(0)
            .setBlockSound(BlockSounds.WOOD)
            .setTags(BlockTags.FENCES_CONNECT, BlockTags.MINEABLE_BY_AXE)
            .build(new BlockTrappedChest("chest.trapped", TRAPPED_CHEST_ID, Material.wood));
    public static final Block lightSensor = new BlockBuilder(MOD_ID)
            .setTextures("light_sensor.png")
            .setHardness(1.0f)
            .setResistance(2.5f)
            .setLuminance(0)
            .setBlockSound(BlockSounds.WOOD)
            .setTags(BlockTags.MINEABLE_BY_AXE)
            .build(new BlockLightSensor("lightsensor", LIGHT_SENSOR_ID, Material.wood));
    public static final Block allocator = new BlockBuilder(MOD_ID)
            .setTopTexture("allocator_back_top_bottom.png")
            .setBottomTexture("allocator_front_top_bottom.png")
            .setEastTexture("misc_side.png")
            .setWestTexture("misc_top_bottom.png")
            .setNorthTexture("allocator_back.png")
            .setSouthTexture("allocator_front.png")
            .setHardness(1.5f)
            .setResistance(10.0f)
            .setLuminance(0)
            .setBlockSound(BlockSounds.STONE)
            .setTags(BlockTags.MINEABLE_BY_PICKAXE)
            .build(new BlockAllocator("allocator", ALLOCATOR_ID, Material.stone, true,
                    true));

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

    public static int setBit(int number, int position, int bit) {
        int mask = 1 << position;
        return (number & ~mask) | ((bit << position) & mask);
    }

    public static int getBit(int number, int offset) {
        return (number >> offset) & 1;
    }

    public static int getRedstoneFromMetadata(int metadata, int redstoneOffset) {
        return getBit(metadata, redstoneOffset);
    }

    public static int getInvertedFromMetadata(int metadata, int invertedOffset) {
        return getBit(metadata, invertedOffset);
    }

    public static int getMetaWithRedstone(int metadata, int redstone, int redstoneOffset) {
        return setBit(metadata, redstoneOffset, redstone);
    }

    public static int getMetaWithInverted(int metadata, int inverted, int invertedOffset) {
        return setBit(metadata, invertedOffset, inverted);
    }

    public static int get3DDirectionFromMeta(int meta) {
        return meta & 7;
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
