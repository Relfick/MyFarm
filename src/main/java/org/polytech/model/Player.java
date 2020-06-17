package org.polytech.model;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Player {
    private int balance;

    public Player(int _balance) {
        balance = _balance;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int _balance) {
        balance = _balance;
    }

    public void addBalance(int amount) {
        balance += amount;
    }

    public boolean reduceBalance(int amount) {
        if (balance < amount) return false;
        else {
            balance -= amount;
            return true;
        }
    }

    public Image getAvatar() {
        Image img = null;
        try {
            img = new Image(new FileInputStream("src/main/resources/images/avatar.jpg"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return img;
    }
}
