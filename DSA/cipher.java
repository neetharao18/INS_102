import java.util.*;

public class KeyGenerator {
    
    // Function to convert a string to binary representation
    static String stringToBinary(String s) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            result.append(String.format("%8s", Integer.toBinaryString(s.charAt(i))).replace(' ', '0'));
        }
        return result.toString();
    }
    
    // Main function to process the input string and generate keys
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
        // Input string
        System.out.print("Enter a string: ");
        String input = sc.nextLine();
        
        // Convert the string to binary representation
        String binaryString = stringToBinary(input);
        System.out.println("Binary Representation: " + binaryString);
        
        // Take every 8th bit and store it in answer
        StringBuilder answer = new StringBuilder();
        for (int i = 0; i < binaryString.length(); i++) {
            if (i % 8 == 0) {
                answer.append(binaryString.charAt(i));
            }
        }
        
        // Split the answer into left and right parts
        int l = answer.length() / 2;
        String left = answer.substring(0, l);
        String right = answer.substring(l);
        
        // The predefined list of left-side transformation indices
        int[] lt = {2, 3, 6, 7, 1, 6, 5, 9};
        
        // List to store the keys
        List<String> keys = new ArrayList<>();
        
        // Generate 8 keys
        for (int i = 0; i < 8; i++) {
            StringBuilder newKey = new StringBuilder();
            StringBuilder newAnswer = new StringBuilder();
            
            // Perform bit manipulations
            int nl = Integer.parseInt(left, 2);   // Convert left part to decimal
            nl = nl << lt[i]; // Shift by the index from lt[i]
            
            int nr = Integer.parseInt(right, 2);  // Convert right part to decimal
            nr = nr << lt[i]; // Shift by the index from lt[i]
            
            String num = Integer.toBinaryString(nr + nl); // Concatenate the shifted values

            // Randomly select 8 bits and generate new keys
            Set<Integer> rm = new HashSet<>();
            Random rand = new Random();
            while (rm.size() < 8) {
                int r = rand.nextInt(num.length());
                rm.add(r);
            }
            
            // Create new answer by excluding randomly selected bits
            for (int j = 0; j < num.length(); j++) {
                if (!rm.contains(j)) {
                    newAnswer.append(num.charAt(j));
                }
            }
            keys.add(newAnswer.toString());
        }
        
        // Output generated keys
        for (int i = 0; i < keys.size(); i++) {
            System.out.println("Key " + (i + 1) + " = " + keys.get(i));
        }
    }
}
