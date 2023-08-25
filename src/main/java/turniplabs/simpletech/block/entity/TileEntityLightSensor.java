package turniplabs.simpletech.block.entity;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import turniplabs.simpletech.block.BlockLightSensor;

public class TileEntityLightSensor extends TileEntity {
    @Override
    public void updateEntity() {
        // If the world object if valid...
        if (this.worldObj != null && !this.worldObj.isClientSide) {
            Block block = this.getBlockType();
            // If it's a light sensor...
            if (block instanceof BlockLightSensor) {
                BlockLightSensor lightSensor = ((BlockLightSensor) block);
                byte redstone;
                if (lightSensor.isInverted(this.worldObj, this.xCoord, this.yCoord, this.zCoord)) {
                    redstone = (byte) (this.worldObj.isDaytime() ? 1 : 0); // Daytime mode.
                } else {
                    redstone = (byte) (this.worldObj.isDaytime() ? 0 : 1); // Nighttime mode.
                }

                // Sends redstone value.
                lightSensor.updateSensor(this.worldObj, this.xCoord, this.yCoord,
                        this.zCoord, redstone);
            }
        }
    }
}
