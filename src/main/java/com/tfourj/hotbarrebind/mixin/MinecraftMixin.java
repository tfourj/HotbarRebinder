package com.tfourj.hotbarrebind.mixin;

import com.tfourj.hotbarrebind.HotbarRebind;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
    @Shadow
    public EntityPlayerSP thePlayer;

    @Shadow
    public GuiScreen currentScreen;

    @Inject(method = "runTick", at = @At("TAIL"))
    private void hotbarRebind$selectAdditionalHotbarSlot(CallbackInfo callback) {
        int hotbarSlot = HotbarRebind.consumePressedHotbarSlot();
        if (hotbarSlot >= 0 && thePlayer != null && currentScreen == null) {
            thePlayer.inventory.currentItem = hotbarSlot;
        }
    }
}
