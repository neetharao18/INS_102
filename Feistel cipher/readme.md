# Feistel Cipher Implementation in Java

This repository contains an implementation of a *Feistel-based Cipher* encryption algorithm in Java.

## About Feistel Cipher
The *Feistel Cipher* is a symmetric encryption technique that splits the plaintext into two halves and applies multiple rounds of processing using a subkey. While the traditional Feistel network involves multiple rounds, this implementation provides a simplified approach using XOR operations.

## Features
- Converts plaintext to binary for processing.
- Uses a key to perform XOR-based encryption and decryption.
- Repeats the key to match the input length for secure processing.
- Encrypts and decrypts text symmetrically.

## How the Code Works
1. The user inputs plaintext and a key.
2. The plaintext and key are converted to binary.
3. The key is repeated to match the plaintext length.
4. XOR operation is performed for encryption.
5. The encrypted text is then decrypted using the same XOR process.
6. The decrypted binary is converted back to text.

## Code Structure
- stringToBinary(String str): Converts a string to its binary representation.
- binaryToString(String binary): Converts binary back to a string.
- encrypt(String input, String key): Encrypts the input string using XOR.
- decrypt(String cipher, String key): Decrypts the encrypted string back to plaintext.
- main(): Handles user input, encryption, and decryption processes.

## Usage
### Running the Program
To compile and run the program:
sh
javac feistel.java
java feistel


### Example Input/Output

Enter a string: HELLO
Enter a key: SECRET
Cipher: 101010...
Decrypted: HELLO


## Notes
- The key length should be adjusted to ensure proper encryption.
- The encryption method uses XOR, making it simple yet effective for basic cryptographic applications.
- This implementation is a basic demonstration and does not use multiple rounds like a full Feistel network.

## License
This project is open-source and available under the MIT License.
