package org.polytech.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public enum SeedType implements Vegetative{
    WHEAT(100, 120, "Пшеница", 6),
    STRAWBERRY(200, 230, "Клубника", 10),
    APPLE(300, 350, "Яблоко", 16);

    private final StringProperty name;
    private final IntegerProperty salePrice;
    private final IntegerProperty purchasePrice;
    private final IntegerProperty count;
    private final IntegerProperty growTime;

    SeedType(int purchasePrice, int salePrice, String name, int growTime) {
        this.name = new SimpleStringProperty(name);
        this.salePrice = new SimpleIntegerProperty(salePrice);
        this.purchasePrice = new SimpleIntegerProperty(purchasePrice);
        this.count = new SimpleIntegerProperty(0);
        this.growTime = new SimpleIntegerProperty(growTime);
    }


    public SeedType getSeedType() { return this; }

    @Override
    public String getName() { return this.name.get(); }

    public void setName(String name) { this.name.set(name); }

    public StringProperty nameProperty() { return this.name; }

    @Override
    public int getSalePrice() { return this.salePrice.get(); }

    public void setSalePrice(int salePrice) { this.salePrice.set(salePrice); }

    public IntegerProperty salePriceProperty() { return this.salePrice; }

    public int getPurchasePrice() { return this.purchasePrice.get(); }

    public void setPurchasePrice(int purchasePrice) { this.purchasePrice.set(purchasePrice); }

    public IntegerProperty purchasePriceProperty() { return this.purchasePrice; }

    @Override
    public int getCount() { return this.count.get(); }
    @Override
    public void setCount(int count) { this.count.set(count); }

    public IntegerProperty countProperty() { return this.count; }

    /* Картинка для отображения выбранного семени на складе */
    @Override
    public String getAdditionalImagePath() {
        return "src/main/java/org/polytech/images/" + this.toString().toLowerCase() + "_seed_add.jpg";
    }

    public int getGrowTime() {
        return growTime.get();
    }

    public void setGrowTime(int time) { this.growTime.set(time); }

    public IntegerProperty growTimeProperty() { return this.growTime; }

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
        return "src/main/java/org/polytech/images/" + this.toString().toLowerCase() + "_seed_" + phase + ".jpg";
    }
}
