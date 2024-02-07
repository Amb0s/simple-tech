package ambos.simpletech.mixin;

import net.minecraft.core.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import ambos.simpletech.IPlayerDisplayer;
import ambos.simpletech.block.entity.TileEntityAllocator;

@Mixin(value = EntityPlayer.class, remap = false)
public class EntityPlayerMixin implements IPlayerDisplayer {
    @Unique
    public void simple_tech$displayGUIAllocator(TileEntityAllocator allocator) {
    }
}
