package me.xkyrell.kstreasureloot.loot.service;

import me.xkyrell.kstreasureloot.loot.Loot;

public interface LootService {

    void register(Loot loot);

    void unregisterAll();

    void unregister(String name);

    LootResolver getResolver();

    default void unregister(Loot loot) {
        unregister(loot.getName());
    }
}
