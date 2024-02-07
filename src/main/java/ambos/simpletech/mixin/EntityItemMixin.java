package ambos.simpletech.mixin;

import net.minecraft.core.entity.EntityItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EntityItem.class, remap = false)
final class EntityItemMixin {
    @Inject(method = "clumpToNearbyStack", at = @At("HEAD"), require = 0, cancellable = true)
    private void removeItemEntityStacking(CallbackInfo ci) {
        EntityItem currentEntityItem = (EntityItem) ((Object) this);

        if (!currentEntityItem.item.isStackable()) {
            ci.cancel();
        }
    }
}
