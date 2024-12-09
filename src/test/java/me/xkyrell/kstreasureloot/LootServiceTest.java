package me.xkyrell.kstreasureloot;

import me.xkyrell.kstreasureloot.loot.Loot;
import me.xkyrell.kstreasureloot.loot.impl.SimpleLoot;
import me.xkyrell.kstreasureloot.loot.service.LootResolver;
import me.xkyrell.kstreasureloot.loot.service.LootService;
import me.xkyrell.kstreasureloot.loot.service.impl.SimpleLootService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class LootServiceTest {

    private static final String KEY = "test";

    private LootService lootService;
    private Loot loot;

    @BeforeEach
    void setUp() {
        lootService = new SimpleLootService();
        loot = new SimpleLoot(KEY, null, null, Collections.emptyList());
    }

    @Test
    void testRegister() {
        lootService.register(loot);

        LootResolver resolver = lootService.getResolver();
        Optional<Loot> resolvedDynamite = resolver.resolve(KEY);

        assertTrue(resolvedDynamite.isPresent());
        assertEquals(loot, resolvedDynamite.get());
        assertEquals(1, resolver.getLoot().size());
    }

    @Test
    void testUnregister() {
        lootService.register(loot);
        lootService.unregister(KEY);

        LootResolver resolver = lootService.getResolver();
        Optional<Loot> resolvedDynamite = resolver.resolve(KEY);

        assertFalse(resolvedDynamite.isPresent());
    }

    @Test
    void testGetResolver() {
        LootResolver resolver = lootService.getResolver();

        assertNotNull(resolver);
        assertEquals(0, resolver.getLoot().size());
    }
}
