import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
public class CS_EncryptionUtils {
    /**
     * Enumération des longueurs de clé supportées.
     */
    public enum LONGUEUR_CLE {
        N_128(128),
        N_192(192),
        N_256(256);

        private final int value;

        LONGUEUR_CLE(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }

    /**
     * Algorithme de chiffrement utilisé.
     */
    public static final String ALGORITHM = "AES/CBC/PKCS5Padding";

    /**
     * Génère une clé secrète
     * @param longueurCle La taille de la clé. 128, 192 ou 256.
     * @return La clé secrète
     * @throws NoSuchAlgorithmException
     */
    public static SecretKey generateKey(LONGUEUR_CLE longueurCle) throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(longueurCle.getValue());
        SecretKey secretKey = keyGenerator.generateKey();
        return secretKey;
    }

    /**
     * Génère une clé secrète à partir d'un mot de passe et d'un sel.
     *
     * @param password Le mot de passe à partir duquel générer la clé secrète.
     * @param salt Le sel utilisé pour renforcer la sécurité du processus de dérivation de clé.
     * @return La clé secrète générée à partir du mot de passe et du sel.
     * @throws NoSuchAlgorithmException Si l'algorithme de dérivation de clé spécifié n'est pas disponible.
     * @throws InvalidKeySpecException Si les spécifications de la clé secrète sont invalides.
     */
    public static SecretKey getKeyFromPassword(String password, String salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 256);
        SecretKey secretkey = new SecretKeySpec(secretKeyFactory.generateSecret(spec).getEncoded(), "AES");
        return secretkey;
    }

    /**
     * Génère une clé secrète à partir d'une chaîne de caractères encodée en base64.
     * @param b64secretKey La chaîne de caractères encodée en base64.
     * @return La clé secrète générée à partir de la chaîne de caractères encodée en base64.
     */
    public static SecretKey getKeyFromBase64String(String b64secretKey) {
        byte[] decodedKey = Base64.getDecoder().decode(b64secretKey);
        SecretKeySpec secretKeySpec = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
        return secretKeySpec;
    }

    /**
     * Génère un vecteur d'initialisation aléatoire.
     * @return Le vecteur d'initialisation aléatoire.
     */
    public static IvParameterSpec generateIv() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    /**
     * Génère un vecteur d'initialisation à partir d'un tableau de bytes.
     * @param iv Le tableau de bytes à partir duquel générer le vecteur d'initialisation.
     * @return Le vecteur d'initialisation généré à partir du tableau de bytes.
     */
    public static IvParameterSpec generateIv(byte[] iv) {
        return new IvParameterSpec(iv);
    }

    /**
     * Génère un vecteur d'initialisation à partir d'une chaîne de caractères encodée en base64.
     * @param base64String La chaîne de caractères encodée en base64.
     * @return Le vecteur d'initialisation généré à partir de la chaîne de caractères encodée en base64.
     */
    public static IvParameterSpec generateIv(String base64String) {
        return new IvParameterSpec(Base64.getDecoder().decode(base64String));
    }

    /**
     * Retourne la clé secrète en format base64
     * @param secretKey La clé secrète à partir de laquelle générer la chaîne de caractères encodée en base64.
     * @return La chaîne de caractères encodée en base64 générée à partir de la clé secrète.
     */
    public static String getBase64SecretKey(SecretKey secretKey) {
        return new String(Base64.getEncoder().encode(secretKey.getEncoded()));
    }

    /**
     * Retourne le vecteur d'initialisation en format base64.
     * @param iv Le vecteur d'initialisation à partir duquel générer la chaîne de caractères encodée en base64.
     * @return La chaîne de caractères encodée en base64 générée à partir du vecteur d'initialisation.
     */
    public static String getBase64Iv(IvParameterSpec iv) {
        return new String(Base64.getEncoder().encode(iv.getIV()));
    }

    /**
     * Chiffre une chaîne de caractères avec une clé secrète et un vecteur d'initialisation.
     * @param input La chaîne de caractères à chiffrer.
     * @param key La clé secrète utilisée pour chiffrer la chaîne de caractères.
     * @param iv Le vecteur d'initialisation utilisé pour chiffrer la chaîne de caractères.
     * @return La chaîne de caractères chiffrée et encodée en base64.
     */
    public static String encrypt(String input, SecretKey key, IvParameterSpec iv) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance(CS_EncryptionUtils.ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        byte[] cipherText = cipher.doFinal(input.getBytes());
        return Base64.getEncoder().encodeToString(cipherText);
    }

    /**
     * Déchiffre une chaîne de caractères avec une clé secrète et un vecteur d'initialisation.
     * @param cipherText La chaîne de caractères en base64 à déchiffrer.
     * @param key La clé secrète utilisée pour déchiffrer la chaîne de caractères.
     * @param iv Le vecteur d'initialisation utilisé pour déchiffrer la chaîne de caractères.
     * @return La chaîne de caractères déchiffrée.
     */
    public static String decrypt(String cipherText, SecretKey key, IvParameterSpec iv) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance(CS_EncryptionUtils.ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(cipherText));
        return new String(plainText);
    }
}
