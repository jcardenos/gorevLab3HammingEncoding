package com.hamming.hammingencoding.utils;

import com.hamming.hammingencoding.controllers.EncodingController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;

public class UIManager {
    private final EncodingController encodingController;

    public UIManager(EncodingController encodingController) {
        this.encodingController = encodingController;
    }

    private final ObservableList<String> typesOfAction = FXCollections.observableArrayList("Encode", "Decode");
    private final ComboBox<String> typesOfActionComboBox = new ComboBox<>(typesOfAction);

    public Scene createMainScene(Stage primaryStage) {
        VBox vbox = new VBox();
        Label chooseTypeOfActionLabel = new Label("Выберите действие:");
        typesOfActionComboBox.setValue("Encode");

        Button selectFileButton = new Button("Выбрать файл");
        selectFileButton.setPrefWidth(500);
        selectFileButton.setOnAction(e -> handleFileSelection(primaryStage));

        Button exitButton = new Button("Выход");
        exitButton.setPrefWidth(500);
        exitButton.setOnAction(e -> primaryStage.close());
        exitButton.getStyleClass().add("exit");

        vbox.getChildren().addAll(chooseTypeOfActionLabel, typesOfActionComboBox, selectFileButton, exitButton);

        Scene scene = new Scene(vbox, 400, 200);
        applyCss(scene);  // Вызов метода для применения стилей
        return scene;
    }

    private void handleFileSelection(Stage primaryStage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        fileChooser.setTitle("Выберите файл для обработки");
        File file = fileChooser.showOpenDialog(primaryStage);

        if (file != null) {
            String selectedAction = typesOfActionComboBox.getValue();
            encodingController.processFile(file, selectedAction);
        }
    }

    private void applyCss(Scene scene) {
        String resourcePath = System.getProperty("user.dir") + File.separator + "style.css";
        File cssFile = new File(resourcePath);

        if (cssFile.exists()) {
            try {
                URL cssUrl = cssFile.toURI().toURL();
                scene.getStylesheets().add(cssUrl.toExternalForm());
            } catch (Exception e) {
                System.err.println("Ошибка при добавлении CSS файла: " + e.getMessage());
            }
        } else {
            System.err.println("CSS файл не найден.");
        }
    }
}
