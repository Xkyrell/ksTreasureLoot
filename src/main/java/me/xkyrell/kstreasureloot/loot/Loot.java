package me.xkyrell.kstreasureloot.loot;

import org.bukkit.World;
import org.bukkit.block.Biome;
import java.util.List;

public interface Loot {

    String getName();

    World getWorld();

    Biome getBiome();

    List<LootItem> getItems();

}
