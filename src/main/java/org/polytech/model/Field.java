package org.polytech.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Pair;

import java.util.HashMap;

public class Field {

    /** первая пара - координаты посаженного семени
     * value второй пары указывает, выросло ли растение*/
    private HashMap<Pair<Integer, Integer>, Pair<SeedType, Boolean>> plantedSeeds = new HashMap<>();

    private ObservableList<SeedType> seedsInStorage = FXCollections.observableArrayList();
    private ObservableList<PlantType> plantsInStorage = FXCollections.observableArrayList();


    public ObservableList<SeedType> getSeedsInStorage() {
        return seedsInStorage;
    }
    public ObservableList<PlantType> getPlantsInStorage() {
        return plantsInStorage;
    }


    /** Кол-во семян данного типа */
    public int getCountOfSeed(SeedType seedType) {
        return seedType.getCount();
    }


    /** Увеличить count данного растительного
     * и добавить его в коллекцию -storage */
    public void addVegetable(Vegetative vegetative, int amount) {
        vegetative.setCount(vegetative.getCount() + amount);
        if (vegetative instanceof SeedType) {
            if (!seedsInStorage.contains(vegetative))
                seedsInStorage.add((SeedType) vegetative);
        }
        else if (vegetative instanceof PlantType) {
            if (!plantsInStorage.contains(vegetative))
                plantsInStorage.add((PlantType) vegetative);
        }
    }

    /** Уменьшает count данного растительного
     * и удаляет его из коллекции -storage*/
    public void deleteVegetable(Vegetative vegetative, int amount) {
        vegetative.delete(amount);
        if (vegetative.getCount() <= 0) {
            if (vegetative instanceof SeedType)
                seedsInStorage.remove(vegetative);
            else if (vegetative instanceof PlantType)
                plantsInStorage.remove(vegetative);
        }
    }

    /** Посадить семечко
     * добавляет семечко в коллекцию посаженных и уменьшает его count */
    public void plantSeed(Pair<Integer, Integer> coords, SeedType seedType) {
        plantedSeeds.put(coords, new Pair<>(seedType, false));

        deleteVegetable(seedType, 1);
    }

    /** Возвращает тип семени, которое выросло в данной ячейке coords */
    public SeedType getHarvest(Pair<Integer, Integer> coords) { return plantedSeeds.get(coords).getKey(); }

    public Boolean hasGrown(Pair<Integer, Integer> coords) {
        return plantedSeeds.containsKey(coords) ? plantedSeeds.get(coords).getValue() : false;
    }

    public void setGrown(Pair<Integer, Integer> coords) {
        SeedType seedType = plantedSeeds.get(coords).getKey();
        plantedSeeds.put(coords, new Pair<>(seedType, true));
    }

    public boolean isAlreadyPlanted(Pair<Integer, Integer> coords) {
        return this.plantedSeeds.containsKey(coords);
    }

    public void deleteGrownPlant(Pair<Integer, Integer> coords) {
        plantedSeeds.remove(coords);
    }
}
