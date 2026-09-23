package com.tfourj.hotbarrebind.launch;

import java.io.File;
import java.util.List;
import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.spongepowered.asm.launch.MixinTweaker;
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
    private final MixinTweaker delegate;

    public HotbarRebindTweaker() {
        delegate = new MixinTweaker();
        Mixins.addConfiguration("mixins.hotbarrebind.json");
    }

    @Override
    public void acceptOptions(List<String> args, File gameDir, File assetsDir, String profile) {
        delegate.acceptOptions(args, gameDir, assetsDir, profile);
    }

    @Override
    public void injectIntoClassLoader(LaunchClassLoader classLoader) {
        delegate.injectIntoClassLoader(classLoader);
    }

    @Override
    public String getLaunchTarget() {
        return delegate.getLaunchTarget();
    }

    @Override
    public String[] getLaunchArguments() {
        return delegate.getLaunchArguments();
    }
}
