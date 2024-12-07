package me.xkyrell.kstreasureloot.loot.impl;

import lombok.*;
import me.xkyrell.kstreasureloot.loot.*;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class SimpleLoot implements Loot {

    @NonNull
    private final String name;
    @Nullable
    private World world;
    @Nullable
    private Biome biome;
    @NonNull
    private List<LootItem> items;

    public SimpleLoot(@NonNull String name) {
        this(name, null, null, new ArrayList<>());
    }
}
