package ambos.simpletech.btwaila;

import ambos.simpletech.block.entity.TileEntityAllocator;

import toufoumaster.btwaila.entryplugins.waila.BTWailaCustomTooltipPlugin;
import toufoumaster.btwaila.gui.components.AdvancedInfoComponent;
import toufoumaster.btwaila.util.ProgressBarOptions;
import toufoumaster.btwaila.tooltips.TooltipRegistry;
import toufoumaster.btwaila.tooltips.TileTooltip;

import net.minecraft.core.item.ItemStack;

import org.slf4j.Logger;

public class BTWailaPlugin implements BTWailaCustomTooltipPlugin {
    public static class AllocatorTooltip extends TileTooltip<TileEntityAllocator> {
        @Override
        public void initTooltip() {
            addClass(TileEntityAllocator.class);
        }

        @Override
        public void drawAdvancedTooltip(TileEntityAllocator tileEntityAllocator, AdvancedInfoComponent advancedInfoComponent) {
            advancedInfoComponent.drawItemList(new ItemStack[]{tileEntityAllocator.getStackInSlot(0)}, 0);
        }
    }

    @Override
    public void initializePlugin(TooltipRegistry tooltipRegistry, Logger logger) {
        tooltipRegistry.register(new AllocatorTooltip());
    }
}