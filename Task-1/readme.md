Here you go — a **README.md** for your **Java Hybrid Encryption System** (with Vigenère, Columnar Transposition, and RSA) in the same format as your PKI-based system:

---

# Hybrid Encryption System (Java) - Vigenère, Columnar Transposition, RSA

This project is a **Java-based hybrid encryption system** combining classical and modern cryptographic techniques. It uses the **Vigenère Cipher** (substitution), **Columnar Transposition Cipher** (permutation), and **RSA** (asymmetric encryption) to encrypt both messages and keys for enhanced security.

---

## Features

- Vigenère Cipher for character-based substitution
- Columnar Transposition Cipher for rearranging characters
- RSA 2048-bit key generation (public/private)
- RSA-encrypted keys for secure exchange
- Manual input for plaintext and keys
- Console output for encrypted message and keys
- RSA-based key decryption for verification

---

## Project Structure

```
HybridEncryption.java                // Main Java file
bcprov-jdk15on-*.jar                 // Bouncy Castle provider (for RSA)
```

---

## Requirements

- Java 8 or above
- [Bouncy Castle Provider](https://www.bouncycastle.org/latest_releases.html)

### Add Bouncy Castle

Download the `bcprov-jdk15on-*.jar` and add it to your classpath.

Or add via Maven:

```xml
<dependency>
  <groupId>org.bouncycastle</groupId>
  <artifactId>bcprov-jdk15on</artifactId>
  <version>1.70</version>
</dependency>
```

---

## How to Run

1. **Compile the Java file**

```bash
javac -cp .:bcprov-jdk15on-*.jar HybridEncryption.java
```

2. **Run the program**

```bash
java -cp .:bcprov-jdk15on-*.jar HybridEncryption
```

3. **Enter input when prompted:**

```
Enter Plaintext: hello my name
Enter Vigenère Key: KEYFORCIPHER
Enter Transposition Key: COLUMNKEY
```

---

## Sample Output

```
Encrypted Text: XOSNVMNHLEEA

Encrypted Vigenère Key (RSA): MIIBIjANBgkq...
Encrypted Transposition Key (RSA): Q0xVSU1OS0VZ...

Decrypted Vigenère Key: KEYFORCIPHER
Decrypted Transposition Key: COLUMNKEY
```

---

## Use Cases

- Secure message encryption and transmission with hybrid methods
- Educational tool to demonstrate combination of classical and modern ciphers
- Foundation for more advanced secure communication systems

---

## Notes

- This is a **demonstration-level hybrid encryption system**.
- Currently supports **encryption and RSA key protection** — decryption of the message can be added as an extension.
- For real-world applications, integrate with certificate-based identity validation and secure channels.

---

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

---

## Contributing

Feel free to fork, improve, and submit pull requests!  
Want to add message decryption, GUI, or database storage? Let's build it together! 💻

---

Let me know if you'd like me to generate the `LICENSE`, sample screenshots, or GitHub badges for stars/forks/etc!
