package com.tfourj.hotbarrebind.mixin;

import com.tfourj.hotbarrebind.HotbarRebind;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiScreen.class)
public abstract class GuiScreenMixin {
    @Inject(method = "handleKeyboardInput", at = @At("HEAD"), cancellable = true)
    private void hotbarRebind$handleAdditionalKeyboardBind(CallbackInfo callback) {
        if (!((Object) this instanceof GuiContainer) || !Keyboard.getEventKeyState()) {
            return;
        }

        int eventKey = Keyboard.getEventKey();
        if (eventKey == Keyboard.KEY_NONE) {
            eventKey = Keyboard.getEventCharacter() + 256;
        }

        int hotbarSlot = HotbarRebind.findHotbarSlot(eventKey);
        if (hotbarSlot >= 0) {
            HotbarRebind.moveHoveredSlot((GuiContainer) (Object) this, hotbarSlot);
            callback.cancel();
        }
    }

    @Inject(method = "handleMouseInput", at = @At("HEAD"), cancellable = true)
    private void hotbarRebind$handleAdditionalMouseBind(CallbackInfo callback) {
        if (!((Object) this instanceof GuiContainer) || !Mouse.getEventButtonState()) {
            return;
        }

        int mouseButton = Mouse.getEventButton();
        if (mouseButton < 0) {
            return;
        }

        int hotbarSlot = HotbarRebind.findHotbarSlot(mouseButton - 100);
        if (hotbarSlot >= 0) {
            HotbarRebind.moveHoveredSlot((GuiContainer) (Object) this, hotbarSlot);
            callback.cancel();
        }
    }
}
