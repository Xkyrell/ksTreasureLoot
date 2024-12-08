package me.xkyrell.kstreasureloot.command.impl;

import me.xkyrell.kstreasureloot.command.AbstractSubCommand;
import me.xkyrell.kstreasureloot.config.GeneralConfig;
import me.xkyrell.kstreasureloot.config.LanguageConfig;
import me.xkyrell.kstreasureloot.loot.Loot;
import me.xkyrell.kstreasureloot.loot.service.LootService;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.Optional;

public class RemoveSubCommand extends AbstractSubCommand {

    private final LootService lootService;
    private final GeneralConfig general;
    private final LanguageConfig language;

    RemoveSubCommand(LootService lootService, GeneralConfig general, LanguageConfig language) {
        super("remove", 2);

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
            player.sendMessage(language.getPrefixedMsg("loot-not-found"));
            return true;
        }

        Loot loot = lootOptional.get();
        lootService.unregister(loot);
        general.removeLoot(loot.getName());

        player.sendMessage(language.getPrefixedMsg("loot-remove"));
        return false;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) throws IllegalArgumentException {
        return TabCompleter.create()
                .from(1)
                .supply(lootService.getResolver().getLoot().stream()
                        .map(Loot::getName)
                        .toList()
                )
                .toSuggestions(args);
    }

    @Override
    public @NotNull String getUsage() {
        return "remove <loot-name>";
    }
}
