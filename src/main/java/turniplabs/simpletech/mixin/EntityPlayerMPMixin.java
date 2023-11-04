package turniplabs.simpletech.mixin;

import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.net.packet.Packet100OpenWindow;
import net.minecraft.core.world.World;
import net.minecraft.server.entity.player.EntityPlayerMP;
import net.minecraft.server.net.handler.NetServerHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import turniplabs.simpletech.IPlayerDisplayer;
import turniplabs.simpletech.SimpleTech;
import turniplabs.simpletech.block.entity.TileEntityAllocator;
import turniplabs.simpletech.player.inventory.ContainerAllocator;

@Mixin(value = EntityPlayerMP.class, remap = false)
public abstract class EntityPlayerMPMixin extends EntityPlayer implements IPlayerDisplayer {
    @Shadow protected abstract void getNextWindowId();

    @Shadow public NetServerHandler playerNetServerHandler;

    @Shadow private int currentWindowId;

    public EntityPlayerMPMixin(World world) {
        super(world);
    }

    @Override
    public void simple_tech$displayGUIAllocator(TileEntityAllocator allocator) {
        this.getNextWindowId();
        this.playerNetServerHandler.sendPacket(new Packet100OpenWindow(this.currentWindowId, SimpleTech.ALLOCATOR_GUI_ID, allocator.getInvName(), allocator.getSizeInventory()));
        this.craftingInventory = new ContainerAllocator(this.inventory, allocator);
        this.craftingInventory.windowId = this.currentWindowId;
        this.craftingInventory.onContainerInit((EntityPlayerMP)(Object)this);
    }
}
