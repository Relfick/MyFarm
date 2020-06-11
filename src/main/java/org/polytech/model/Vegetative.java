package org.polytech.model;

public interface Vegetative {
    void delete(int amount);
    String getName();

    String getAdditionalImagePath();

    void setCount(int count);

    int getSalePrice();

    int getCount();
}
