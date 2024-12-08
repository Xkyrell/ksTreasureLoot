package me.xkyrell.kstreasureloot.util;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.bukkit.block.Biome;
import java.util.Arrays;
import java.util.Optional;

@UtilityClass
public class Biomes {

    public Optional<Biome> safeLoadBiome(@NonNull String biomeName) {
        return Arrays.stream(Biome.values())
                .filter(b -> b.name().equalsIgnoreCase(biomeName))
                .findFirst();
    }

    public Biome loadBiome(@NonNull String biomeName) {
        return safeLoadBiome(biomeName).orElseThrow(
                () -> new IllegalArgumentException("Invalid biome name: " + biomeName)
        );
    }
}
