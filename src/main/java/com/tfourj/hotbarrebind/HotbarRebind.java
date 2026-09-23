package com.tfourj.hotbarrebind;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

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
    private static final Method HANDLE_MOUSE_CLICK = ReflectionHelper.findMethod(
        GuiContainer.class,
        (GuiContainer) null,
        new String[] {"handleMouseClick", "func_146984_a"},
        Slot.class,
        int.class,
        int.class,
        int.class
    );

    private final KeyBinding[] hotbarKeys = new KeyBinding[HOTBAR_SIZE];

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        ClientCommandHandler.instance.registerCommand(new TestCommand());

        for (int slot = 0; slot < HOTBAR_SIZE; slot++) {
            hotbarKeys[slot] = new KeyBinding(
                "key.hotbarrebind.slot." + (slot + 1),
                Keyboard.KEY_NONE,
                KEY_CATEGORY
            );
            ClientRegistry.registerKeyBinding(hotbarKeys[slot]);
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        Minecraft minecraft = Minecraft.getMinecraft();
        for (int slot = 0; slot < HOTBAR_SIZE; slot++) {
            if (hotbarKeys[slot].isPressed()) {
                if (minecraft.thePlayer != null && minecraft.currentScreen == null) {
                    minecraft.thePlayer.inventory.currentItem = slot;
                }
                break;
            }
        }
    }

    @SubscribeEvent
    public void onGuiKeyboardInput(GuiScreenEvent.KeyboardInputEvent.Pre event) {
        if (!(event.gui instanceof GuiContainer) || !Keyboard.getEventKeyState()) {
            return;
        }

        int eventKey = Keyboard.getEventKey();
        if (eventKey == Keyboard.KEY_NONE) {
            eventKey = Keyboard.getEventCharacter() + 256;
        }

        handleGuiBinding(event, eventKey);
    }

    @SubscribeEvent
    public void onGuiMouseInput(GuiScreenEvent.MouseInputEvent.Pre event) {
        if (!(event.gui instanceof GuiContainer) || !Mouse.getEventButtonState()) {
            return;
        }

        int mouseButton = Mouse.getEventButton();
        if (mouseButton >= 0) {
            handleGuiBinding(event, mouseButton - 100);
        }
    }

    private void handleGuiBinding(GuiScreenEvent event, int keyCode) {
        int hotbarSlot = findHotbarSlot(keyCode);
        if (hotbarSlot < 0) {
            return;
        }

        moveHoveredSlot((GuiContainer) event.gui, hotbarSlot);
        event.setCanceled(true);
    }

    private int findHotbarSlot(int keyCode) {
        if (keyCode == Keyboard.KEY_NONE) {
            return -1;
        }

        for (int slot = 0; slot < HOTBAR_SIZE; slot++) {
            if (hotbarKeys[slot].getKeyCode() == keyCode) {
                return slot;
            }
        }

        return -1;
    }

    private void moveHoveredSlot(GuiContainer container, int hotbarSlot) {
        Minecraft minecraft = Minecraft.getMinecraft();
        if (minecraft.thePlayer == null || minecraft.playerController == null
            || minecraft.thePlayer.inventory.getItemStack() != null) {
            return;
        }

        Slot hoveredSlot = container.getSlotUnderMouse();
        if (hoveredSlot != null) {
            invokeHotbarSwap(container, hoveredSlot, hotbarSlot);
        }
    }

    private void invokeHotbarSwap(GuiContainer container, Slot hoveredSlot, int hotbarSlot) {
        try {
            HANDLE_MOUSE_CLICK.invoke(
                container,
                hoveredSlot,
                hoveredSlot.slotNumber,
                hotbarSlot,
                HOTBAR_SWAP_CLICK_TYPE
            );
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Could not access the container click handler", e);
        } catch (InvocationTargetException e) {
            throw new IllegalStateException("Container click handler failed", e.getCause());
        }
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
