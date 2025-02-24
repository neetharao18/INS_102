# Hill Cipher Implementation in Java

This repository contains an implementation of the *Hill Cipher* encryption algorithm in Java.

## About Hill Cipher
The *Hill Cipher* is a polygraphic substitution cipher based on linear algebra. It encrypts plaintext by multiplying it with a key matrix.

## Features
- Uses a 3x3 key matrix for encryption.
- Converts plaintext into numerical vectors for processing.
- Encrypts text using matrix multiplication and modular arithmetic.

## How the Code Works
1. The program generates a key matrix from the user-provided key.
2. It processes the plaintext into numerical vectors.
3. The key matrix is multiplied with the plaintext vector to produce the ciphertext.
4. The resulting ciphertext is displayed.

## Code Structure
- generate_key(String key, int key_matrix[][]): Converts the key into a 3x3 matrix.
- generate_cipher(int message_vector[][], int cipher_matrix[][], int key_matrix[][]): Performs matrix multiplication to produce the cipher text.
- final_matrix(String plaintext, String key): Manages encryption by generating required matrices and computing ciphertext.
- main(): Handles user input and calls the encryption function.

## Usage
### Running the Program
To compile and run the program:
sh
javac hill_cipher.java
java hill_cipher


### Example Input/Output

Enter the plain text: ACT
Enter the key: GYBNQKURP
Ciphertext: POH


## Notes
- The key must be a 9-character string to form a 3x3 matrix.
- The plaintext is padded with 'X' if its length is not a multiple of 3.
- Only uppercase letters are used in this implementation.

## License
This project is open-source and available under the MIT License.
