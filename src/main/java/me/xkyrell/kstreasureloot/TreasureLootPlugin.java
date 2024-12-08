package me.xkyrell.kstreasureloot;

import me.xkyrell.kstreasureloot.command.impl.MainCommand;
import me.xkyrell.kstreasureloot.config.GeneralConfig;
import me.xkyrell.kstreasureloot.config.LanguageConfig;
import me.xkyrell.kstreasureloot.loot.service.LootService;
import me.xkyrell.kstreasureloot.loot.service.impl.SimpleLootService;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public class TreasureLootPlugin extends JavaPlugin {

    private static final String[] LOGO = {
            " _  __ __ _____ ___ ___  __    __  _  _ ___ ___ _   __   __ _____       ",
            "| |/ /' _/_   _| _ \\ __|/  \\ /' _/| || | _ \\ __| | /__\\ /__\\_   _| ",
            "|   <`._`. | | | v / _|| /\\ |`._`.| \\/ | v / _|| || \\/ | \\/ || |    ",
            "|_|\\_\\___/ |_| |_|_\\___|_||_||___/ \\__/|_|_\\___|___\\__/ \\__/ |_| ",
            "",
            "                          Plugin by Xkyrell",
            ""
    };

    private MainCommand command;
    private LootService lootService;

    @Override
    public void onLoad() {
        lootService = new SimpleLootService();
    }

    @Override
    public void onEnable() {
        GeneralConfig general = new GeneralConfig(this, lootService);
        LanguageConfig language = new LanguageConfig(this);

        command = new MainCommand(lootService, general, language);
        getServer().getCommandMap().register(getName(), command);

        getServer().getPluginManager().registerEvents(
                new TreasureLootListener(lootService), this
        );

        for (String line : LOGO) {
            getLogger().info(line);
        }
    }

    @Override
    public void onDisable() {
        lootService.unregisterAll();
        HandlerList.unregisterAll(this);
        if (command != null) {
            command.unregister(this);
        }
    }
}