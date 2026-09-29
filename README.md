# Mob Behavior Overlay

Client-side Fabric mod for Minecraft 26.2 that puts a thin outline on nearby mobs:

| Color  | Meaning |
|--------|---------|
| Red    | Hostile (zombies, creepers…) or a neutral mob that is currently angry |
| Yellow | Neutral and calm (endermen, wild wolves, bees, iron golems…) |
| Green  | Friendly: passive animals/villagers, and anything you have tamed |

Taming a wolf/cat/parrot/horse flips it from yellow to green; angering a neutral mob flips it to red.
By default only mobs within 24 blocks and in line of sight are outlined (no x-ray through walls).

## Build
Requires JDK 25 and Gradle 9.5.1+ (Loom 1.17). `gradle build` → `build/libs/mob-behavior-overlay-*.jar` → drop in `mods/` with Fabric Loader 0.19+ (Fabric API optional).

## Settings
Press **O** (rebindable under Controls → Mob Behavior Overlay) to open the settings screen:
- Overlay on/off
- Outline range slider (4–64 blocks)
- "Only visible mobs" (line of sight) toggle
- Hostile / Neutral / Friendly category toggles
- **Choose mobs…** – per-mob on/off list with All on / All off

Settings are saved to `config/mobbehavioroverlay.json`.

## Building in IntelliJ IDEA
1. Install **JDK 25** (File → Project Structure → SDKs → Add SDK → Download JDK → 25).
2. File → Open → select this folder → "Trust project". IntelliJ imports it as a Gradle project.
3. Settings → Build, Execution, Deployment → Build Tools → Gradle: set **Gradle JVM** to JDK 25.
4. Wait for the Gradle sync (first run downloads Minecraft, ~few minutes).
5. Open the Gradle tool window → Tasks → build → **build**. The jar is `build/libs/mob-behavior-overlay-1.0.0.jar` (not the `-sources` one).
6. To test in-game: Tasks → fabric → **runClient** (or run `./gradlew runClient`).
