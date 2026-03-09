package com.ingeniusapps.antares.security;

import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

public class Security {

    public enum SecretKeyMode {
        UltraFast,
        Fast,
        Strong,
        Superman,
        Paranoid,
        Sick
    }

    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String KEY_ALGORITHM = "AES";
    private static final int KEY_SIZE = 128; // Tamaño de la clave AES
    private static final int SALT_LENGTH = 16; // Longitud del salt en bytes
    private static final int IV_LENGTH = 16; // Longitud del IV en bytes

    private Security() {
    }

    // Deriva una clave AES a partir de una contraseña y un salt
    private static SecretKey deriveKey(String cryptokey, byte[] salt, SecretKeyMode secretKeyMode) throws Exception {
        int iterationCount = switch (secretKeyMode) {
            case UltraFast ->
                10000;
            case Fast ->
                20000;
            case Strong ->
                65536;
            case Superman ->
                100000;
            case Paranoid ->
                300000;
            case Sick ->
                600000;
            default ->
                65536;
        };

        PBEKeySpec spec = new PBEKeySpec(cryptokey.toCharArray(), salt, iterationCount, KEY_SIZE);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] keyBytes = factory.generateSecret(spec).getEncoded();
        return new SecretKeySpec(keyBytes, KEY_ALGORITHM);
    }

    // Cifra el texto plano usando la clave derivada y un IV aleatorio
    public static String encrypt(String plainText, String cryptokey, SecretKeyMode secretKeyMode) throws Exception {
        // Genera un salt y un IV aleatorios para cada mensaje
        byte[] salt = new byte[SALT_LENGTH];
        byte[] ivBytes = new byte[IV_LENGTH];
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);
        random.nextBytes(ivBytes);

        // Deriva la clave usando la contraseña y el salt
        SecretKey secretKey = deriveKey(cryptokey, salt, secretKeyMode);

        // Configura el cifrado con AES en modo CBC
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(ivBytes));
        byte[] encryptedData = cipher.doFinal(plainText.getBytes("UTF-8"));

        // Devuelve el salt, IV y datos cifrados en Base64, separados por ':'
        return EncodeBase64(salt) + ":" + EncodeBase64(ivBytes) + ":" + EncodeBase64(encryptedData);
    }

    // Descifra el texto cifrado usando la clave derivada y el IV
    public static String decrypt(String encryptedText, String cryptokey, SecretKeyMode secretKeyMode) throws Exception {
        // Divide el mensaje en salt, IV y datos cifrados
        String[] parts = encryptedText.split(":");
        byte[] salt = DecodeBase64(parts[0]);
        byte[] ivBytes = DecodeBase64(parts[1]);
        byte[] encryptedData = DecodeBase64(parts[2]);

        // Deriva la clave usando la contraseña y el salt
        SecretKey secretKey = deriveKey(cryptokey, salt, secretKeyMode);

        // Configura el descifrado con AES en modo CBC
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(ivBytes));
        byte[] decryptedData = cipher.doFinal(encryptedData);

        return new String(decryptedData, "UTF-8");
    }

    public static byte[] DecodeBase64(String coded64) {
        try {
            return Base64.getDecoder().decode(coded64);
        } catch (IllegalArgumentException ex) {
            return new byte[0];
        }
    }

    public static String EncodeBase64(byte[] value) {
        return Base64.getEncoder().encodeToString(value);
    }
}
