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
    private TextField numUnitsSelectedTextField;
    @FXML
    private Button sellButton;
    @FXML
    private Button plantButton;

    private Player player;
    private Field field;
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

                    numUnitsSelectedTextField.setDisable(false);
                    sellButton.setDisable(false);
                    plantButton.setDisable(false);
                    numUnitsSelectedTextField.setText("");
                    totalProfitLabel.setText("0");
                    numUnitsSelectedLabel.setText("0");
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

                    numUnitsSelectedTextField.setDisable(false);
                    sellButton.setDisable(false);
                    plantButton.setDisable(true);
                    numUnitsSelectedTextField.setText("");
                    totalProfitLabel.setText("0");
                    numUnitsSelectedLabel.setText("0");
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
            currentSeedNameLabel.setText("Ничего не выбрано...");
            currentImage.setImage(null);
            totalProfitLabel.setText("0");
            numUnitsSelectedLabel.setText("0");
        }
    }

    /** Показывает кол-во выбранных позиций и общую стоимость продажи */
    @FXML
    private void showNumUnitsSelected() {
        try {
            numUnitsSelected = Integer.parseInt(numUnitsSelectedTextField.getText());
            if (numUnitsSelected < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            numUnitsSelected = 0;
            numUnitsSelectedTextField.setText("");
            numUnitsSelectedLabel.setText("0");
            totalProfitLabel.setText("0");
            return;
        }
        int totalCost = currentVegetativeType.getSalePrice() * numUnitsSelected;
        numUnitsSelectedLabel.setText(String.valueOf(numUnitsSelected));
        totalProfitLabel.setText(String.valueOf(totalCost));
    }

    /** Продажа: Уменьшение количества данной позиции на складе и увеличение баланса */
    @FXML
    private void handleSell() {
        if (numUnitsSelected == 0 || numUnitsSelected > currentVegetativeType.getCount()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            //alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("Предупреждение");
            if (numUnitsSelected == 0) {
                alert.setHeaderText("Вы пытаетесь продать 0 штук.");
                alert.setContentText("Пожалуйста, выберете, сколько штук желаете продать.");
            } else {
                alert.setHeaderText(
                        "Вы не можете продать " + numUnitsSelected
                                + " штук. Доступно лишь: " + currentVegetativeType.getCount());
                alert.setContentText("Пожалуйста, выберете доступное количество товара.");
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

            //Установка поля ввода и лейблов в стандартное положение
            numUnitsSelectedTextField.setText("");
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
        field.deleteVegetable(currentVegetativeType, numUnitsSelected);
    }

    private void disableMainElements() {
        numUnitsSelectedTextField.setDisable(true);
        sellButton.setDisable(true);
        plantButton.setDisable(true);
    }

    public boolean isPlantClicked() {
        return plantClicked;
    }

    public void setMainApp(Main MainApp) {
        this.mainApp = MainApp;
        player = mainApp.getPlayer();
        field = mainApp.getField();
        storageSeedsTable.setItems(mainApp.getStorageSeeds());
        storagePlantsTable.setItems(mainApp.getStoragePlants());
        balanceLabel.setText(Integer.toString(player.getBalance()));
    }

    public void setStoreStage(Stage storageStage) { this.storeStage = storageStage; }
}
