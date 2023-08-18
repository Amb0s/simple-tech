package turniplabs.simpletech.mixin;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Minecraft.class, remap = false)
final class MinecraftMixin {
    @Shadow
    private static Minecraft theMinecraft;

    @Inject(method = "getMinecraft(Ljava/lang/Class;)Lnet/minecraft/client/Minecraft;", at = @At("HEAD"),
            require = 0, cancellable = true)
    private static void removeGetterLimitation(Class<?> caller, CallbackInfoReturnable<Minecraft> cir) {
        cir.setReturnValue(theMinecraft);
    }
}
