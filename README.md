# Hotbar Rebinder

A client-side Forge 1.8.9 mod that adds a second configurable keybind for each hotbar slot.

## Usage

Assign the nine additional binds under **Options > Controls > Hotbar Rebind**. A bind can then:

- select its hotbar slot while playing;
- swap a hovered inventory item with its hotbar slot while a container is open.

Keyboard keys and mouse buttons are supported. As with vanilla number-key swaps, the inventory action only works when
the cursor is not carrying an item.

The mod uses Sponge Mixins for game-tick, keyboard, and mouse handling. A Mixin invoker calls each container screen's
own vanilla click handler. Mixin is bundled in the mod JAR, so no additional dependency is required.

## Build

Build with Java 8 by running `gradlew build`. The reobfuscated mod JAR is written to `build/libs`.

## Run in IntelliJ IDEA

Generate the IDEA project with Java 8 by running `gradlew idea`, then use the `Minecraft Client` application
configuration. Its important settings are:

- JRE: Java 8
- Main class: `GradleStart`
- Program arguments: `--tweakClass com.tfourj.hotbarrebind.launch.HotbarRebindTweaker`
- Working directory: the project's `run` directory

If IDEA kept an older duplicate `Minecraft Client` configuration, delete it or add the program arguments above. A
working launch prints `Loading tweak class name com.tfourj.hotbarrebind.launch.HotbarRebindTweaker` in the console.
