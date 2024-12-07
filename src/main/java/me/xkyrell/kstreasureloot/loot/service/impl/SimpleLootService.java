package me.xkyrell.kstreasureloot.loot.service.impl;

import lombok.RequiredArgsConstructor;
import me.xkyrell.kstreasureloot.loot.Loot;
import me.xkyrell.kstreasureloot.loot.service.LootResolver;
import me.xkyrell.kstreasureloot.loot.service.LootService;
import java.util.*;

public class SimpleLootService implements LootService {

    private final Map<String, Loot> lootByName = new HashMap<>();

    @Override
    public void register(Loot loot) {
        lootByName.put(loot.getName(), loot);
    }

    @Override
    public void unregisterAll() {
        lootByName.clear();
    }

    @Override
    public void unregister(String name) {
        lootByName.remove(name);
    }

    @Override
    public LootResolver getResolver() {
        return new SimpleResolver(lootByName);
    }

    @RequiredArgsConstructor
    private static class SimpleResolver implements LootResolver {

        private final Map<String, Loot> lootByName;

        @Override
        public Optional<Loot> resolve(String name) {
            return Optional.ofNullable(lootByName.get(name));
        }

        @Override
        public Collection<Loot> getLoot() {
            return lootByName.values();
        }
    }
}
