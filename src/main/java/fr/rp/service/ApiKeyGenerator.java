package fr.rp.service;

import java.security.SecureRandom;

public class ApiKeyGenerator {

    public static String generateApiKey() {
            SecureRandom random = new SecureRandom();
            String caracteresValides = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

            StringBuilder cleUnique = new StringBuilder();
            for (int i = 0; i < 32; i++) {
                int index = random.nextInt(caracteresValides.length());
                char caractere = caracteresValides.charAt(index);
                cleUnique.append(caractere);
            }

            return cleUnique.toString();
        }
}
