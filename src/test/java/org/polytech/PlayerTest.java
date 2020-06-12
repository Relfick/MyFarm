package org.polytech;

import javafx.util.Pair;
import org.junit.jupiter.api.Test;

import org.polytech.model.Player;
import org.polytech.model.SeedType;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    private Player player = new Player(300);

    @Test
    void changeBalance() {
        player.addBalance(200);
        assertEquals(500, player.getBalance());
        player.reduceBalance(300);
        assertEquals(200, player.getBalance());
    }

    @Test
    void changeVegetables() {
        player.addVegetable(SeedType.APPLE, 3);
        player.addVegetable(SeedType.WHEAT, 2);
        assertEquals(player.getCountOfSeed(SeedType.APPLE), 3);
        assertEquals(player.getCountOfSeed(SeedType.WHEAT), 2);
        player.deleteVegetable(SeedType.APPLE, 1);
        player.deleteVegetable(SeedType.WHEAT, 1);
        assertEquals(player.getCountOfSeed(SeedType.APPLE), 2);
        assertEquals(player.getCountOfSeed(SeedType.WHEAT), 1);
    }

    @Test
    void planting() {
        Pair<Integer, Integer> wheatCoords = new Pair<>(2, 1);
        Pair<Integer, Integer> strawberryCoords = new Pair<>(0, 0);

        player.addVegetable(SeedType.STRAWBERRY, 1);
        player.addVegetable(SeedType.WHEAT, 1);

        player.plantSeed(strawberryCoords, SeedType.STRAWBERRY);
        assertTrue(player.isAlreadyPlanted(strawberryCoords));

        assertFalse(player.isAlreadyPlanted(wheatCoords));
        player.plantSeed(wheatCoords, SeedType.WHEAT);

        assertEquals(SeedType.STRAWBERRY, player.getHarvest(strawberryCoords));
        assertEquals(SeedType.WHEAT, player.getHarvest(wheatCoords));

        player.setGrown(strawberryCoords);

        assertEquals(true, player.hasGrown(strawberryCoords));
        assertEquals(false, player.hasGrown(wheatCoords));
    }


}