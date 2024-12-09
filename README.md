# 🧰 ksTreasureLoot

A Minecraft plugin that allows adding various items including custom ones to the loot generation of vanilla dungeons

## Features
- 🗺️ **Custom Loot Generation**: Add unique items to the loot tables of vanilla structures.  
- 🌍 **World and Biome Support**: Configure loot generation for specific worlds and biomes.  
- 🎲 **Probability Control**: Set custom probabilities for items to appear in loot chests.  

---

### Commands

- **`/ksloot create <loot-name> [world] [biome]`**  
  Creates a new loot table with specified settings.  
  - `loot-name`: The name of the loot table.  
  - `world`: *(Optional)* The world where the loot applies.  
  - `biome`: *(Optional)* The biome where the loot applies.  
  *Permission*: `kstreasureloot.admin`

- **`/ksloot remove <loot-name>`**  
  Deletes the specified loot table.  
  - `loot-name`: The name of the loot table.  
  *Permission*: `kstreasureloot.admin`

- **`/ksloot additem <loot-name> <probability>`**  
  Adds the item held in the main hand to the specified loot table with a given probability.  
  - `loot-name`: The name of the loot table.  
  - `probability`: The chance for the item to appear in loot (e.g. `0.5` for 50%).  
  *Permission*: `kstreasureloot.admin`

- **`/ksloot removeitem <loot-name>`**  
  Removes the item held in the main hand from the specified loot table.  
  - `loot-name`: The name of the loot table.  
  *Permission*: `kstreasureloot.admin`

- **`/ksloot reload`**  
  Reloads the plugin configuration. 
  *Permission*: `kstreasureloot.admin`

---

## Customization
- Fully configurable loot tables.
- Support for custom items and probabilities.