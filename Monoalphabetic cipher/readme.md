# Monoalphabetic Cipher Implementation in Java

This repository contains an implementation of the *Monoalphabetic Cipher* encryption algorithm in Java.

## About Monoalphabetic Cipher
The *Monoalphabetic Cipher* is a simple substitution cipher where each letter in the plaintext is replaced with another letter based on a fixed key mapping.

## Features
- Encrypts plaintext using a predefined key mapping.
- Retains non-alphabetic characters unchanged.
- Provides basic encryption using character substitution.

## How the Code Works
1. The user inputs plaintext.
2. The program maps each lowercase letter to a corresponding letter from a predefined key.
3. Non-alphabetic characters remain unchanged.
4. The encrypted text is displayed as output.

## Code Structure
- char[] arr: The standard alphabet.
- char[] key: The substitution key for encryption.
- cipher_array[]: Stores the encrypted text.
- main(): Handles user input and encryption process.

## Usage
### Running the Program
To compile and run the program:
sh
javac monoalphabet.java
java monoalphabet


### Example Input/Output

Enter the plain text: hello
Ciphertext: itkkn


## Notes
- This implementation only supports lowercase letters.
- The key mapping is predefined and static.
- This is a basic form of substitution cipher, vulnerable to frequency analysis.

## License
This project is open-source and available under the MIT License.
