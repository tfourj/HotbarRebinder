# Hotbar Rebinder

A client-side Forge 1.8.9 mod that adds a second configurable keybind for each hotbar slot.

## Usage

Assign the nine additional binds under **Options > Controls > Hotbar Rebind**. A bind can then:

- select its hotbar slot while playing;
- swap a hovered inventory item with its hotbar slot while a container is open.

Keyboard keys and mouse buttons are supported. As with vanilla number-key swaps, the inventory action only works when
the cursor is not carrying an item.

## Build

Build with Java 8 by running `gradlew build`. The reobfuscated mod JAR is written to `build/libs`.
