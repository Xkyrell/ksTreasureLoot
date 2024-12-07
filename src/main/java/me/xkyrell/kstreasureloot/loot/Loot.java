package me.xkyrell.kstreasureloot.loot;

import lombok.NonNull;
import org.bukkit.World;
import org.bukkit.block.Biome;
import java.util.List;

public interface Loot {

    String getName();

    World getWorld();

    void setWorld(@NonNull World world);

    Biome getBiome();

    void setBiome(@NonNull Biome biome);

    List<LootItem> getItems();

    void setItems(@NonNull List<LootItem> items);

}
