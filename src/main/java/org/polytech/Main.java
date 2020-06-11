package org.polytech;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.polytech.controller.FarmFieldsController;
import org.polytech.controller.ShopWindowController;
import org.polytech.controller.StorageWindowController;
import org.polytech.model.PlantType;
import org.polytech.model.Player;
import org.polytech.model.SeedType;

import java.io.IOException;
import java.util.Arrays;

public class Main extends Application {
    private Stage primaryStage;
    private AnchorPane farmField;

    private Player player = new Player(500);
    private SeedType currSeedType;

    private ObservableList<SeedType> allAvailableSeeds = FXCollections.observableArrayList();

    public Main() {
        player.addVegetable(SeedType.APPLE, 2);
        player.addVegetable(PlantType.STRAWBERRY, 2);
        player.addVegetable(SeedType.WHEAT, 1);
        player.addVegetable(SeedType.STRAWBERRY, 4);

        allAvailableSeeds.addAll(Arrays.asList(SeedType.values()));
    }

    public static void main(String[] args) { launch(args); }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("My Farm");

        showFarmFields();
    }

    public void showFarmFields() {
        try {
            System.out.println("выдал " + getClass().getResource("/view/FarmFields.fxml"));
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FarmFields.fxml"));
            farmField = loader.load();

            FarmFieldsController controller = loader.getController();
            controller.setMainApp(this);

            Scene scene = new Scene(farmField);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean showStorage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/StorageWindow.fxml"));
            AnchorPane storage = loader.load();    // Создаём диалоговое окно Stage.
            Stage storageStage = new Stage();
            storageStage.setTitle("Склад");
            storageStage.initModality(Modality.WINDOW_MODAL);
            storageStage.initOwner(primaryStage);
            Scene scene = new Scene(storage);
            storageStage.setScene(scene);

            StorageWindowController controller = loader.getController();
            controller.setStoreStage(storageStage);
            controller.setMainApp(this);
            storageStage.showAndWait();

            return controller.isPlantClicked();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void showShop() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ShopWindow.fxml"));
            AnchorPane storage = loader.load();    // Создаём диалоговое окно Stage.
            Stage shopStage = new Stage();
            shopStage.setTitle("Магазин");
            shopStage.initModality(Modality.WINDOW_MODAL);
            shopStage.initOwner(primaryStage);
            Scene scene = new Scene(storage);
            shopStage.setScene(scene);

            ShopWindowController controller = loader.getController();
            controller.setShopStage(shopStage);
            controller.setMainApp(this);
            shopStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setCurrentSeedType(SeedType seedType) {
        currSeedType = seedType;
    }

    public SeedType getCurrentSeedType() {
        return currSeedType;
    }

    public ObservableList<SeedType> getStorageSeeds() {
        return player.getSeedsInStorage();
    }

    public ObservableList<PlantType> getStoragePlants() { return player.getPlantsInStorage(); }

    public ObservableList<SeedType> getAllAvailableSeeds() { return allAvailableSeeds; }

}
