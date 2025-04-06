import java.security.*;
import java.util.Base64;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

public class HybridEncryptionJava {

    // Vigenère Cipher (Substitution)
    public static String vigenereEncrypt(String text, String key) {
        StringBuilder result = new StringBuilder();
        key = key.toUpperCase();
        int keyIndex = 0;

        for (char c : text.toCharArray()) {
            if (Character.isLetter(c)) {
                boolean isLower = Character.isLowerCase(c);
                char base = isLower ? 'a' : 'A';
                int shift = key.charAt(keyIndex % key.length()) - 'A';
                char encryptedChar = (char) ((c - base + shift) % 26 + base);
                result.append(encryptedChar);
                keyIndex++;
            } else {
                result.append(c);
            }
        }

        return result.toString();
    }

    // Columnar Transposition Cipher (Transposition)
    public static String columnarTranspositionEncrypt(String text, String key) {
        text = text.replaceAll("\\s+", "");
        int columns = key.length();
        String[] grid = new String[columns];
        for (int i = 0; i < columns; i++) grid[i] = "";

        for (int i = 0; i < text.length(); i++) {
            grid[i % columns] += text.charAt(i);
        }

        char[] keyChars = key.toCharArray();
        char[] sortedKey = key.toCharArray();
        java.util.Arrays.sort(sortedKey);

        StringBuilder ciphertext = new StringBuilder();
        for (char ch : sortedKey) {
            int index = indexOfChar(keyChars, ch);
            ciphertext.append(grid[index]);
            keyChars[index] = '*'; // mark as used
        }

        return ciphertext.toString();
    }

    private static int indexOfChar(char[] arr, char ch) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == ch) return i;
        }
        return -1;
    }

    // RSA Key Generation
    public static KeyPair generateRSAKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        return keyGen.generateKeyPair();
    }

    // RSA Encryption
    public static String rsaEncrypt(PublicKey publicKey, String data) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encrypted = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    // RSA Decryption
    public static String rsaDecrypt(PrivateKey privateKey, String encryptedData) throws Exception {
        byte[] data = Base64.getDecoder().decode(encryptedData);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decrypted = cipher.doFinal(data);
        return new String(decrypted);
    }

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        // Manual Input
        System.out.print("Enter Plaintext: ");
        String plaintext = scanner.nextLine();

        System.out.print("Enter Vigenère Key: ");
        String vigenereKey = scanner.nextLine();

        System.out.print("Enter Transposition Key: ");
        String transpositionKey = scanner.nextLine();

        // Step 1: Vigenère Encryption
        String substitutedText = vigenereEncrypt(plaintext, vigenereKey);

        // Step 2: Columnar Transposition
        String encryptedText = columnarTranspositionEncrypt(substitutedText, transpositionKey);

        // RSA Key Generation
        KeyPair rsaKeyPair = generateRSAKeyPair();
        PublicKey publicKey = rsaKeyPair.getPublic();
        PrivateKey privateKey = rsaKeyPair.getPrivate();

        // Step 3: RSA Encryption for Keys
        String encryptedVigenereKey = rsaEncrypt(publicKey, vigenereKey);
        String encryptedTranspositionKey = rsaEncrypt(publicKey, transpositionKey);

        // Output
        System.out.println("\nEncrypted Text: " + encryptedText);
        System.out.println("Encrypted Vigenère Key: " + encryptedVigenereKey);
        System.out.println("Encrypted Transposition Key: " + encryptedTranspositionKey);

        // Decrypt keys
        String decryptedVigenereKey = rsaDecrypt(privateKey, encryptedVigenereKey);
        String decryptedTranspositionKey = rsaDecrypt(privateKey, encryptedTranspositionKey);

        System.out.println("\nDecrypted Vigenère Key: " + decryptedVigenereKey);
        System.out.println("Decrypted Transposition Key: " + decryptedTranspositionKey);
    }
}
