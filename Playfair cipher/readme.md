# Playfair Cipher Implementation in Java

This repository contains an implementation of the *Playfair Cipher* encryption algorithm in Java.

## About Playfair Cipher
The *Playfair Cipher* is a digraph substitution cipher that encrypts pairs of letters using a 5x5 key matrix.

## Features
- Generates a 5x5 key matrix from a given keyword.
- Encrypts plaintext by processing letter pairs.
- Decrypts ciphertext back to the original plaintext.
- Handles cases where plaintext has an odd number of characters by appending 'x'.

## How the Code Works
1. The user provides a keyword to generate the 5x5 key matrix.
2. The plaintext is modified by replacing 'j' with 'i' and padding with 'x' if needed.
3. Each pair of letters is encrypted according to the Playfair cipher rules.
4. Decryption reverses the process to retrieve the original message.

## Code Structure
- input_matrix(String key): Generates a 5x5 key matrix.
- contains(char[] store_key, char c): Checks if a character is already in the key matrix.
- generate_cipher(String plaintext, String keyword): Encrypts plaintext using the key matrix.
- find_pos(char[][] matrix, char c): Finds the position of a character in the key matrix.
- decrypt_cipher(String ciphertext, String keyword): Decrypts the ciphertext back to plaintext.
- main(): Runs the encryption and decryption process.

## Usage
### Running the Program
To compile and run the program:
sh
javac playfair.java
java playfair


### Example Input/Output

Plaintext: instruments
Ciphertext: gatlmzclrqtx
Decrypted text: instrumentsx


## Notes
- The letter 'j' is replaced with 'i' to fit in the 5x5 matrix.
- If the plaintext has an odd number of characters, an 'x' is appended.
- Playfair cipher provides better security than simple substitution ciphers but is still vulnerable to cryptanalysis.

## License
This project is open-source and available under the MIT License.
