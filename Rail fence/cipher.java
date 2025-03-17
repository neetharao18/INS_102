import java.util.ArrayList;
import java.util.List;

public class RailFenceCipher {

    // Method to encrypt the message using Rail Fence Cipher
    public static String railFenceEncrypt(String plaintext, int depth) {
        // Create a list of lists (fence) to represent the rows
        List<List<Character>> fence = new ArrayList<>();
        
        // Initialize the rows (depth number of rows)
        for (int i = 0; i < depth; i++) {
            fence.add(new ArrayList<>());
        }

        int rail = 0;
        int direction = 1;

        // Fill the fence with characters in a zigzag pattern
        for (char ch : plaintext.toCharArray()) {
            fence.get(rail).add(ch);
            rail += direction;
            if (rail == 0 || rail == depth - 1) {
                direction *= -1;  // Change direction at the top or bottom
            }
        }

        // Build the encrypted string by concatenating the rows
        StringBuilder cipherText = new StringBuilder();
        for (List<Character> row : fence) {
            for (char ch : row) {
                cipherText.append(ch);
            }
        }

        return cipherText.toString();
    }

    // Method to decrypt the message using Rail Fence Cipher
    public static String railFenceDecrypt(String cipherText, int depth) {
        // Create a list of lists (fence) to represent the rows
        List<List<Character>> fence = new ArrayList<>();
        
        // Initialize the rows (depth number of rows)
        for (int i = 0; i < depth; i++) {
            fence.add(new ArrayList<>());
        }

        // Create an array to track the zigzag pattern positions
        int rail = 0;
        int direction = 1;

        // Mark the fence with placeholders ('*') to track the character positions
        int[] railLengths = new int[depth]; // Keep track of how many characters go into each rail
        for (int i = 0; i < cipherText.length(); i++) {
            railLengths[rail]++;
            rail += direction;
            if (rail == 0 || rail == depth - 1) {
                direction *= -1;
            }
        }

        // Place the cipherText into the fence based on the zigzag pattern
        int index = 0;
        for (int i = 0; i < depth; i++) {
            for (int j = 0; j < railLengths[i]; j++) {
                fence.get(i).add(cipherText.charAt(index++));
            }
        }

        // Reconstruct the original text by reading the fence row by row
        StringBuilder plainText = new StringBuilder();
        rail = 0;
        direction = 1;
        for (int i = 0; i < cipherText.length(); i++) {
            plainText.append(fence.get(rail).remove(0)); // Read one character from the current rail
            rail += direction;
            if (rail == 0 || rail == depth - 1) {
                direction *= -1;
            }
        }

        return plainText.toString();
    }

    public static void main(String[] args) {
        // Example usage
        String plaintext = "HELLO";
        int depth = 3;

        // Encryption
        String encrypted = railFenceEncrypt(plaintext, depth);
        System.out.println("Encrypted: " + encrypted);

        // Decryption
        String decrypted = railFenceDecrypt(encrypted, depth);
        System.out.println("Decrypted: " + decrypted);
    }
}
