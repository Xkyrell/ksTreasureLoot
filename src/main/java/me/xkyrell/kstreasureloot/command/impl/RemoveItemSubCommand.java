package me.xkyrell.kstreasureloot.command.impl;

import me.xkyrell.kstreasureloot.command.AbstractSubCommand;
import me.xkyrell.kstreasureloot.config.GeneralConfig;
import me.xkyrell.kstreasureloot.config.LanguageConfig;
import me.xkyrell.kstreasureloot.loot.Loot;
import me.xkyrell.kstreasureloot.loot.LootItem;
import me.xkyrell.kstreasureloot.loot.service.LootService;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.Optional;

public class RemoveItemSubCommand extends AbstractSubCommand {

    private final LootService lootService;
    private final GeneralConfig general;
    private final LanguageConfig language;

    RemoveItemSubCommand(LootService lootService, GeneralConfig general, LanguageConfig language) {
        super("removeitem", 2);

        this.lootService = lootService;
        this.general = general;
        this.language = language;

        addAvailability(
                sender -> !(sender instanceof Player),
                language.getPrefixedMsg("only-player")
        );
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        Optional<Loot> lootOptional = lootService.getResolver().resolve(args[1]);
        if (lootOptional.isEmpty()) {
            sender.sendMessage(language.getPrefixedMsg("loot-not-found"));
            return true;
        }

        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (itemStack.getType().isAir()) {
            sender.sendMessage(language.getPrefixedMsg("loot-item-air"));
            return true;
        }

        Loot loot = lootOptional.get();
        List<LootItem> matchingItems = loot.getItems().stream()
                .filter(item -> item.itemStack().equals(itemStack))
                .toList();

        loot.getItems().removeAll(matchingItems);
        general.removeLoot(loot.getName());
        general.saveLoot(loot);

        sender.sendMessage(language.getPrefixedMsg("remove-loot-item"));
        return false;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) throws IllegalArgumentException {
        return TabCompleter.create()
                .from(1)
                .supply(getLoot())
                .toSuggestions(args);
    }

    private List<String> getLoot() {
        return lootService.getResolver().getLoot().stream()
                .map(Loot::getName)
                .toList();
    }

    @Override
    public @NotNull String getUsage() {
        return "removeitem <loot-name>";
    }
}
