package me.xkyrell.kstreasureloot.loot;

import org.bukkit.inventory.ItemStack;

public record LootItem(ItemStack itemStack, double probability) { }
