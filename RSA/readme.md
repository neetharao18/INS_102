# RSA

This repository contains a Java program that implements the RSA encryption and decryption algorithm.

## About RSA
RSA (Rivest-Shamir-Adleman) is a widely used public-key cryptographic algorithm. It enables secure data transmission through key pair generation, encryption, and decryption.

## Features
- Generates public and private keys using two prime numbers.
- Encrypts a message using the public key.
- Decrypts the message using the private key.

## How the Code Works
1. The user inputs two prime numbers.
2. The program calculates n = p * q and phi = (p-1) * (q-1).
3. It selects a public exponent e such that 1 < e < phi and gcd(e, phi) = 1.
4. It calculates the private exponent d such that (e * d) % phi = 1.
5. The user provides a message (integer) to encrypt.
6. The message is encrypted using c = (message^e) % n.
7. The encrypted message is decrypted using m = (c^d) % n.

## Code Structure
- gcd(int a, int b): Computes the greatest common divisor.
- RSA_encrypt(int p, int q, int message): Handles encryption and decryption.
- modExp(int base, int exp, int mod): Performs modular exponentiation.
- main(): Handles user input and calls the encryption/decryption functions.

## Usage
### Running the Program
To compile and run the program:
sh
javac RSA.java
java RSA


### Example Input/Output

Enter the 1st prime number:
17
Enter the 2nd prime number:
19
Enter the message (int):
12
Encrypted message: 247
Decrypted message: 12


## Notes
- The prime numbers should be large for better security.
- The message should be an integer smaller than n.

## License
This project is open-source and available under the MIT License.
