package turniplabs.simpletech.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.net.handler.NetClientHandler;
import net.minecraft.core.net.packet.Packet100OpenWindow;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import turniplabs.simpletech.IPlayerDisplayer;
import turniplabs.simpletech.SimpleTech;
import turniplabs.simpletech.block.entity.TileEntityAllocator;

@Mixin(value = NetClientHandler.class, remap = false)
public class NetClientHandlerMixin {
    @Final
    @Shadow private Minecraft mc;

    @Inject(method = "handleOpenWindow(Lnet/minecraft/core/net/packet/Packet100OpenWindow;)V", at = @At("HEAD"), cancellable = true)
    public void handleAllocator(Packet100OpenWindow packet100openwindow, CallbackInfo ci){
        if (packet100openwindow.inventoryType == SimpleTech.ALLOCATOR_GUI_ID) {
            TileEntityAllocator tileEntityAllocator = new TileEntityAllocator();
            ((IPlayerDisplayer)this.mc.thePlayer).simple_tech$displayGUIAllocator(tileEntityAllocator);
            this.mc.thePlayer.craftingInventory.windowId = packet100openwindow.windowId;
            ci.cancel();
        }
    }
}
