package me.xkyrell.kstreasureloot.command.impl;

import me.xkyrell.kstreasureloot.command.AbstractSubCommand;
import me.xkyrell.kstreasureloot.config.*;
import me.xkyrell.kstreasureloot.loot.Loot;
import me.xkyrell.kstreasureloot.loot.impl.SimpleLoot;
import me.xkyrell.kstreasureloot.loot.service.LootService;
import me.xkyrell.kstreasureloot.util.Biomes;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CreateSubCommand extends AbstractSubCommand {

    private final LootService lootService;
    private final GeneralConfig general;
    private final LanguageConfig language;

    CreateSubCommand(LootService lootService, GeneralConfig general, LanguageConfig language) {
        super("create", 2);

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
        if (lootOptional.isPresent()) {
            player.sendMessage(language.getPrefixedMsg("loot-already-exists"));
            return true;
        }

        Loot loot = new SimpleLoot(args[1], player);
        if (setupLootOrDefault(loot, player, args)) {
            return true;
        }

        lootService.register(loot);
        general.saveLoot(loot);

        player.sendMessage(language.getPrefixedMsg("loot-create"));
        return false;
    }

    private boolean setupLootOrDefault(Loot loot, Player player, String[] args) {
        if (args.length < 4) {
            return false;
        }

        World world = player.getServer().getWorld(args[2]);
        if (world == null) {
            player.sendMessage(language.getPrefixedMsg("unknown-world"));
            return true;
        }

        loot.setWorld(world);
        Optional<Biome> biomeOptional = Biomes.safeLoadBiome(args[3]);
        if (biomeOptional.isEmpty()) {
            player.sendMessage(language.getPrefixedMsg("unknown-biome"));
            return true;
        }

        loot.setBiome(biomeOptional.get());
        return false;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) throws IllegalArgumentException {
        return TabCompleter.create()
                .from(1)
                .supply(() -> generateLootName(sender))
                .supply(getWorldsByServer(sender.getServer()))
                .supply(getBiomes())
                .toSuggestions(args);
    }

    private List<String> generateLootName(CommandSender sender) {
        Player player = (Player) sender;
        Biome biome = player.getLocation().getBlock().getBiome();
        String suggestion = biome.name().toLowerCase().concat("_template");

        Set<String> existingNames = lootService.getResolver().getLoot().stream()
                .map(Loot::getName)
                .collect(Collectors.toSet());

        int nextIndex = IntStream.range(1, Integer.MAX_VALUE)
                .filter(i -> existingNames.stream()
                        .noneMatch(name -> name.equals(suggestion + i)))
                .findFirst()
                .orElse(1);

        return Collections.singletonList(suggestion + nextIndex);
    }

    private List<String> getWorldsByServer(Server server) {
        return server.getWorlds().stream()
                .map(World::getName)
                .toList();
    }

    private List<String> getBiomes() {
        return Arrays.stream(Biome.values())
                .map(Enum::name)
                .toList();
    }

    @Override
    public @NotNull String getUsage() {
        return "create <loot-name> [world] [biome]";
    }
}
