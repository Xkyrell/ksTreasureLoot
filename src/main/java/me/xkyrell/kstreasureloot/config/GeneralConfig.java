package me.xkyrell.kstreasureloot.config;

import lombok.NonNull;
import me.xkyrell.kstreasureloot.loot.*;
import me.xkyrell.kstreasureloot.loot.impl.SimpleLoot;
import me.xkyrell.kstreasureloot.loot.service.LootService;
import me.xkyrell.kstreasureloot.util.Biomes;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GeneralConfig extends Config {

    private final Plugin plugin;
    private final LootService lootService;

    public GeneralConfig(Plugin plugin, LootService lootService) {
        super(plugin, "config");

        this.plugin = plugin;
        this.lootService = lootService;
        updateOrLoad();
    }

    @Override
    public void reload() {
        super.reload();
        updateOrLoad();
    }

    private void updateOrLoad() {
        ConfigurationSection customLootSection = getSource().getConfigurationSection("custom-loot");
        if (customLootSection == null) {
            return;
        }

        lootService.unregisterAll();
        for (String key : customLootSection.getKeys(false)) {
            ConfigurationSection lootSection = customLootSection.getConfigurationSection(key);
            if (lootSection == null) {
                continue;
            }

            World world = loadWorld(lootSection.getString("world"));
            Biome biome = Biomes.loadBiome(lootSection.getString("biome"));

            List<LootItem> lootItems = loadItems(lootSection.getConfigurationSection("items"));

            lootService.register(new SimpleLoot(key, world, biome, lootItems));
        }
    }

    private World loadWorld(@NonNull String worldName) {
        World world = plugin.getServer().getWorld(worldName);
        if (world == null) {
            throw new IllegalArgumentException("World not found: " + worldName);
        }
        return world;
    }

    private List<LootItem> loadItems(ConfigurationSection itemsSection) {
        if (itemsSection == null) {
            return Collections.emptyList();
        }

        return itemsSection.getMapList("").stream()
                .map(itemMap -> {
                    ConfigurationSection itemSection = (ConfigurationSection) itemMap.get("item");
                    if (itemSection == null) {
                        return null;
                    }
                    ItemStack itemStack = ItemStack.deserialize(itemSection.getValues(false));
                    double probability = (double) itemMap.get("probability");

                    return new LootItem(itemStack, probability);
                })
                .filter(Objects::nonNull)
                .toList();
    }

    public void saveLoot(Loot loot) {
        ConfigurationSection customLootSection = getSource().getConfigurationSection("custom-loot");
        if (customLootSection == null) {
            customLootSection = getSource().createSection("custom-loot");
        }

        ConfigurationSection lootSection = customLootSection.createSection(loot.getName());
        lootSection.set("world", loot.getWorld().getName());
        lootSection.set("biome", loot.getBiome().name());

        List<Map<String, Object>> serializedItems = loot.getItems().stream()
                .map(lootItem -> Map.of(
                        "item", lootItem.itemStack().serialize(),
                        "probability", lootItem.probability()
                ))
                .toList();

        lootSection.set("items", serializedItems);
        save();
    }

    public void removeLoot(String lootName) {
        ConfigurationSection customLootSection = getSource().getConfigurationSection("custom-loot");
        if (customLootSection == null || !customLootSection.contains(lootName)) {
            throw new IllegalArgumentException("Loot with name '" + lootName + "' does not exist!");
        }

        customLootSection.set(lootName, null);
        save();
    }
}
