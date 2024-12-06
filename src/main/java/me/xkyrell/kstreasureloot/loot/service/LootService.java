package me.xkyrell.kstreasureloot.loot.service;

import me.xkyrell.kstreasureloot.loot.Loot;

public interface LootService {

    void register(Loot loot);

    void unregister(String name);

    default void unregister(Loot loot) {
        unregister(loot.getName());
    }
}
