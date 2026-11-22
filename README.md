# Aristois for 26.3

**Unofficial** build of the EMC framework that runs **Aristois on Minecraft 26.3** (Fabric).

[![Download](https://img.shields.io/github/v/release/justbbenedev/aristois-for-26.3?label=download&style=for-the-badge)](https://github.com/justbbenedev/aristois-for-26.3/releases/latest)
![Minecraft](https://img.shields.io/badge/minecraft-26.3-3c8527?style=for-the-badge)
![Loader](https://img.shields.io/badge/loader-fabric-dbd0b4?style=for-the-badge)
![Java](https://img.shields.io/badge/java-25-orange?style=for-the-badge)

## Download

Grab the latest release from the **[Releases page](https://github.com/justbbenedev/aristois-for-26.3/releases/latest)**:

| File | Goes to |
|---|---|
| `EMC-F-v2-latest-26.3.jar` | `.minecraft/libraries/me/deftware/EMC-F-v2/latest-26.3/` |
| `26.3-Aristois.json` | `.minecraft/versions/26.3-Aristois/` |

`Aristois.jar` is not included. Get it from [aristois.net](https://aristois.net/download) and put it in `.minecraft/libraries/EMC/26.3/`.

## Install

1. Launch vanilla **26.3** once.
2. Copy the files above to their folders.
3. Restart your launcher, pick the **26.3-Aristois** version and play.
4. Open the Aristois menu in game with **Right Shift**.

## Requirements

- Minecraft **26.3**
- **Java 25**
- Graphics backend set to **OpenGL** (Options → Video Settings)

## Compatibility

- Works alongside other **Fabric mods**, resource packs and Fabric API.
- Optional extra mods folder: if `.minecraft/mods-aristois/` exists, this profile loads mods from there instead of `.minecraft/mods/`. Use it to keep a separate mod set for Aristois.
- Mods that replace the chunk renderer can conflict with render modules.

## Fixes over the original 26.3 port

- Crash when opening the menu: `NoSuchFieldError: int glId`.
- Crash when drawing menus: `Scissor size must be >0` / `Scissor ... is out of bounds`.
- Render modules leaving the world blank after being toggled (chunks are now rebuilt the same way as F3+A).
- Block lists applied correctly when chunks are built through the Fabric Renderer API (Indigo).
- `fabric.modsFolder` is respected when it is already set.

## Build from source

```bash
export JAVA_HOME=/path/to/jdk-25
./gradlew build
# build/libs/EMC-<version>.jar  ->  rename to EMC-F-v2-latest-26.3.jar
```

For personal use you can embed your own `Aristois.jar` into a single file:

```bash
./gradlew build -ParistoisJar=/path/to/Aristois.jar
# build/libs/EMC-<version>-aristois.jar
```

## Notes

- Clients like Aristois are banned on most servers. Use it in your own worlds.
- Not affiliated with Deftware. Aristois and EMC are made by [Deftware](https://gitlab.com/EMC-Framework/EMC). EMC is MIT licensed, see [LICENSE](LICENSE).
- Based on the 26.3 port by [memelet11](https://github.com/memelet11/aristois-26.3-minecraft-release).
