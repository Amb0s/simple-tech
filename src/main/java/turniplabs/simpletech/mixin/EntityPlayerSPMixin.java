package turniplabs.simpletech.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.EntityPlayerSP;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import turniplabs.simpletech.IPlayerDisplayer;
import turniplabs.simpletech.block.entity.TileEntityAllocator;
import turniplabs.simpletech.gui.GuiAllocator;

@Mixin(value = EntityPlayerSP.class, remap = false)
public abstract class EntityPlayerSPMixin extends EntityPlayer implements IPlayerDisplayer {
    @Shadow protected Minecraft mc;

    public EntityPlayerSPMixin(World world) {
        super(world);
    }

    @Override
    public void simple_tech$displayGUIAllocator(TileEntityAllocator allocator) {
        if (allocator != null){
            this.mc.displayGuiScreen(new GuiAllocator(inventory, allocator));
        }
    }
}
