package com.hamming.hammingencoding.controllers;

import com.hamming.hammingencoding.services.EncodingService;
import com.hamming.hammingencoding.services.FileService;
import com.hamming.hammingencoding.utils.AlertUtils;

import java.io.File;
import java.io.IOException;

public class EncodingController {
    private final EncodingService encodingService;
    private final FileService fileService;

    public EncodingController(EncodingService encodingService, FileService fileService) {
        this.encodingService = encodingService;
        this.fileService = fileService;
    }

    public void processFile(File file, String action) {
        try {
            String content = fileService.readFile(file);
            if ("Encode".equals(action)) {
                encodingService.encode(content, file);
            } else if ("Decode".equals(action)) {
                encodingService.decode(content, file);
            }
        } catch (IOException e) {
            AlertUtils.showErrorAlert("Ошибка", "Не удалось обработать файл: " + e.getMessage());
        }
    }
}
