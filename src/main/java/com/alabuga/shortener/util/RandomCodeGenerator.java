package com.alabuga.shortener.util;

import java.security.SecureRandom;
import java.util.Random;

public class RandomCodeGenerator {
    
    private static final String ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int BASE = ALPHABET.length();
    private static final int MIN_LENGTH = 4;
    private static final int MAX_LENGTH = 8;
    
    private static final Random random = new SecureRandom();
    
    /**
     * Генерирует рандомный код длиной от MIN_LENGTH до MAX_LENGTH символов
     */
    public static String generateRandomCode() {
        int length = MIN_LENGTH + random.nextInt(MAX_LENGTH - MIN_LENGTH + 1);
        return generateRandomCode(length);
    }
    
    /**
     * Генерирует рандомный код указанной длины
     */
    public static String generateRandomCode(int length) {
        if (length < MIN_LENGTH) {
            length = MIN_LENGTH;
        }
        
        StringBuilder sb = new StringBuilder(length);
        
        // Первый символ не должен быть цифрой для лучшей читаемости
        sb.append(ALPHABET.charAt(10 + random.nextInt(52))); // только буквы
        
        // Остальные символы могут быть любыми
        for (int i = 1; i < length; i++) {
            sb.append(ALPHABET.charAt(random.nextInt(BASE)));
        }
        
        return sb.toString();
    }
    
    /**
     * Генерирует рандомный код с гарантированной минимальной длиной
     */
    public static String generateRandomCodeMinLength(int minLength) {
        int length = Math.max(minLength, MIN_LENGTH);
        return generateRandomCode(length);
    }
    
    /**
     * Проверяет, является ли строка валидным кодом
     */
    public static boolean isValidCode(String code) {
        if (code == null || code.length() < MIN_LENGTH) {
            return false;
        }
        
        for (char c : code.toCharArray()) {
            if (ALPHABET.indexOf(c) == -1) {
                return false;
            }
        }
        
        return true;
    }
}
