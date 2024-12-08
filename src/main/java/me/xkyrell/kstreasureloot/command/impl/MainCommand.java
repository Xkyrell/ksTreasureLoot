package me.xkyrell.kstreasureloot.command.impl;

import lombok.Getter;
import me.xkyrell.kstreasureloot.command.*;
import me.xkyrell.kstreasureloot.config.*;
import me.xkyrell.kstreasureloot.loot.service.LootService;
import net.kyori.adventure.text.Component;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainCommand extends AbstractCommand {

    @Getter
    private final Set<AbstractSubCommand> subCommands = new HashSet<>();
    private final LootService lootService;
    private final GeneralConfig general;
    private final LanguageConfig language;

    public MainCommand(LootService lootService, GeneralConfig general, LanguageConfig language) {
        super("kstreasureloot", List.of("treasureloot", "ksloot", "loot"));

        this.lootService = lootService;
        this.general = general;
        this.language = language;

        setUnknownSubCommand(sender -> {
            Component unknownSubCommand = language.getPrefixedMsg("subcommand-not-exist");
            sender.sendMessage(unknownSubCommand.replaceText(builder -> {
                builder.matchLiteral("{label}").replacement(getUsage());
            }));
        });

        addAvailability(
                sender -> !sender.hasPermission("kstreasureloot.admin"),
                language.getPrefixedMsg("no-permission")
        );

        setUnknownArgExecuting(sender -> {
            sender.sendMessage(language.getPrefixedMsg("not-enough-args"));
        });

        setupSubCommands();
    }

    private void setupSubCommands() {
        subCommands.add(new AddItemSubCommand(lootService, general, language));
        subCommands.add(new CreateSubCommand(lootService, general, language));
        subCommands.add(new RemoveItemSubCommand(lootService, general, language));
        subCommands.add(new RemoveSubCommand(lootService, general, language));
        subCommands.add(new ReloadSubCommand(general, language));
    }
}
