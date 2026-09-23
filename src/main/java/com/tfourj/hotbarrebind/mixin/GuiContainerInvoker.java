package com.tfourj.hotbarrebind.mixin;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(GuiContainer.class)
public interface GuiContainerInvoker {
    @Invoker("handleMouseClick")
    void hotbarRebind$handleMouseClick(Slot slot, int slotId, int hotbarSlot, int clickType);
}
