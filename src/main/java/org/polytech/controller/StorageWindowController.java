package org.polytech.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.polytech.Main;
import org.polytech.model.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class StorageWindowController {


    @FXML
    private TableView<SeedType> storageSeedsTable;
    @FXML
    private TableColumn<SeedType, String> seedsNameColumn;
    @FXML
    private TableColumn<SeedType, Number> seedsNumberColumn;
    @FXML
    private TableColumn<SeedType, Number> seedsSalePriceColumn;

    @FXML
    private TableView<PlantType> storagePlantsTable;
    @FXML
    private TableColumn<PlantType, String> plantsNameColumn;
    @FXML
    private TableColumn<PlantType, Number> plantsNumberColumn;
    @FXML
    private TableColumn<PlantType, Number> plantsSalePriceColumn;

    @FXML
    private Label balanceLabel;
    @FXML
    private Label currentSeedNameLabel;
    @FXML
    private ImageView currentImage;
    @FXML
    private Label numUnitsSelectedLabel;
    @FXML
    private Label totalProfitLabel;
    @FXML
    private Slider numUnitsSelectedSlider;
    @FXML
    private Button sellButton;
    @FXML
    private Button plantButton;

    private Player player;

    private Vegetative currentVegetativeType;

    // При открытии склада открывается страница семян
    private StoragePage currentStoragePage = StoragePage.SEEDS;

    private int numUnitsSelected = 0;

    private boolean plantClicked = false;

    private Main mainApp;

    private Stage storeStage;

    public StorageWindowController() {

    }

    @FXML
    private void initialize() {

        /* Заполняем таблицу семян */
        seedsNameColumn.setCellValueFactory(
                cellData -> cellData.getValue().nameProperty());
        seedsNumberColumn.setCellValueFactory(
                cellData -> cellData.getValue().countProperty());
        seedsSalePriceColumn.setCellValueFactory(
                cellData -> cellData.getValue().salePriceProperty());

        storageSeedsTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {

                    showVegetativeDetails(newValue);

                    currentVegetativeType = newValue;

                    numUnitsSelectedSlider.setDisable(false);
                    sellButton.setDisable(false);
                    plantButton.setDisable(false);
                    numUnitsSelectedSlider.adjustValue(0);
                    numUnitsSelected = 0;
                });

        /* Заполняем таблицу выросших растений */
        plantsNameColumn.setCellValueFactory(
                cellData -> cellData.getValue().nameProperty());
        plantsNumberColumn.setCellValueFactory(
                cellData -> cellData.getValue().countProperty());
        plantsSalePriceColumn.setCellValueFactory(
                cellData -> cellData.getValue().salePriceProperty());

        storagePlantsTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {

                    showVegetativeDetails(newValue);

                    currentVegetativeType = newValue;

                    numUnitsSelectedSlider.setDisable(false);
                    sellButton.setDisable(false);
                    plantButton.setDisable(true);
                    numUnitsSelectedSlider.adjustValue(0);
                    numUnitsSelected = 0;
                });
    }

    private void showVegetativeDetails(Vegetative vegetative) {
        if (vegetative != null) {
            currentSeedNameLabel.setText(vegetative.getName());
            try {
                currentImage.setImage(new Image(new FileInputStream(vegetative.getAdditionalImagePath())));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            currentSeedNameLabel.setText("Ничего не выбрано");
            currentImage.setImage(null);
            totalProfitLabel.setText("0");
            numUnitsSelectedLabel.setText("0");
        }
    }

    /** Показывает кол-во выбранных позиций и общую стоимость продажи */
    @FXML
    private void showNumUnitsSelected() {
        numUnitsSelected = (int) numUnitsSelectedSlider.getValue();
        int totalProfit = currentVegetativeType.getSalePrice() * numUnitsSelected;
        numUnitsSelectedLabel.setText(String.valueOf(numUnitsSelected));
        totalProfitLabel.setText(String.valueOf(totalProfit));
    }

    /** Продажа: Уменьшение количества данной позиции на складе и увеличение баланса */
    @FXML
    private void handleSell() {
        if (numUnitsSelected == 0 || numUnitsSelected > currentVegetativeType.getCount()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            //alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("Предупреждение");
            if (numUnitsSelected == 0) {
                alert.setHeaderText("Нельзя продать 0 шт.");
                alert.setContentText("Пожалуйста, выберите, сколько шт. желаете продать");
            } else {
                alert.setHeaderText(
                        "Нельзя продать " + numUnitsSelected
                                + " шт. Доступно лишь " + currentVegetativeType.getCount());
                alert.setContentText("Пожалуйста, выберите доступное количество.");
            }

            alert.showAndWait();
        } else {
            sell();
            balanceLabel.setText(String.valueOf(player.getBalance()));

            // Очистка всей доп информации в случае, когда склад становится пустым
            if ((currentStoragePage == StoragePage.SEEDS && storageSeedsTable.getItems().size() == 0) ||
                    currentStoragePage == StoragePage.PLANTS && storagePlantsTable.getItems().size() == 0) {
                disableMainElements();
            }

            //Установка слайдера и лейблов в стандартное положение
            numUnitsSelectedSlider.adjustValue(0);
            numUnitsSelected = 0;
            numUnitsSelectedLabel.setText("0");
            totalProfitLabel.setText("0");
        }
    }

    @FXML
    private void handlePlanting() {
        plantClicked = true;
        mainApp.setCurrentSeedType((SeedType) currentVegetativeType);
        storeStage.close();
    }

    @FXML
    private void handleOpenSeedsPage() {
        this.currentStoragePage = StoragePage.SEEDS;

        // Очищаем выбор в таблице растений
        if (currentVegetativeType instanceof PlantType) {
            storagePlantsTable.getSelectionModel().clearSelection();
            disableMainElements();
        }
    }

    @FXML
    private void handleOpenPlantsPage() {
        this.currentStoragePage = StoragePage.PLANTS;

        // Очищаем выбор в таблице семян
        if (currentVegetativeType instanceof SeedType) {
            storageSeedsTable.getSelectionModel().clearSelection();
            disableMainElements();
        }
    }

    private void sell() {
        player.addBalance(numUnitsSelected * currentVegetativeType.getSalePrice());
        player.deleteVegetable(currentVegetativeType, numUnitsSelected);
    }

    private void disableMainElements() {
        numUnitsSelectedSlider.setDisable(true);
        sellButton.setDisable(true);
        plantButton.setDisable(true);
    }

    public boolean isPlantClicked() {
        return plantClicked;
    }

    public void setMainApp(Main MainApp) {
        this.mainApp = MainApp;
        player = mainApp.getPlayer();
        storageSeedsTable.setItems(mainApp.getStorageSeeds());
        storagePlantsTable.setItems(mainApp.getStoragePlants());
        balanceLabel.setText(Integer.toString(player.getBalance()));
    }

    public void setStoreStage(Stage storageStage) { this.storeStage = storageStage; }


}
