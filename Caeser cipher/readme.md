# Caesar Cipher Implementation in Java

This repository contains a simple implementation of the *Caesar Cipher* encryption and decryption algorithm in Java.

## About Caesar Cipher
The *Caesar Cipher* is a basic encryption technique where each letter in the plaintext is shifted by a fixed number of positions in the alphabet. For example, with a shift of 3:
- A → D
- B → E
- C → F

The same shift is applied to decrypt the message, but in the opposite direction.

## Features
- Encrypts text using a user-defined shift value.
- Decrypts the encrypted text back to its original form.
- Preserves spaces and non-alphabetic characters.

## How the Code Works
1. The user inputs the text to encrypt.
2. The user specifies a shift value.
3. The program encrypts the text using the Caesar Cipher algorithm.
4. The encrypted text is displayed.
5. The program then decrypts the text and prints the original message.

## Code Structure
- encrypt(String text, int s): Encrypts the given text by shifting characters forward.
- decrypt(String text, int s): Decrypts the given text by shifting characters backward.
- main(): Handles user input and calls the encryption and decryption functions.

## Usage
### Running the Program
To compile and run the program:
sh
javac Caeser_cipher.java
java Caeser_cipher


### Example Input/Output

Enter the text to encrypt:
hello
Enter the shift value:
3
Encrypted text: khoor
decrypted message is hello


## Notes
- The shift value should be between 0 and 25.
- Only alphabetic characters are shifted; other characters remain unchanged.

## License
This project is open-source and available under the MIT License.
