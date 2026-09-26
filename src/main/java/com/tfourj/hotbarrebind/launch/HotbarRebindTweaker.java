package com.tfourj.hotbarrebind.launch;

import java.io.File;
import java.net.URISyntaxException;
import java.security.CodeSource;
import java.util.List;
import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.LaunchClassLoader;
import net.minecraftforge.fml.relauncher.CoreModManager;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

/**
 * Starts Sponge Mixin and registers this mod's configuration explicitly.
 *
 * <p>An IDEA development launch runs compiled classes instead of the packaged
 * mod JAR, so it cannot discover {@code MixinConfigs} from the JAR manifest.
 * Keeping the registration here makes development and packaged launches use
 * the same bootstrap path.</p>
 */
public final class HotbarRebindTweaker implements ITweaker {
    @Override
    public void acceptOptions(List<String> args, File gameDir, File assetsDir, String profile) {
    }

    @Override
    public void injectIntoClassLoader(LaunchClassLoader classLoader) {
        MixinBootstrap.init();
        Mixins.addConfiguration("mixins.hotbarrebind.json");
        MixinEnvironment.getDefaultEnvironment().setObfuscationContext("searge");
        MixinEnvironment.getDefaultEnvironment().setSide(MixinEnvironment.Side.CLIENT);

        CodeSource codeSource = getClass().getProtectionDomain().getCodeSource();
        if (codeSource == null) {
            throw new IllegalStateException("Cannot determine the Hotbar Rebind mod location");
        }

        try {
            File file = new File(codeSource.getLocation().toURI());
            if (file.isFile()) {
                CoreModManager.getIgnoredMods().remove(file.getName());
            }
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Invalid Hotbar Rebind mod location", e);
        }
    }

    @Override
    public String getLaunchTarget() {
        return "net.minecraft.client.main.Main";
    }

    @Override
    public String[] getLaunchArguments() {
        return new String[0];
    }
}
