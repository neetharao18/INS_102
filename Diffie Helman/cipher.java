import java.util.Scanner;

public class diffie_helman{
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Input values
        System.out.print("Enter a prime number: ");
        int q = scanner.nextInt();

        System.out.print("Enter a primitive root: ");
        int a = scanner.nextInt();

        System.out.print("Enter the private key of A: ");
        int Xa = scanner.nextInt();

        System.out.print("Enter the private key of B: ");
        int Xb = scanner.nextInt();

        // Calculate public keys
        double Ya = Math.pow(a, Xa) % q;
        double Yb = Math.pow(a, Xb) % q;

        System.out.println("Public key of A: " + (float)Ya);
        System.out.println("Public key of B: " + (float)Yb);

        
        double Ka = Math.pow(Yb, Xa) % q;
        double Kb = Math.pow(Ya, Xb) % q;

        System.out.println("Shared key for A: " + (float)Ka);
        System.out.println("Shared key for B: " + (float)Kb);

        scanner.close();
    }
}
