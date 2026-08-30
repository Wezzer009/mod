# Quartz Climb — Minecraft 26.2 / Fabric

Prototype wall-climbing system based on the supplied video.

## Current behavior
- Press **V** while looking at a vertical solid surface to grab it.
- While attached, **W/S** moves up/down and **A/D** moves sideways along the wall.
- **Space** or **Shift** releases the wall.
- The controller alternates a `leftHand` / `rightHand` grip state every 12 ticks; this is the state intended for the visible hand animation layer.
- The controller is designed so the hand renderer can be added without changing the climbing logic.

## Target toolchain
- Minecraft 26.2
- Fabric Loader 0.19.4
- Fabric API 0.158.0+26.2
- Fabric Loom 1.17
- Java 25

## Build on Windows
1. Install JDK 25.
2. Open this project in IntelliJ IDEA 2025.3+ or VS Code with the Java/Fabric tooling.
3. Run `gradlew.bat build`.
4. Take the shortest JAR from `build/libs/` and put it in `.minecraft/mods`.

Fabric's 26.2 documentation recommends Loom 1.17 and Gradle 9.5.1, and the current Fabric docs require JDK 25 for this development line.
