# Vigenère Cipher Implementation in Java

This repository contains an implementation of the *Vigenère Cipher* encryption and decryption algorithm in Java.

## About Vigenère Cipher
The *Vigenère Cipher* is a method of encrypting text by using a series of Caesar ciphers based on the letters of a keyword. It is a polyalphabetic substitution cipher that improves security over the simple Caesar Cipher.

## Features
- Generates a repeating key sequence to match the plaintext length.
- Encrypts text using the Vigenère Cipher algorithm.
- Decrypts the encrypted text back to its original form.

## How the Code Works
1. The program generates a key sequence that matches the length of the plaintext.
2. The plaintext is encrypted by shifting each letter based on the corresponding letter in the key.
3. The encrypted text is displayed.
4. The program then decrypts the ciphertext back to the original message.

## Code Structure
- generate_key(String plaintext, String key): Expands the key to match the length of the plaintext.
- cipherText(String plaintext, String key): Encrypts the given text using the Vigenère Cipher algorithm.
- originalText(String cipher_text, String key): Decrypts the given ciphertext back to plaintext.
- main(): Demonstrates encryption and decryption using sample text.

## Usage
### Running the Program
To compile and run the program:
sh
javac vigenere_cipher.java
java vigenere_cipher


### Example Input/Output

CipherText: GIEWIVrGMTLIVrGIEWIV
OriginalText: GEEKSFORGEEKS


## Notes
- Only uppercase letters are used in this implementation.
- The key should be provided in uppercase for proper functionality.
- The program assumes plaintext and key contain only uppercase alphabets.

## License
This project is open-source and available under the MIT License.
