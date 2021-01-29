package org.polytech;

import javafx.util.Pair;
import org.junit.jupiter.api.Test;

import org.polytech.model.Field;
import org.polytech.model.Player;
import org.polytech.model.SeedType;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    private Player player = new Player(300);
    private Field field = new Field();

    @Test
    void changeBalance() {
        player.addBalance(200);
        assertEquals(500, player.getBalance());
        player.reduceBalance(300);
        assertEquals(200, player.getBalance());
    }

    @Test
    void changeVegetables() {
        field.addVegetable(SeedType.APPLE, 3);
        field.addVegetable(SeedType.WHEAT, 2);
        assertEquals(field.getCountOfSeed(SeedType.APPLE), 3);
        assertEquals(field.getCountOfSeed(SeedType.WHEAT), 2);
        field.deleteVegetable(SeedType.APPLE, 1);
        field.deleteVegetable(SeedType.WHEAT, 1);
        assertEquals(field.getCountOfSeed(SeedType.APPLE), 2);
        assertEquals(field.getCountOfSeed(SeedType.WHEAT), 1);
    }

    @Test
    void planting() {
        Pair<Integer, Integer> wheatCoords = new Pair<>(2, 1);
        Pair<Integer, Integer> strawberryCoords = new Pair<>(0, 0);

        field.addVegetable(SeedType.STRAWBERRY, 1);
        field.addVegetable(SeedType.WHEAT, 1);

        field.plantSeed(strawberryCoords, SeedType.STRAWBERRY);
        assertTrue(field.isAlreadyPlanted(strawberryCoords));

        assertFalse(field.isAlreadyPlanted(wheatCoords));
        field.plantSeed(wheatCoords, SeedType.WHEAT);

        assertEquals(SeedType.STRAWBERRY, field.getHarvest(strawberryCoords));
        assertEquals(SeedType.WHEAT, field.getHarvest(wheatCoords));

        field.setGrown(strawberryCoords);

        assertEquals(true, field.hasGrown(strawberryCoords));
        assertEquals(false, field.hasGrown(wheatCoords));
    }


}