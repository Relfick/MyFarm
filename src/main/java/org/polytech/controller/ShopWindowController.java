package org.polytech.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.polytech.Main;
import org.polytech.model.Field;
import org.polytech.model.Player;
import org.polytech.model.SeedType;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ShopWindowController {
    private Main mainApp;
    private Player player;
    private Field field;

    private SeedType currentSeedType;
    private int numUnitsSelected = 0;
    private Stage shopStage;

    @FXML
    private TableView<SeedType> shopSeedsTable;
    @FXML
    private TableColumn<SeedType, String> seedsNameColumn;
    @FXML
    private TableColumn<SeedType, Number> seedsGrowTimeColumn;
    @FXML
    private TableColumn<SeedType, Number> seedsSalePriceColumn;
    @FXML
    private TableColumn<SeedType, Number> seedsPurchasePriceColumn;


    @FXML
    private Label balanceLabel;
    @FXML
    private Label currentSeedNameLabel;
    @FXML
    private ImageView currentImage;
    @FXML
    private Label numUnitsSelectedLabel;
    @FXML
    private Label totalCostLabel;
    @FXML
    private Button purchaseButton;
    @FXML
    private TextField numUnitsSelectedTextField;

    public ShopWindowController() {

    }

    @FXML
    private void initialize() {

        /* Заполняем таблицу семян */
        seedsNameColumn.setCellValueFactory(
                cellData -> cellData.getValue().nameProperty());
        seedsGrowTimeColumn.setCellValueFactory(
                cellData -> cellData.getValue().growTimeProperty());
        seedsSalePriceColumn.setCellValueFactory(
                cellData -> cellData.getValue().salePriceProperty());
        seedsPurchasePriceColumn.setCellValueFactory(
                cellData -> cellData.getValue().purchasePriceProperty());

        shopSeedsTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {

                    showSeedsDetails(newValue);

                    currentSeedType = newValue;

                    numUnitsSelectedTextField.setDisable(false);
                    purchaseButton.setDisable(false);
                    numUnitsSelectedTextField.setText("");
                    numUnitsSelected = 0;
                    totalCostLabel.setText("0");
                    numUnitsSelectedLabel.setText("0");
                });


    }

    /** Показывает кол-во выбранных позиций и общую стоимость покупки */
    @FXML
    private void showNumUnitsSelected() {
        try {
            numUnitsSelected = Integer.parseInt(numUnitsSelectedTextField.getText());
            if (numUnitsSelected < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            numUnitsSelected = 0;
            numUnitsSelectedTextField.setText("");
            numUnitsSelectedLabel.setText("0");
            totalCostLabel.setText("0");
            return;
        }
        int totalCost = currentSeedType.getPurchasePrice() * numUnitsSelected;
        numUnitsSelectedLabel.setText(String.valueOf(numUnitsSelected));
        totalCostLabel.setText(String.valueOf(totalCost));
    }

    /** Продажа: Уменьшение количества данной позиции на складе и увеличение баланса */
    @FXML
    private void handlePurchase() {
        if (numUnitsSelected == 0 ||
                player.getBalance() < numUnitsSelected * currentSeedType.getPurchasePrice()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            //alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("Предупреждение");
            if (numUnitsSelected == 0) {
                alert.setHeaderText("Вы пытаетесь купить 0 шт.");
                alert.setContentText("Пожалуйста, выберете, сколько штук вы желаете купить..");
            } else {
                alert.setHeaderText("Недостаточно средств. ");
                alert.setContentText("Пожалуйста, выберите доступное количество товара.");
            }

            alert.showAndWait();
        } else {
            buy();
            balanceLabel.setText(String.valueOf(player.getBalance()));

            //Установка поля ввода и лейблов в стандартное положение
            numUnitsSelectedTextField.setText("");
            numUnitsSelected = 0;
            numUnitsSelectedLabel.setText("0");
            totalCostLabel.setText("0");
        }
    }

    private void buy() {
        player.reduceBalance(numUnitsSelected * currentSeedType.getPurchasePrice());
        field.addVegetable(currentSeedType, numUnitsSelected);
    }

    private void showSeedsDetails(SeedType seedType) {
        if (seedType != null) {
            currentSeedNameLabel.setText(seedType.getName());
            try {
                currentImage.setImage(new Image(new FileInputStream(seedType.getAdditionalImagePath())));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            currentSeedNameLabel.setText("Ничего не выбрано");
            currentImage.setImage(null);
            totalCostLabel.setText("0");
            numUnitsSelectedLabel.setText("0");
        }
    }

    public void setMainApp(Main MainApp) {
        this.mainApp = MainApp;
        player = mainApp.getPlayer();
        field = mainApp.getField();
        shopSeedsTable.setItems(mainApp.getAllAvailableSeeds());
        balanceLabel.setText(Integer.toString(player.getBalance()));
    }

    public void setShopStage(Stage shopStage) { this.shopStage = shopStage; }

}
