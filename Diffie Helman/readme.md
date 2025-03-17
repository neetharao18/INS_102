# Diffie-Hellman

This repository contains a Java implementation of the Diffie-Hellman key exchange algorithm.

## About Diffie-Hellman
Diffie-Hellman is a key exchange algorithm that allows two parties to securely share a common secret key over an insecure communication channel.

## Features
- Uses a prime number and a primitive root for key exchange.
- Generates public keys for both users.
- Computes a shared secret key using private and public keys.

## How the Code Works
1. The user inputs:
   - A prime number (q).
   - A primitive root (a).
   - Private keys for two users (Xa for A, Xb for B).
2. The program calculates public keys:
   - Ya = (a^Xa) % q
   - Yb = (a^Xb) % q
3. The shared secret key is computed using:
   - Ka = (Yb^Xa) % q
   - Kb = (Ya^Xb) % q
4. The shared secret keys Ka and Kb should be identical.

## Code Structure
- main(): Handles user input, performs calculations, and prints the results.

## Usage
### Running the Program
To compile and run the program:
sh
javac diffie_helman.java
java diffie_helman


### Example Input/Output

Enter a prime number: 23
Enter a primitive root: 5
Enter the private key of A: 6
Enter the private key of B: 15
Public key of A: 8.0
Public key of B: 19.0
Shared key for A: 2.0
Shared key for B: 2.0


## Notes
- The prime number should be sufficiently large for security.
- The primitive root should be a valid generator for the chosen prime.
- The shared key is computed separately by both users and should match.

## License
This project is open-source and available under the MIT License.
