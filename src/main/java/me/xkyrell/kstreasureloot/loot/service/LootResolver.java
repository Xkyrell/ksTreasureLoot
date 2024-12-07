package me.xkyrell.kstreasureloot.loot.service;

import me.xkyrell.kstreasureloot.loot.Loot;

import java.util.Collection;
import java.util.Optional;

public interface LootResolver {

    Optional<Loot> resolve(String name);

    Collection<Loot> getLoot();

}
