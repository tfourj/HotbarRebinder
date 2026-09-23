package com.tfourj.hotbarrebind;

import com.tfourj.hotbarrebind.mixin.GuiContainerInvoker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

@Mod(
    modid = HotbarRebind.MODID,
    name = HotbarRebind.NAME,
    version = HotbarRebind.VERSION,
    acceptedMinecraftVersions = "[1.8.9]",
    clientSideOnly = true
)
public class HotbarRebind {
    public static final String MODID = "hotbarrebind";
    public static final String NAME = "Hotbar Rebind";
    public static final String VERSION = "3.0.0";

    private static final int HOTBAR_SIZE = 9;
    private static final int HOTBAR_SWAP_CLICK_TYPE = 2;
    private static final String KEY_CATEGORY = "key.categories.hotbarrebind";

    private static final KeyBinding[] HOTBAR_KEYS = new KeyBinding[HOTBAR_SIZE];

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        ClientCommandHandler.instance.registerCommand(new TestCommand());

        for (int slot = 0; slot < HOTBAR_SIZE; slot++) {
            HOTBAR_KEYS[slot] = new KeyBinding(
                "key.hotbarrebind.slot." + (slot + 1),
                Keyboard.KEY_NONE,
                KEY_CATEGORY
            );
            ClientRegistry.registerKeyBinding(HOTBAR_KEYS[slot]);
        }
    }

    public static int consumePressedHotbarSlot() {
        for (int slot = 0; slot < HOTBAR_SIZE; slot++) {
            if (HOTBAR_KEYS[slot] != null && HOTBAR_KEYS[slot].isPressed()) {
                return slot;
            }
        }

        return -1;
    }

    public static int findHotbarSlot(int keyCode) {
        if (keyCode == Keyboard.KEY_NONE) {
            return -1;
        }

        for (int slot = 0; slot < HOTBAR_SIZE; slot++) {
            if (HOTBAR_KEYS[slot] != null && HOTBAR_KEYS[slot].getKeyCode() == keyCode) {
                return slot;
            }
        }

        return -1;
    }

    public static boolean moveHoveredSlot(GuiContainer container, int hotbarSlot) {
        Minecraft minecraft = Minecraft.getMinecraft();
        if (minecraft.thePlayer == null || minecraft.playerController == null
            || minecraft.thePlayer.inventory.getItemStack() != null) {
            return false;
        }

        Slot hoveredSlot = container.getSlotUnderMouse();
        if (hoveredSlot == null) {
            return false;
        }

        ((GuiContainerInvoker) container).hotbarRebind$handleMouseClick(
            hoveredSlot,
            hoveredSlot.slotNumber,
            hotbarSlot,
            HOTBAR_SWAP_CLICK_TYPE
        );
        return true;
    }

    private static class TestCommand extends CommandBase {
        @Override
        public String getCommandName() {
            return "hotbarrebindtest";
        }

        @Override
        public String getCommandUsage(ICommandSender sender) {
            return "/hotbarrebindtest";
        }

        @Override
        public void processCommand(ICommandSender sender, String[] args) {
            sender.addChatMessage(new ChatComponentText("Hotbar Rebind is loaded and working! (v" + VERSION + ")"));
        }

        @Override
        public int getRequiredPermissionLevel() {
            return 0;
        }
    }
}
