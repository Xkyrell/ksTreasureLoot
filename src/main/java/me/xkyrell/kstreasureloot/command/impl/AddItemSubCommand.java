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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class AddItemSubCommand extends AbstractSubCommand {

    private final LootService lootService;
    private final GeneralConfig general;
    private final LanguageConfig language;

    AddItemSubCommand(LootService lootService, GeneralConfig general, LanguageConfig language) {
        super("additem", 3);

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

        double probability = parseProbability(args[2]);
        if (probability <= 0) {
            sender.sendMessage(language.getPrefixedMsg("not-double"));
            return true;
        }

        Loot loot = lootOptional.get();
        LootItem item = new LootItem(itemStack, probability);
        loot.getItems().add(item);

        general.removeLoot(loot.getName());
        general.saveLoot(loot);

        sender.sendMessage(language.getPrefixedMsg("add-loot-item"));
        return false;
    }

    private double parseProbability(String probability) {
        try {
            return Double.parseDouble(probability);
        }
        catch (NumberFormatException e) {
            return 0;
        }
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) throws IllegalArgumentException {
        return TabCompleter.create()
                .from(1)
                .supply(getLoot())
                .supply(Collections.singletonList("0.50"))
                .toSuggestions(args);
    }

    private List<String> getLoot() {
        return lootService.getResolver().getLoot().stream()
                .map(Loot::getName)
                .toList();
    }

    @Override
    public @NotNull String getUsage() {
        return "additem <loot-name> <probability>";
    }
}
