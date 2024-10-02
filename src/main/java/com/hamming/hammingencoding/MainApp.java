package com.hamming.hammingencoding;

import com.hamming.hammingencoding.controllers.EncodingController;
import com.hamming.hammingencoding.services.EncodingService;
import com.hamming.hammingencoding.services.FileService;
import com.hamming.hammingencoding.utils.UIManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) {
        FileService fileService = new FileService();
        EncodingService encodingService = new EncodingService(fileService);
        EncodingController encodingController = new EncodingController(encodingService, fileService);
        UIManager uiManager = new UIManager(encodingController);

        Scene scene = uiManager.createMainScene(stage);
        stage.setScene(scene);
        stage.setTitle("Hamming Encoding/Decoding");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
