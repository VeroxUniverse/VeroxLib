# VeroxLib

![Minecraft](https://img.shields.io/badge/Minecraft-1.21%2B-green)
![Loader](https://img.shields.io/badge/Loader-Fabric%20%7C%20NeoForge-blue)
![Architectury](https://img.shields.io/badge/Architectury-Powered-orange)
![License](https://img.shields.io/badge/License-MIT-lightgrey)
![API](https://img.shields.io/badge/API-Stable-success)

**VeroxLib** is a **Architectury-based** library for Minecraft mods that introduces a fully extensible **Sanity System**.

It provides developers with an easy-to-use API for managing player mental states, triggering madness effects, and integrating sanity-related gameplay mechanics across **Fabric** and **NeoForge**.

---

## Overview

VeroxLib allows mods to simulate psychological mechanics such as fear, corruption, hallucinations, or insanity without reinventing infrastructure.

## Features

### Global Sanity System

* Every player has a sanity value from `0.0` to `100.0`
* Sanity Loss in specific biome with the tag: `is_horror_biome`

### Modular Madness Effects

* Register effects triggered by sanity thresholds
* Client or server execution support

### Custom Attributes

VeroxLib automatically registers:

* `veroxlib:corruption`
* `veroxlib:sanity_resistance`
* `veroxlib:sanity_regen`

### Cross-Platform Support

Works seamlessly on:

* Fabric
* NeoForge

via Architectury.

### Cultist System

Built-in support for **Cultist players**:

* Immune to sanity effects
* Ideal for factions, classes, or lore systems

---

## Installation

### CurseMaven Setup

Add the CurseMaven repository to your `build.gradle`.

```gradle
repositories {
    maven {
        url "https://www.cursemaven.com"
        content {
            includeGroup "curse.maven"
        }
    }
}
```

Add the dependency:

```gradle
dependencies {

    modApi "curse.maven:PROJECT_ID:FILE_ID"

}
```

---

## Quick Start

### Reading Player Sanity

```java
float sanity = SanityAPI.getSanity(player);
```

### Modifying Sanity

```java
SanityAPI.modifySanity(player, -5.0f);
```

### Checking Cultist State

```java
if (SanityAPI.isCultist(player)) {
}
```

---

## Developer Guide

### Creating Custom Sanity Effects

Implement `ISanityEffect`.

Example: Door Creak hallucination.

```java
public class DoorCreakEffect implements ISanityEffect {

    @Override
    public float getThreshold() {
        return 40.0f;
    }

    @Override
    public void apply(Player player, float currentSanity) {

        if (player.getRandom().nextFloat() < 0.05f) {

            Vec3 pos = player.position().add(
                (player.getRandom().nextDouble() - 0.5) * 10,
                0,
                (player.getRandom().nextDouble() - 0.5) * 10
            );

            player.level().playLocalSound(
                pos.x,
                pos.y,
                pos.z,
                SoundEvents.WOODEN_DOOR_OPEN,
                SoundSource.AMBIENT,
                0.4f,
                0.5f + player.getRandom().nextFloat() * 0.3f,
                false
            );
        }
    }

    @Override
    public boolean isClientSide() {
        return true;
    }
}
```

---

### Adding Sanity Modifiers to Items

Items can influence sanity loss by implementing `ISanityModifier`.

```java
public class SanityCharmItem extends Item implements ISanityModifier {

    @Override
    public float getSanityResistance(ItemStack stack) {
        return 0.5f;
    }
}
```

---

## Attributes

| Attribute                    | Description                                      |
| ---------------------------- | ------------------------------------------------ |
| `veroxlib:corruption`        | Increases sanity loss speed                      |
| `veroxlib:sanity_resistance` | Reduces sanity changes (`1.0 = 100% resistance`) |

## Example Use Cases

* Horror mods
* Psychological survival gameplay
* Lovecraftian mechanics
* Magic corruption systems
* RPG classes & factions
* Hallucination systems

---

## Compatibility

| Loader   | Supported   |
| -------- |-------------|
| Fabric   | Yes         |
| NeoForge | Yes         |
| Forge    | not planned |
| Quilt    | Untested    |

## Version Matrix

| Minecraft | VeroxLib  |
|-----------| --------- |
| 1.21.1    | Supported |
| 1.21+     | Planned   |

---

## Contributing

Contributions are welcome.

Recommended workflow:

1. Fork repository
2. Create feature branch
3. Implement changes
4. Submit Pull Request

Please keep API stability in mind.

---

## License

Licensed under the **MIT License**.

You may freely:

* Use in mods
* Include in modpacks

Attribution appreciated but not required.

---

## Credits

Created for developers who want to build sanity mechanics without rebuilding core systems from scratch.

---

## Support

If you encounter issues:

* Open a GitHub Issue
* Provide logs
* Include loader + Minecraft version

