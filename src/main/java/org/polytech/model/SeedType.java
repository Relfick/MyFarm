package org.polytech.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public enum SeedType implements Vegetative{
    WHEAT(100, 120, "Пшеница", 6),
    STRAWBERRY(200, 230, "Клубника", 10),
    APPLE(300, 350, "Яблоко", 16);

    private final String name;
    private final Integer salePrice;
    private final Integer purchasePrice;
    private Integer count;
    private final Integer growTime;

    SeedType(int purchasePrice, int salePrice, String name, int growTime) {
        this.name = name;
        this.salePrice = salePrice;
        this.purchasePrice = purchasePrice;
        this.count = 0;
        this.growTime = growTime;
    }


    public SeedType getSeedType() { return this; }

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

    public int getPurchasePrice() { return this.purchasePrice; }

    public IntegerProperty purchasePriceProperty() {
        IntegerProperty purchasePriceProp = new SimpleIntegerProperty(purchasePrice);
        return purchasePriceProp;
    }



    /* Картинка для отображения выбранного семени на складе */
    @Override
    public String getAdditionalImagePath() {
        return "src/main/resources/images/" + this.toString().toLowerCase() + "_seed_add.jpg";
    }

    public int getGrowTime() {
        return growTime;
    }

    public IntegerProperty growTimeProperty() {
        IntegerProperty growTimeProp = new SimpleIntegerProperty(growTime);
        return growTimeProp;
    }


    @Override
    public void delete(int amount) {
        int intCount = getCount();
        if (intCount > amount - 1) {
            setCount(intCount - amount);
        } else
            throw new IllegalArgumentException("Удаление невозможно, у вас недостаточно образцов");
    }

    // Картинки для отображения роста
    public String getSeedImagePath(int phase) {
        return "src/main/resources/images/" + this.toString().toLowerCase() + "_seed_" + phase + ".jpg";
    }
}
