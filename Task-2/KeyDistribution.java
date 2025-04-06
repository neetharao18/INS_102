import java.security.*;
import java.security.cert.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.util.*;
import java.io.*;
import java.math.BigInteger;
import org.bouncycastle.cert.*;
import org.bouncycastle.x509.*;
import org.bouncycastle.jce.provider.*;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;

import javax.crypto.interfaces.DHPublicKey;

public class KeyDistributionSystemWithPKIAndDH {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    private static final String CRL_FILE = "revoked.crl";

    // Generate RSA key pair
    public static KeyPair generateRSAKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);  // 2048-bit RSA key pair
        return keyPairGenerator.generateKeyPair();
    }

    // Create a self-signed X.509 certificate for the RSA key pair
    public static X509Certificate generateSelfSignedCertificate(KeyPair keyPair, String commonName) throws Exception {
        long now = System.currentTimeMillis();
        Date startDate = new Date(now);
        Date endDate = new Date(now + 365 * 24 * 60 * 60 * 1000);  // Valid for 1 year

        X500Name issuer = new X500Name("CN=" + commonName + ", O=Key Distribution System, C=US");
        X500Name subject = issuer;

        ContentSigner contentSigner = new JcaContentSignerBuilder("SHA256withRSA").build(keyPair.getPrivate());

        SubjectPublicKeyInfo publicKeyInfo = SubjectPublicKeyInfo.getInstance(keyPair.getPublic().getEncoded());

        X509v3CertificateBuilder certBuilder = new X509v3CertificateBuilder(
                issuer,
                BigInteger.valueOf(now),
                startDate,
                endDate,
                subject,
                publicKeyInfo
        );

        X509Certificate certificate = new JcaX509CertificateConverter().getCertificate(certBuilder.build(contentSigner));
        return certificate;
    }

    // Store key pair into files
    public static void storeKeyPair(KeyPair keyPair, String keyId) throws IOException {
        try (ObjectOutputStream privateKeyOut = new ObjectOutputStream(new FileOutputStream(keyId + "_private.key"))) {
            privateKeyOut.writeObject(keyPair.getPrivate());
            System.out.println("Private key for " + keyId + " stored.");
        }

        try (ObjectOutputStream publicKeyOut = new ObjectOutputStream(new FileOutputStream(keyId + "_public.key"))) {
            publicKeyOut.writeObject(keyPair.getPublic());
            System.out.println("Public key for " + keyId + " stored.");
        }
    }

    // Generate AES Key (128-bit AES key)
    public static SecretKey generateAESKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128);  // 128-bit AES key
        return keyGenerator.generateKey();
    }

    // Perform Diffie-Hellman key exchange
    public static KeyPair generateDHKeyPair() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DH");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }

    // Derive a shared secret from the Diffie-Hellman exchange
    public static SecretKey deriveSharedSecret(KeyPair senderKeyPair, PublicKey recipientPublicKey) throws Exception {
        KeyAgreement keyAgreement = KeyAgreement.getInstance("DH");
        keyAgreement.init(senderKeyPair.getPrivate());
        keyAgreement.doPhase(recipientPublicKey, true);
        byte[] secret = keyAgreement.generateSecret();
        return new SecretKeySpec(secret, 0, 16, "AES"); // Using only the first 128 bits for AES key
    }

    // Verify certificate using public key
    public static boolean verifyCertificate(X509Certificate certificate, PublicKey publicKey) {
        try {
            certificate.verify(publicKey); // Verify the certificate with the public key
            return true; // Certificate is valid
        } catch (Exception e) {
            return false; // Certificate verification failed
        }
    }

    // Check if the certificate is revoked
    public static boolean isRevoked(X509Certificate certificate) throws IOException, CertificateEncodingException {
        try (BufferedReader reader = new BufferedReader(new FileReader(CRL_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.equals(Base64.getEncoder().encodeToString(certificate.getEncoded()))) {
                    return true;  // Certificate is revoked
                }
            }
        }
        return false;
    }

    // Revoke the certificate
    public static void revokeCertificate(X509Certificate certificate) throws IOException, CertificateEncodingException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CRL_FILE, true))) {
            writer.write(Base64.getEncoder().encodeToString(certificate.getEncoded()));
            writer.newLine();
            System.out.println("Certificate has been revoked.");
        }
    }

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        KeyPair aliceKeyPair = null;
        KeyPair bobKeyPair = null;
        X509Certificate aliceCertificate = null;
        X509Certificate bobCertificate = null;
        SecretKey aesKey = null;

        while (true) {
            // Display menu options
            System.out.println("\n--- Key Distribution System ---");
            System.out.println("1. Generate RSA Key Pair and Certificate");
            System.out.println("2. Generate AES Key");
            System.out.println("3. Generate Diffie-Hellman Key Pair and Exchange");
            System.out.println("4. Verify Certificate");
            System.out.println("5. Revoke Certificate");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 1: // Generate RSA Key Pair and Certificate
                    // Get user and recipient names
                    System.out.print("Enter your name (for key generation): ");
                    String userName = scanner.nextLine();
                    System.out.print("Enter recipient's name: ");
                    String recipientName = scanner.nextLine();

                    // Generate RSA Key Pair for User and Recipient
                    System.out.println("Generating RSA key pair for " + userName + "...");
                    aliceKeyPair = generateRSAKeyPair();
                    System.out.println("Generating RSA key pair for " + recipientName + "...");
                    bobKeyPair = generateRSAKeyPair();
                    System.out.println("RSA Key Pairs generated.");

                    // Store RSA keys to files using the user names
                    storeKeyPair(aliceKeyPair, userName);
                    storeKeyPair(bobKeyPair, recipientName);

                    // Generate self-signed certificates for User and Recipient
                    System.out.println("Generating self-signed certificate for " + userName + "...");
                    aliceCertificate = generateSelfSignedCertificate(aliceKeyPair, userName);
                    System.out.println(userName + "'s certificate has been successfully generated!");

                    System.out.println("Generating self-signed certificate for " + recipientName + "...");
                    bobCertificate = generateSelfSignedCertificate(bobKeyPair, recipientName);
                    System.out.println(recipientName + "'s certificate has been successfully generated!");

                    // Display RSA Keys
                    System.out.println("\nRSA Key Pair for " + userName + ":");
                    System.out.println("Public Key: " + Base64.getEncoder().encodeToString(aliceKeyPair.getPublic().getEncoded()));
                    System.out.println("Private Key: " + Base64.getEncoder().encodeToString(aliceKeyPair.getPrivate().getEncoded()));

                    System.out.println("\nRSA Key Pair for " + recipientName + ":");
                    System.out.println("Public Key: " + Base64.getEncoder().encodeToString(bobKeyPair.getPublic().getEncoded()));
                    System.out.println("Private Key: " + Base64.getEncoder().encodeToString(bobKeyPair.getPrivate().getEncoded()));
                    break;

                case 2: // Generate AES Key
                    // Get user name
                    System.out.print("Enter your name (for key generation): ");
                    String aesUserName = scanner.nextLine();

                    // Generate AES key for the user
                    System.out.println("Generating AES key for " + aesUserName + "...");
                    aesKey = generateAESKey();
                    System.out.println("AES Key generated for " + aesUserName + ": " + Base64.getEncoder().encodeToString(aesKey.getEncoded()));
                    break;

                case 3: // Generate Diffie-Hellman Key Pair and Exchange
                    // Get user and recipient names
                    System.out.print("Enter your name (for Diffie-Hellman): ");
                    String dhUserName = scanner.nextLine();
                    System.out.print("Enter recipient's name: ");
                    String dhRecipientName = scanner.nextLine();

                    System.out.println("Generating Diffie-Hellman key pair for " + dhUserName + "...");
                    aliceKeyPair = generateDHKeyPair();
                    System.out.println("Generating Diffie-Hellman key pair for " + dhRecipientName + "...");
                    bobKeyPair = generateDHKeyPair();

                    // Perform key exchange
                    SecretKey sharedSecret = deriveSharedSecret(aliceKeyPair, bobKeyPair.getPublic());
                    System.out.println("Shared secret derived (AES key) for " + dhUserName + " and " + dhRecipientName + ": " + Base64.getEncoder().encodeToString(sharedSecret.getEncoded()));
                    break;

                                case 4: // Verify Certificate
                    if (aliceCertificate == null || bobCertificate == null) {
                        System.out.println("Please generate keys and certificates first.");
                        break;
                    }

                    // Verify the certificates for Alice and Bob
                    System.out.println("Verifying " + aliceCertificate.getSubjectX500Principal().getName() + "'s certificate...");
                    // Example verification
                    if (verifyCertificate(aliceCertificate, aliceKeyPair.getPublic())) {
                        System.out.println("Certificate for " + aliceCertificate.getSubjectX500Principal().getName() + " verified successfully.");
                    } else {
                        System.out.println("Certificate verification for " + aliceCertificate.getSubjectX500Principal().getName() + " failed.");
                    }

                    System.out.println("Verifying " + bobCertificate.getSubjectX500Principal().getName() + "'s certificate...");
                    if (verifyCertificate(bobCertificate, bobKeyPair.getPublic())) {
                        System.out.println("Certificate for " + bobCertificate.getSubjectX500Principal().getName() + " verified successfully.");
                    } else {
                        System.out.println("Certificate verification for " + bobCertificate.getSubjectX500Principal().getName() + " failed.");
                    }
                    break;

                case 5: // Revoke Certificate
                    if (aliceCertificate == null || bobCertificate == null) {
                        System.out.println("Please generate keys and certificates first.");
                        break;
                    }

                    // Revoke Alice's certificate as an example
                    System.out.println("Revoking " + aliceCertificate.getSubjectX500Principal().getName() + "'s certificate...");
                    revokeCertificate(aliceCertificate);

                    // Check Alice's certificate again after revocation
                    if (isRevoked(aliceCertificate)) {
                        System.out.println("Certificate for " + aliceCertificate.getSubjectX500Principal().getName() + " is revoked.");
                    } else {
                        System.out.println("Certificate for " + aliceCertificate.getSubjectX500Principal().getName() + " is not revoked.");
                    }

                    break;

                case 6: // Exit
                    System.out.println("Exiting the system.");
                    scanner.close();
                    System.exit(0);

                default:
                    System.out.println("Invalid choice. Please select a valid option.");
                    break;
            }
        }
    }
}

