import java.util.Scanner;

public class RSA {

    // Function to find GCD of two numbers
    static int gcd(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    // Function to perform RSA encryption and decryption
    static void RSA_encrypt(int p, int q, int message) {
        int n = p * q; // n = p * q
        int phi = (p - 1) * (q - 1); // phi = (p - 1) * (q - 1)

        // Finding 'e' such that 1 < e < phi and gcd(e, phi) == 1
        int e = 0;
        for (int i = 2; i < phi; i++) {
            if (gcd(i, phi) == 1) {
                e = i;
                break;
            }
        }

        // Finding 'd' such that (e * d) % phi == 1
        int d = 0;
        for (int i = 1; i < phi; i++) {
            if ((e * i) % phi == 1) {
                d = i;
                break;
            }
        }

        // Encryption: c = (message^e) % n
        int encryptedMessage = modExp(message, e, n);
        System.out.println("Encrypted message: " + encryptedMessage);

        // Decryption: m = (encryptedMessage^d) % n
        int decryptedMessage = modExp(encryptedMessage, d, n);
        System.out.println("Decrypted message: " + decryptedMessage);
    }

    
    static int modExp(int base, int exp, int mod) {
        int result = 1;
        base = base % mod;
        while (exp > 0) {
            if (exp % 2 == 1) {
                result = (result * base) % mod;
            }
            exp = exp >> 1;
            base = (base * base) % mod;
        }
        return result;
    }

    public static void main(String[] args) {
        int p, q, message;

        Scanner sc = new Scanner(System.in);

        System.out.println("Enter the 1st prime number:");
        p = sc.nextInt();
        System.out.println("Enter the 2nd prime number:");
        q = sc.nextInt();

       
        System.out.println("Enter the message (int):");
        message = sc.nextInt();

        RSA_encrypt(p, q, message);
    }
}
