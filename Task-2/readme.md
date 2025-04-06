Here’s a **README** you can use for your GitHub repository. It includes a clear project description, features, requirements, how to run, and more.

---

#Key Distribution System with RSA, Diffie-Hellman, AES, and PKI (Bouncy Castle)

This project is a **Java-based secure key distribution system** that demonstrates the use of **RSA key pairs**, **AES symmetric encryption**, **Diffie-Hellman (DH) key exchange**, and **Public Key Infrastructure (PKI)** with **certificate generation, verification, and revocation** using **Bouncy Castle**.

---

## Features

- RSA key pair generation (2048-bit)
- X.509 self-signed certificate creation
- Certificate verification and revocation (CRL-based)
- AES key generation (128-bit)
- Secure shared key derivation via Diffie-Hellman
- Key and certificate storage using files
- Built with Bouncy Castle for cryptographic operations

---

## Project Structure

```
KeyDistributionSystemWithPKIAndDH.java   // Main Java class
revoked.crl                              // Revoked certificate list (text file)
username_private.key / username_public.key // Serialized RSA key files
```

---

## Requirements

- Java 8 or above
- [Bouncy Castle Provider](https://www.bouncycastle.org/latest_releases.html)

### Add Bouncy Castle

Download the JAR (`bcprov-jdk15on-*.jar`) and add it to your project’s classpath.

Or if you're using Maven, add:

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
javac KeyDistributionSystemWithPKIAndDH.java
```

2. **Run the program**

```bash
java KeyDistributionSystemWithPKIAndDH
```

3. **Follow the menu options:**

```
1. Generate RSA Key Pair and Certificate
2. Generate AES Key
3. Generate Diffie-Hellman Key Pair and Exchange
4. Verify Certificate
5. Revoke Certificate
6. Exit
```

---

## Sample Use Cases

- Generate and exchange public keys and certificates between two users.
- Derive a shared AES key using DH for encrypted communication.
- Verify and revoke certificates using a local Certificate Revocation List (CRL).

---

## Notes

- Certificates are self-signed and not from a trusted Certificate Authority (CA).
- This implementation is for **educational and demonstration purposes**.
- A real-world secure system would require CA integration, secure storage, and more robust revocation mechanisms (like OCSP).

---

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

---

## Contributing

Feel free to fork, improve, and submit pull requests! 🚀  
Want to add database storage or digital signature validation? Let's build it together!

---

Let me know if you want a version with MySQL or GUI support too!
