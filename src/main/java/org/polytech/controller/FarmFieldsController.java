package org.polytech.controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import org.polytech.Main;
import org.polytech.model.Field;
import org.polytech.model.PlantType;
import org.polytech.model.Player;
import org.polytech.model.SeedType;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Timer;
import java.util.TimerTask;

public class FarmFieldsController {

    private Main mainApp;
    private Player player;
    private Field field;
    private SeedType currSeedType;

    private boolean isPlantingMode = false;

    @FXML
    private GridPane plantField;
    @FXML
    private Label balanceLabel;
    @FXML
    private Button stopPlantingButton;
    @FXML
    private Label seedsLeftLabel;
    @FXML
    private Button storageButton;
    @FXML
    private Button shopButton;
    @FXML
    private ImageView avatarImage;

    @FXML
    private void initialize() {
        plantField.getChildren().forEach(item -> item.setOnMouseClicked(mouseEvent -> {
            if (item instanceof ImageView) {

                /* Получаем координаты выбранной ячейки (или границы)*/
                Node source = (Node) mouseEvent.getSource();
                Integer cI = GridPane.getColumnIndex(source);
                Integer rI = GridPane.getRowIndex(source);
                int colI = cI != null ? cI : 0;
                int rowI = rI != null ? rI : 0;
                Pair<Integer, Integer> coords = new Pair<>(rowI, colI);

                if (isPlantingMode) {
                    if (!field.isAlreadyPlanted(coords)) {
                        field.plantSeed(coords, currSeedType);
                        try {
                            this.plant(item, coords);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }

                // Сбор созревшего растения, если в данной ячейке такое имеется
                if (field.hasGrown(coords)) {
                    ((ImageView)item).setImage(null);
                    SeedType harvest = field.getHarvest(coords);
                    String harvestName = harvest.getName();
                    for (PlantType plant: PlantType.values()) {
                        if (plant.getName().equals(harvestName)) {
                            field.addVegetable(plant, 1);
                            break;
                        }
                    }

                    field.deleteGrownPlant(coords);
                }
            }
        }));
    }

    @FXML
    private void handleOpenStorage() {
        setPlantingMode(mainApp.showStorage());
        balanceLabel.setText(String.valueOf(player.getBalance()));
    }

    @FXML
    private void handleOpenShop() {
        mainApp.showShop();
        balanceLabel.setText(String.valueOf(player.getBalance()));
    }

    @FXML
    private void handleStopPlanting() {
        setPlantingMode(false);
        seedsLeftLabel.setVisible(false);
        stopPlantingButton.setVisible(false);
        storageButton.setDisable(false);
        shopButton.setDisable(false);
    }

    public void setMainApp(Main MainApp) {
        this.mainApp = MainApp;
        player = mainApp.getPlayer();
        field = mainApp.getField();
        balanceLabel.setText(Integer.toString(player.getBalance()));
        avatarImage.setImage(player.getAvatar());
    }

    /** сажает растение в ячейку */
    private void plant(Node item, Pair<Integer, Integer> coords) throws FileNotFoundException {
        SeedType currSeed = currSeedType;
        storageButton.setDisable(true);
        shopButton.setDisable(true);

        seedsLeftLabel.setText("Осталось " + currSeed.getCount());

        //т.к. 2 стадии роста, делим общее время на 2
        int growTime = currSeed.getGrowTime() * 1000 / 2;

        /* Установка первоначальной картинки семени.
         * Далее, в таймере, через задержку growTime устанавливается картинка промежуточного роста.
         * Далее, после Thread.sleep(growTime) устанавливается последняя картинка выросшего растения */
        ImageView img = (ImageView) item;
        img.setImage(new Image(
                new FileInputStream(currSeed.getSeedImagePath(1))));

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    img.setImage(new Image(
                            new FileInputStream(currSeed.getSeedImagePath(2))));
                    Thread.sleep(growTime);
                    img.setImage(new Image(
                            new FileInputStream(currSeed.getSeedImagePath(3))));
                    field.setGrown(coords);
                } catch (InterruptedException | FileNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    timer.cancel();
                }
            }
        }, growTime);

        if (field.getCountOfSeed(currSeed) == 0)
            handleStopPlanting();
    }

    public void setPlantingMode(boolean mode) {
        currSeedType = mainApp.getCurrentSeedType();

        isPlantingMode = mode;
        if (mode) {
            stopPlantingButton.setVisible(true);
            seedsLeftLabel.setText("Осталось " + field.getCountOfSeed(currSeedType));
            seedsLeftLabel.setVisible(true);
        }
    }
}