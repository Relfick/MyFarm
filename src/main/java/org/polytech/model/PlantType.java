package org.polytech.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public enum PlantType implements Vegetative {
    WHEAT(120, "Пшеница"),
    STRAWBERRY(250, "Клубника"),
    APPLE(370, "Яблоко");

    private final StringProperty name;
    private final IntegerProperty salePrice;
    private final IntegerProperty count;

    PlantType(int salePrice, String name) {
        this.name = new SimpleStringProperty(name);
        this.salePrice = new SimpleIntegerProperty(salePrice);
        this.count = new SimpleIntegerProperty(0);
    }

    public PlantType getPlantType() { return this; }

    @Override
    public String getName() { return this.name.get(); }

    public void setName(String name) { this.name.set(name); }

    public StringProperty nameProperty() { return this.name; }

    @Override
    public int getSalePrice() { return this.salePrice.get(); }

    public void setSalePrice(int salePrice) { this.salePrice.set(salePrice); }

    public IntegerProperty salePriceProperty() { return this.salePrice; }

    @Override
    public int getCount() { return this.count.get(); }

    @Override
    public void setCount(int count) { this.count.set(count); }

    public IntegerProperty countProperty() { return this.count; }

    @Override
    public String getAdditionalImagePath() {
        return "src/main/java/org/polytech/images/" + this.toString().toLowerCase() + "_plant_add.jpg";
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
