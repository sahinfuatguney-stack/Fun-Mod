# Fun-Mod 2.0

**Minecraft Java 1.21.11 · Fabric · Java 21**

The original `26.2` branch is untouched. Work for this version is on `fun-mod-2.0-1.21.11`.

## Implemented in this prototype
- Separate **Fun-Mod 2.0** creative inventory tab
- Custom Lucky Block outcomes and a Rainbow Block
- Jewelry items: gold bracelet, necklace, four effect earrings, dollar and euro
- Decorative jewelry-case block and a mannequin item that places an armor stand
- Cash register + shop key: summon three customers; interact with them near the register to receive emerald, dollar or euro payments; they disappear after paying
- Reusable travel seals for **Netherstorm** and **Luck Realm**. Netherstorm travel triggers a thunderstorm.
- Summon items with crafting recipes for a named Iron Golem, named Warden boss and wandering trader
- Recipes, block/item models, Turkish and English names, custom pixel-art PNG textures, and a jewelry-shop chest loot table

## Still on the roadmap
The summoned mobs use vanilla models with names/effects; they do not yet have dedicated custom models or complete new AI. The display case is decorative for now rather than a storage GUI. The loot table exists, but randomly generated jewelry shops and storm-enhanced pillager camps are not yet placed by world generation. The dimensions currently use vanilla terrain generators and need their own custom terrain/biomes and structures.

## Download a built JAR
GitHub Actions builds this branch automatically. Open the repository's **Actions** tab, choose the latest `build-fun-mod` run, and download the artifact named `Fun-Mod-2.0-Minecraft-1.21.11`. The ZIP contains the JAR. Install the JAR in Minecraft's `mods` folder alongside Fabric API.
