package org.polytech.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public enum PlantType implements Vegetative {
    WHEAT(120, "Пшеница"),
    STRAWBERRY(250, "Клубника"),
    APPLE(370, "Яблоко");

    private final String name;
    private final Integer salePrice;
    private Integer count;

    PlantType(int salePrice, String name) {
        this.name = name;
        this.salePrice = salePrice;
        this.count = 0;
    }

    public PlantType getPlantType() { return this; }


    @Override
    public String getName() { return this.name; }

    public StringProperty nameProperty() {
        StringProperty nameProp = new SimpleStringProperty(name);
        return nameProp;
    }

    @Override
    public int getSalePrice() { return this.salePrice; }

    public IntegerProperty salePriceProperty() {
        IntegerProperty salePriceProp = new SimpleIntegerProperty(salePrice);
        return salePriceProp; }

    @Override
    public int getCount() { return this.count; }

    @Override
    public void setCount(int count) { this.count = count; }

    public IntegerProperty countProperty() {
        IntegerProperty countProp = new SimpleIntegerProperty(count);
        return countProp;
    }

    @Override
    public String getAdditionalImagePath() {
        return "src/main/resources/images/" + this.toString().toLowerCase() + "_plant_add.jpg";
    }

    @Override
    public void delete(int amount) {
        int intCount = getCount();
        if (intCount > amount - 1) {
            setCount(intCount - amount);
        } else
            throw new IllegalArgumentException("Удаление невозможно, у вас недостаточно образцов");
    }
}
