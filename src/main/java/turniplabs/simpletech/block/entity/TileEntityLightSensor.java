package turniplabs.simpletech.block.entity;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import turniplabs.simpletech.SimpleTech;
import turniplabs.simpletech.block.BlockLightSensor;

public class TileEntityLightSensor extends TileEntity {
    @Override
    public void updateEntity() {
        // If the world object is valid...
        if (worldObj != null && !worldObj.isClientSide) {
            Block block = getBlockType();
            // If it's a light sensor...
            if (block instanceof BlockLightSensor) {
                BlockLightSensor lightSensor = ((BlockLightSensor) block);
                boolean isDay = worldObj.isDaytime();
                boolean isPowered = SimpleTech.getRedstoneFromMetadata(
                        worldObj.getBlockMetadata(xCoord, yCoord, zCoord),
                        BlockLightSensor.redstoneOffset) > 0;
                boolean isInverted = lightSensor.isInverted(worldObj, xCoord, yCoord, zCoord);
                if (isInverted) {
                    // Daytime mode.
                    if (isDay && !isPowered)
                        // Sends redstone value.
                        lightSensor.updateSensor(worldObj, xCoord, yCoord, zCoord, true);
                    if (!isDay && isPowered)
                        lightSensor.updateSensor(worldObj, xCoord, yCoord, zCoord, false);
                } else {
                    // Nighttime mode.
                    if (isDay && isPowered)
                        lightSensor.updateSensor(worldObj, xCoord, yCoord, zCoord, false);
                    if (!isDay && !isPowered)
                        lightSensor.updateSensor(worldObj, xCoord, yCoord, zCoord, true);
                }
            }
        }
    }
}
