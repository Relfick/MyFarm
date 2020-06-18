package org.polytech.model;


public enum SeedType implements Vegetative{
    WHEAT(100, 120, "Пшеница", 6),
    STRAWBERRY(200, 230, "Клубника", 10),
    APPLE(300, 350, "Яблоко", 16);

    private String name;
    private Integer salePrice;
    private Integer purchasePrice;
    private Integer count;
    private Integer growTime;

    SeedType(int purchasePrice, int salePrice, String name, int growTime) {
        this.name = name;
        this.salePrice = salePrice;
        this.purchasePrice = purchasePrice;
        this.count = 0;
        this.growTime = growTime;
    }


    @Override
    public String getName() { return this.name; }

    @Override
    public int getSalePrice() { return this.salePrice; }

    @Override
    public int getCount() { return this.count; }

    @Override
    public void setCount(int count) { this.count = count; }

    public int getPurchasePrice() { return this.purchasePrice; }


    /* Картинка для отображения выбранного семени на складе */
    @Override
    public String getAdditionalImagePath() {
        return "src/main/resources/images/" + this.toString().toLowerCase() + "_seed_add.jpg";
    }

    public int getGrowTime() {
        return growTime;
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
