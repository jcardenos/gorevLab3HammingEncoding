package com.hamming.hammingencoding.services;

import com.hamming.hammingencoding.utils.AlertUtils;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.charset.StandardCharsets;

public class FileService {
    public String readFile(File file) throws IOException {
        return Files.readString(file.toPath(), StandardCharsets.UTF_8);
    }

    public void saveToFile(String data, String fileName) throws IOException {
        File file = new File(System.getProperty("user.dir"), fileName);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(data);
            AlertUtils.showInfoAlert("Успешно", "Данные сохранены в файл " + file.getAbsolutePath());
            openFileLocation(file);
        } catch (IOException e) {
            AlertUtils.showErrorAlert("Ошибка", "Ошибка при сохранении данных в файл: " + e.getMessage());
        }
    }

    public void openFileLocation(File file) {
        try {
            File parentDirectory = file.getParentFile();
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(parentDirectory);
            } else {
                AlertUtils.showErrorAlert("Ошибка", "Открытие проводника не поддерживается.");
            }
        } catch (IOException e) {
            AlertUtils.showErrorAlert("Ошибка", "Не удалось открыть проводник.");
        }
    }

    public String getFileNameWithoutExtension(File file) {
        if (file != null && file.exists()) {
            String fileName = file.getName();
            int lastDotIndex = fileName.lastIndexOf(".");
            if (lastDotIndex > 0) {
                return fileName.substring(0, lastDotIndex);
            }
            return fileName;
        }
        return null;
    }
}
