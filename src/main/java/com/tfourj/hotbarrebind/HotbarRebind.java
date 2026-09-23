package com.tfourj.hotbarrebind;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
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
    private static final String KEY_CATEGORY = "key.categories.hotbarrebind";

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
        if (minecraft.thePlayer == null || minecraft.currentScreen != null) {
            return;
        }

        for (int slot = 0; slot < HOTBAR_SIZE; slot++) {
            if (hotbarKeys[slot].isPressed()) {
                minecraft.thePlayer.inventory.currentItem = slot;
                break;
            }
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
