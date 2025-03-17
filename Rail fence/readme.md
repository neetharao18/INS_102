# Rail Fence Cipher

This repository contains a Java implementation of the Rail Fence Cipher encryption and decryption algorithm.

## About Rail Fence Cipher
The Rail Fence Cipher is a transposition cipher that rearranges characters in a zigzag pattern across multiple rows, making it more difficult to decipher without the correct depth.

## Features
- Encrypts text using a user-defined depth.
- Decrypts the encrypted text back to its original form.
- Supports any string input.

## How the Code Works
1. The user inputs:
   - A plaintext message.
   - A depth value representing the number of rails.
2. The program encrypts the message by arranging characters in a zigzag pattern and then reading row-wise.
3. The program decrypts the message by reconstructing the zigzag pattern and reading in the original order.

## Code Structure
- railFenceEncrypt(String plaintext, int depth): Encrypts the given text using the Rail Fence Cipher algorithm.
- railFenceDecrypt(String cipherText, int depth): Decrypts the given text by reversing the encryption process.
- main(): Handles user input, performs encryption and decryption, and prints the results.

## Usage
### Running the Program
To compile and run the program:
sh
javac RailFenceCipher.java
java RailFenceCipher


### Example Input/Output

Enter the plaintext: HELLO
Enter the depth: 3
Encrypted: HOELL
Decrypted: HELLO


## Notes
- The depth value determines how many rows are used in the zigzag pattern.
- A depth of 1 results in no encryption (the text remains unchanged).

## License
This project is open-source and available under the MIT License.
