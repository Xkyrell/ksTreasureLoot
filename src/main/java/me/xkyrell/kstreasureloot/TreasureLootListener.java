package me.xkyrell.kstreasureloot;

import lombok.RequiredArgsConstructor;
import me.xkyrell.kstreasureloot.loot.Loot;
import me.xkyrell.kstreasureloot.loot.LootItem;
import me.xkyrell.kstreasureloot.loot.service.LootService;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.LootGenerateEvent;
import org.bukkit.inventory.ItemStack;
import java.util.List;
import java.util.Random;

@RequiredArgsConstructor
public class TreasureLootListener implements Listener {

    private final LootService lootService;
    private final Random random = new Random();

    @EventHandler(priority = EventPriority.LOW)
    public void onGenerate(LootGenerateEvent event) {
        for (Loot loot : lootService.getResolver().getLoot()) {
            Location location = event.getLootContext().getLocation();
            if (!location.getWorld().equals(loot.getWorld())) {
                continue;
            }

            if (!location.getBlock().getBiome().equals(loot.getBiome())) {
                continue;
            }

            event.getLoot().addAll(generateCustomLoot(loot));
        }
    }

    private List<ItemStack> generateCustomLoot(Loot loot) {
        return loot.getItems().stream()
                .filter(lootItem -> random.nextDouble() < lootItem.probability())
                .map(LootItem::itemStack)
                .toList();
    }
}
