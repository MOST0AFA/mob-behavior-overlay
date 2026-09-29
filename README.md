# Mob Behavior Overlay

Client-side Fabric mod for Minecraft 26.2 that puts a thin outline on nearby mobs:

| Color  | Meaning |
|--------|---------|
| Red    | Hostile (zombies, creepers…) or a neutral mob that is currently angry |
| Yellow | Neutral and calm (endermen, wild wolves, bees, iron golems…) |
| Green  | Friendly: passive animals/villagers, and anything you have tamed |

Taming a wolf/cat/parrot/horse flips it from yellow to green; angering a neutral mob flips it to red.
Only mobs within 24 blocks and in line of sight are outlined (no x‑ray through walls).

## Build
Requires JDK 25. `gradle build` → `build/libs/mob-behavior-overlay-*.jar` → drop in `mods/` with Fabric Loader + Fabric API.
