import java.math.BigInteger; 
import java.util.Scanner; 
 
public class feistel { 
 
    // Convert string to 8-bit binary representation 
    public static String stringToBinary(String str) { 
        StringBuilder binary = new StringBuilder(); 
        for (char c : str.toCharArray()) { 
            binary.append(String.format("%8s", Integer.toBinaryString(c)).replace(' ', '0')); 
        } 
        return binary.toString(); 
    } 
 
    // Convert binary back to string 
    public static String binaryToString(String binary) { 
        StringBuilder str = new StringBuilder(); 
        for (int i = 0; i < binary.length(); i += 8) { 
            str.append((char) Integer.parseInt(binary.substring(i, i + 8), 2)); 
        } 
        return str.toString(); 
    } 
 
    // Simple encryption: XOR input with the key (repeated as needed) 
    public static String encrypt(String input, String key) { 
        String inputBinary = stringToBinary(input); 
        String keyBinary = stringToBinary(key); 
 
        // Ensure key length matches input length by repeating it 
        StringBuilder keyBuilder = new StringBuilder(); 
        while (keyBuilder.length() < inputBinary.length()) { 
            keyBuilder.append(keyBinary); 
        } 
        keyBinary = keyBuilder.toString(); 
 
        // XOR operation using BigInteger for large binary numbers 
        StringBuilder encrypted = new StringBuilder(); 
        for (int i = 0; i < inputBinary.length(); i++) { 
            encrypted.append(inputBinary.charAt(i) == keyBinary.charAt(i) ? '0' : '1'); 
        } 
        return encrypted.toString(); 
    } 
 
    // Simple decryption: XOR cipher with the key (repeated as needed) 
    public static String decrypt(String cipher, String key) { 
        String keyBinary = stringToBinary(key); 
 
        // Ensure key length matches cipher length by repeating it 
        StringBuilder keyBuilder = new StringBuilder(); 
        while (keyBuilder.length() < cipher.length()) { 
            keyBuilder.append(keyBinary); 
        } 
        keyBinary = keyBuilder.toString(); 
 
        // XOR operation 
        StringBuilder decrypted = new StringBuilder(); 
        for (int i = 0; i < cipher.length(); i++) { 
            decrypted.append(cipher.charAt(i) == keyBinary.charAt(i) ? '0' : '1'); 
        } 
 
        // Convert binary back to string 
        return binaryToString(decrypted.toString()); 
    } 
 
    public static void main(String[] args) { 
        Scanner scanner = new Scanner(System.in); 
 
        // Take user input 
        System.out.print("Enter a string: "); 
        String input = scanner.nextLine(); 
         
        System.out.print("Enter a key: "); 
        String key = scanner.nextLine(); 
 
        // Encrypt the input 
        String encrypted = encrypt(input, key); 
        System.out.println("Cipher: " + encrypted); 
 
        // Decrypt the cipher back to the original input 
        String decrypted = decrypt(encrypted, key); 
        System.out.println("Decrypted: " + decrypted); 
    } 
}
