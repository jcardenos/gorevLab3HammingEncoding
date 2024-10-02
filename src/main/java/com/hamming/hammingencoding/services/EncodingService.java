package com.hamming.hammingencoding.services;

import com.hamming.hammingencoding.utils.AlertUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class EncodingService {
    private final FileService fileService;

    public EncodingService(FileService fileService) {
        this.fileService = fileService;
    }

    public void encode(String input, File file) throws IOException {
        String binaryStr = convertStringToBinary(input);
        String encodedMessage = encodeHamming(binaryStr);
        fileService.saveToFile(encodedMessage, "encoded_" + fileService.getFileNameWithoutExtension(file) + ".txt");
    }

    public void decode(String input, File file) throws IOException {
        String correctedBinaryStr = decodeHamming(input);
        String decodedStr = convertBinaryToString(correctedBinaryStr);
        fileService.saveToFile(decodedStr, "decoded_" + fileService.getFileNameWithoutExtension(file) + ".txt");
    }

    // Преобразование строки в двоичное представление с использованием кодировки UTF-8
    static String convertStringToBinary(String input) {
        StringBuilder binary = new StringBuilder();
        byte[] bytes = input.getBytes(StandardCharsets.UTF_8); // Преобразование строки в байты с кодировкой UTF-8
        for (byte b : bytes) {
            String binString = String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
            binary.append(binString);
        }
        return binary.toString();
    }

    // Преобразование двоичной строки обратно в текст с использованием кодировки UTF-8
    static String convertBinaryToString(String binaryStr) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        for (int i = 0; i < binaryStr.length(); i += 8) {
            String byteStr = binaryStr.substring(i, Math.min(i + 8, binaryStr.length()));
            int byteValue = Integer.parseInt(byteStr, 2);
            byteArrayOutputStream.write(byteValue);
        }
        return byteArrayOutputStream.toString(StandardCharsets.UTF_8); // Преобразование байтов обратно в строку с использованием UTF-8
    }

    // Реализация кодирования Хэмминга
    private static String encodeHamming(String binaryStr) {
        StringBuilder encodedMessage = new StringBuilder();

        // Разбиение двоичной строки на блоки по 4 символа
        for (int i = 0; i < binaryStr.length(); i += 4) {
            String block = binaryStr.substring(i, Math.min(i + 4, binaryStr.length()));
            String encodedBlock = encodeHammingBlock(block);
            encodedMessage.append(encodedBlock).append(" ");
        }

        return encodedMessage.toString();
    }

    // Кодирование одного блока Хэмминга
    private static String encodeHammingBlock(String block) {
        // Добавляем резервные биты в позиции степеней двойки (r1, r2, r4 и т.д.)
        char[] encodedBlock = new char[7]; // Длина блока с резервными битами

        // Расположение битов данных
        encodedBlock[2] = block.length() > 0 ? block.charAt(0) : '0';
        encodedBlock[4] = block.length() > 1 ? block.charAt(1) : '0';
        encodedBlock[5] = block.length() > 2 ? block.charAt(2) : '0';
        encodedBlock[6] = block.length() > 3 ? block.charAt(3) : '0';

        // Вычисление контрольных битов
        encodedBlock[0] = calculateParity(encodedBlock, new int[]{2, 4, 6}); // r1
        encodedBlock[1] = calculateParity(encodedBlock, new int[]{2, 5, 6}); // r2
        encodedBlock[3] = calculateParity(encodedBlock, new int[]{4, 5, 6}); // r4

        return new String(encodedBlock);
    }

    // Декодирование с использованием кода Хэмминга
    private static String decodeHamming(String encodedStr) {
        StringBuilder decodedMessage = new StringBuilder();

        // Удаление всех пробелов
        encodedStr = encodedStr.replaceAll("\\s+", "");

        // Разбиение закодированной строки на блоки по 7 символов
        for (int i = 0; i < encodedStr.length(); i += 7) {
            String block = encodedStr.substring(i, Math.min(i + 7, encodedStr.length()));
            String decodedBlock = decodeHammingBlock(block);
            decodedMessage.append(decodedBlock);
        }

        return decodedMessage.toString();
    }

    // Декодирование одного блока Хэмминга
    private static String decodeHammingBlock(String block) {
        // Определение позиций битов r1, r2 и r4
        char[] encodedBlock = block.toCharArray();
        int errorPosition = 0;

        if (calculateParity(encodedBlock, new int[]{2, 4, 6}) != encodedBlock[0]) {
            errorPosition += 1; // Ошибка в r1
        }
        if (calculateParity(encodedBlock, new int[]{2, 5, 6}) != encodedBlock[1]) {
            errorPosition += 2; // Ошибка в r2
        }
        if (calculateParity(encodedBlock, new int[]{4, 5, 6}) != encodedBlock[3]) {
            errorPosition += 4; // Ошибка в r4
        }

        // Исправление ошибки, если она есть
        if (errorPosition > 0) {
            int errorIndex = errorPosition - 1;
            encodedBlock[errorIndex] = encodedBlock[errorIndex] == '0' ? '1' : '0'; // Инвертирование ошибочного бита
            AlertUtils.showErrorAlert("Ошибка", "Был исправлен ошибочный бит с индексом " + errorIndex + " в позиции r" + errorPosition);
        }

        // Извлечение данных (биты на позициях 2, 4, 5 и 6)
        StringBuilder decodedBlock = new StringBuilder();
        decodedBlock.append(encodedBlock[2]);
        decodedBlock.append(encodedBlock[4]);
        decodedBlock.append(encodedBlock[5]);
        decodedBlock.append(encodedBlock[6]);

        return decodedBlock.toString();
    }

    // Вычисление контрольного бита
    private static char calculateParity(char[] block, int[] positions) {
        int count = 0;
        for (int pos : positions) {
            if (block[pos] == '1') {
                count++;
            }
        }
        return (count % 2 == 0) ? '0' : '1';
    }
}
