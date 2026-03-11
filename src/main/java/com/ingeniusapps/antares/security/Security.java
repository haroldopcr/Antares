package com.ingeniusapps.antares.security;

import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Utilidades criptográficas para cifrado y descifrado simétrico basadas en AES.
 *
 * <p>Esta clase proporciona operaciones de cifrado y descifrado de texto usando
 * una clave derivada desde una contraseña mediante PBKDF2 con HMAC-SHA256, junto
 * con un salt aleatorio y un vector de inicialización (IV) aleatorio por cada
 * operación de cifrado.</p>
 *
 * <p>El algoritmo de cifrado utilizado es {@code AES/CBC/PKCS5Padding}. La clave
 * AES se deriva con distintos niveles de costo computacional definidos por
 * {@link SecretKeyMode}.</p>
 *
 * <p>El formato de salida del método {@link #encrypt(String, String, SecretKeyMode)}
 * consiste en tres segmentos codificados en Base64 separados por {@code :}:
 * salt, IV y contenido cifrado.</p>
 *
 * <p>Esta clase está diseñada como utilitaria y no debe ser instanciada.</p>
 */
public final class Security {

    /**
     * Modos predefinidos para la derivación de clave.
     *
     * <p>Cada valor representa una cantidad distinta de iteraciones PBKDF2,
     * permitiendo equilibrar rendimiento y fortaleza criptográfica según las
     * necesidades de la aplicación.</p>
     */
    public enum SecretKeyMode {

        /**
         * Modo de derivación de clave con muy baja carga computacional relativa.
         */
        UltraFast,

        /**
         * Modo de derivación de clave rápido.
         */
        Fast,

        /**
         * Modo de derivación de clave fuerte con configuración equilibrada.
         */
        Strong,

        /**
         * Modo de derivación de clave con mayor endurecimiento computacional.
         */
        Superman,

        /**
         * Modo de derivación de clave de alta exigencia.
         */
        Paranoid,

        /**
         * Modo de derivación de clave de máxima exigencia disponible en esta clase.
         */
        Sick
    }

    /**
     * Transformación criptográfica utilizada para cifrado y descifrado.
     */
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";

    /**
     * Algoritmo base de la clave simétrica.
     */
    private static final String KEY_ALGORITHM = "AES";

    /**
     * Tamaño de la clave AES en bits.
     */
    private static final int KEY_SIZE = 128;

    /**
     * Longitud del salt en bytes.
     */
    private static final int SALT_LENGTH = 16;

    /**
     * Longitud del vector de inicialización (IV) en bytes.
     */
    private static final int IV_LENGTH = 16;

    /**
     * Evita la instanciación accidental de esta clase utilitaria.
     */
    private Security() {
    }

    /**
     * Deriva una clave AES a partir de una contraseña, un salt y un modo de derivación.
     *
     * <p>La derivación se realiza mediante PBKDF2 con HMAC-SHA256. El número de
     * iteraciones depende del valor de {@link SecretKeyMode} proporcionado.</p>
     *
     * @param cryptokey contraseña base utilizada para derivar la clave
     * @param salt salt aleatorio usado en la derivación
     * @param secretKeyMode modo de derivación que determina la cantidad de iteraciones
     * @return clave AES derivada
     * @throws Exception si ocurre un error durante la derivación de la clave
     */
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

    /**
     * Cifra un texto plano usando una clave derivada desde la contraseña indicada.
     *
     * <p>Para cada operación se genera un salt aleatorio y un IV aleatorio. Ambos
     * valores, junto con el resultado cifrado, se retornan codificados en Base64
     * y concatenados con el separador {@code :}.</p>
     *
     * @param plainText texto plano que se desea cifrar
     * @param cryptokey contraseña base utilizada para derivar la clave
     * @param secretKeyMode modo de derivación de clave
     * @return cadena compuesta por {@code salt:iv:data} codificados en Base64
     * @throws Exception si ocurre un error durante el proceso de cifrado
     */
    public static String encrypt(String plainText, String cryptokey, SecretKeyMode secretKeyMode) throws Exception {
        byte[] salt = new byte[SALT_LENGTH];
        byte[] ivBytes = new byte[IV_LENGTH];
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);
        random.nextBytes(ivBytes);

        SecretKey secretKey = deriveKey(cryptokey, salt, secretKeyMode);

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(ivBytes));
        byte[] encryptedData = cipher.doFinal(plainText.getBytes("UTF-8"));

        return EncodeBase64(salt) + ":" + EncodeBase64(ivBytes) + ":" + EncodeBase64(encryptedData);
    }

    /**
     * Descifra un texto previamente cifrado con {@link #encrypt(String, String, SecretKeyMode)}.
     *
     * <p>La entrada debe contener exactamente tres segmentos separados por {@code :},
     * correspondientes al salt, al IV y al contenido cifrado, todos codificados
     * en Base64.</p>
     *
     * @param encryptedText texto cifrado en formato {@code salt:iv:data}
     * @param cryptokey contraseña base utilizada para derivar la clave
     * @param secretKeyMode modo de derivación de clave
     * @return texto plano descifrado
     * @throws Exception si ocurre un error durante el descifrado o si el contenido
     *         de entrada no es válido para el algoritmo esperado
     */
    public static String decrypt(String encryptedText, String cryptokey, SecretKeyMode secretKeyMode) throws Exception {
        String[] parts = encryptedText.split(":");
        byte[] salt = DecodeBase64(parts[0]);
        byte[] ivBytes = DecodeBase64(parts[1]);
        byte[] encryptedData = DecodeBase64(parts[2]);

        SecretKey secretKey = deriveKey(cryptokey, salt, secretKeyMode);

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(ivBytes));
        byte[] decryptedData = cipher.doFinal(encryptedData);

        return new String(decryptedData, "UTF-8");
    }

    /**
     * Decodifica una cadena Base64 a un arreglo de bytes.
     *
     * @param coded64 cadena codificada en Base64
     * @return arreglo de bytes decodificado; si la entrada no es Base64 válido,
     *         retorna un arreglo vacío
     */
    public static byte[] DecodeBase64(String coded64) {
        try {
            return Base64.getDecoder().decode(coded64);
        } catch (IllegalArgumentException ex) {
            return new byte[0];
        }
    }

    /**
     * Codifica un arreglo de bytes en una cadena Base64.
     *
     * @param value arreglo de bytes a codificar
     * @return representación Base64 del arreglo de bytes
     */
    public static String EncodeBase64(byte[] value) {
        return Base64.getEncoder().encodeToString(value);
    }
}