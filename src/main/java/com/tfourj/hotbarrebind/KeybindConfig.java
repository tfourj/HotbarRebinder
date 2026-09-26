package com.tfourj.hotbarrebind;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

final class KeybindConfig {
    static final String FILE_NAME = "hotbarrebinder.cfg";

    private static final String CATEGORY = "keybinds";
    private static final String LEGACY_OPTION_PREFIX = "key_key.hotbarrebind.slot.";

    private final Configuration configuration;
    private final Property[] properties;
    private final int[] keyCodes;

    private KeybindConfig(Configuration configuration, Property[] properties, int[] keyCodes) {
        this.configuration = configuration;
        this.properties = properties;
        this.keyCodes = keyCodes;
    }

    static KeybindConfig load(File configDirectory, File gameDirectory, int hotbarSize, Logger logger) {
        int[] legacyKeyCodes = readLegacyKeyCodes(new File(gameDirectory, "options.txt"), hotbarSize, logger);
        Configuration configuration = new Configuration(new File(configDirectory, FILE_NAME));
        Property[] properties = new Property[hotbarSize];
        int[] keyCodes = new int[hotbarSize];

        configuration.load();
        for (int slot = 0; slot < hotbarSize; slot++) {
            properties[slot] = configuration.get(
                CATEGORY,
                "slot_" + (slot + 1),
                legacyKeyCodes[slot],
                "Key code for additional hotbar slot " + (slot + 1) + "."
            );
            keyCodes[slot] = properties[slot].getInt(legacyKeyCodes[slot]);
        }

        if (configuration.hasChanged()) {
            configuration.save();
        }

        return new KeybindConfig(configuration, properties, keyCodes);
    }

    int getKeyCode(int slot) {
        return keyCodes[slot];
    }

    void saveIfChanged(KeyBinding[] keyBindings) {
        boolean changed = false;
        for (int slot = 0; slot < properties.length; slot++) {
            int keyCode = keyBindings[slot].getKeyCode();
            if (properties[slot].getInt(Keyboard.KEY_NONE) != keyCode) {
                properties[slot].set(keyCode);
                keyCodes[slot] = keyCode;
                changed = true;
            }
        }

        if (changed) {
            configuration.save();
        }
    }

    private static int[] readLegacyKeyCodes(File optionsFile, int hotbarSize, Logger logger) {
        int[] keyCodes = new int[hotbarSize];
        if (!optionsFile.isFile()) {
            return keyCodes;
        }

        try (BufferedReader reader = new BufferedReader(
            new InputStreamReader(new FileInputStream(optionsFile), StandardCharsets.UTF_8)
        )) {
            String line;
            while ((line = reader.readLine()) != null) {
                for (int slot = 0; slot < hotbarSize; slot++) {
                    String prefix = LEGACY_OPTION_PREFIX + (slot + 1) + ":";
                    if (line.startsWith(prefix)) {
                        keyCodes[slot] = parseKeyCode(line.substring(prefix.length()));
                        break;
                    }
                }
            }
        } catch (IOException exception) {
            logger.warn("Could not migrate Hotbar Rebind keybinds from {}", optionsFile, exception);
        }

        return keyCodes;
    }

    private static int parseKeyCode(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ignored) {
            return Keyboard.KEY_NONE;
        }
    }
}
