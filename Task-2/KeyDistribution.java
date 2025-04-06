// KeyDistributionSystemWithPKIAndDH.java

import java.security.*;
import java.security.cert.X509Certificate;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.util.*;
import java.io.*;
import java.math.BigInteger;
import java.util.Base64;
import org.bouncycastle.cert.*;
import org.bouncycastle.jce.provider.*;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;

public class KeyDistributionSystemWithPKIAndDH {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    private static final String CRL_FILE = "revoked.crl";

    private static Map<String, KeyPair> userKeys = new HashMap<>();
    private static Map<String, X509Certificate> userCertificates = new HashMap<>();

    public static KeyPair generateRSAKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }

    public static X509Certificate generateSelfSignedCertificate(KeyPair keyPair, String commonName) throws Exception {
        long now = System.currentTimeMillis();
        Date startDate = new Date(now);
        Date endDate = new Date(now + 365L * 24 * 60 * 60 * 1000);

        X500Name name = new X500Name("CN=" + commonName + ", O=KDS, C=US");

        ContentSigner signer = new JcaContentSignerBuilder("SHA256withRSA").build(keyPair.getPrivate());
        SubjectPublicKeyInfo publicKeyInfo = SubjectPublicKeyInfo.getInstance(keyPair.getPublic().getEncoded());

        X509v3CertificateBuilder builder = new X509v3CertificateBuilder(
                name,
                BigInteger.valueOf(now),
                startDate,
                endDate,
                name,
                publicKeyInfo);

        return new JcaX509CertificateConverter().getCertificate(builder.build(signer));
    }

    public static void storeKeyPair(KeyPair keyPair, String userId) throws IOException {
        try (ObjectOutputStream privateOut = new ObjectOutputStream(new FileOutputStream(userId + "_private.key"))) {
            privateOut.writeObject(keyPair.getPrivate());
        }
        try (ObjectOutputStream publicOut = new ObjectOutputStream(new FileOutputStream(userId + "_public.key"))) {
            publicOut.writeObject(keyPair.getPublic());
        }
    }

    public static SecretKey generateAESKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128);
        return keyGenerator.generateKey();
    }

    public static KeyPair generateDHKeyPair() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("DH");
        generator.initialize(2048);
        return generator.generateKeyPair();
    }

    public static SecretKey deriveSharedSecret(KeyPair senderPair, PublicKey receiverPublic) throws Exception {
        KeyAgreement agreement = KeyAgreement.getInstance("DH");
        agreement.init(senderPair.getPrivate());
        agreement.doPhase(receiverPublic, true);
        byte[] shared = agreement.generateSecret();
        return new SecretKeySpec(shared, 0, 16, "AES");
    }

    public static boolean verifyCertificate(X509Certificate certificate, PublicKey publicKey) {
        try {
            certificate.verify(publicKey);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isRevoked(X509Certificate certificate) throws IOException {
        String serial = certificate.getSerialNumber().toString();
        try (BufferedReader reader = new BufferedReader(new FileReader(CRL_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.equals(serial)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void revokeCertificate(X509Certificate certificate) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CRL_FILE, true))) {
            writer.write(certificate.getSerialNumber().toString());
            writer.newLine();
        }
    }

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- Key Distribution System ---");
            System.out.println("1. Generate RSA Key Pair and Certificate");
            System.out.println("2. Generate AES Key");
            System.out.println("3. Perform DH Key Exchange");
            System.out.println("4. Verify Certificate");
            System.out.println("5. Revoke Certificate");
            System.out.println("6. Exit");
            System.out.print("Enter choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter your name: ");
                    String user = scanner.nextLine();
                    System.out.print("Enter recipient name: ");
                    String recipient = scanner.nextLine();

                    KeyPair userPair = generateRSAKeyPair();
                    KeyPair recipientPair = generateRSAKeyPair();
                    userKeys.put(user, userPair);
                    userKeys.put(recipient, recipientPair);

                    storeKeyPair(userPair, user);
                    storeKeyPair(recipientPair, recipient);

                    X509Certificate userCert = generateSelfSignedCertificate(userPair, user);
                    X509Certificate recipientCert = generateSelfSignedCertificate(recipientPair, recipient);
                    userCertificates.put(user, userCert);
                    userCertificates.put(recipient, recipientCert);

                    System.out.println("\nCertificates generated for " + user + " and " + recipient);
                    break;

                case 2:
                    System.out.print("Enter user name: ");
                    String aesUser = scanner.nextLine();
                    SecretKey aesKey = generateAESKey();
                    System.out.println("AES Key for " + aesUser + ": " + Base64.getEncoder().encodeToString(aesKey.getEncoded()));
                    break;

                case 3:
                    System.out.print("Enter your name: ");
                    String dhUser = scanner.nextLine();
                    System.out.print("Enter recipient name: ");
                    String dhRecipient = scanner.nextLine();

                    KeyPair dhUserPair = generateDHKeyPair();
                    KeyPair dhRecipientPair = generateDHKeyPair();

                    SecretKey sharedKey = deriveSharedSecret(dhUserPair, dhRecipientPair.getPublic());
                    System.out.println("Shared secret derived (AES key): " + Base64.getEncoder().encodeToString(sharedKey.getEncoded()));
                    break;

                case 4:
                    System.out.print("Enter user name to verify certificate: ");
                    String verifyUser = scanner.nextLine();
                    X509Certificate certToVerify = userCertificates.get(verifyUser);
                    KeyPair kp = userKeys.get(verifyUser);
                    if (certToVerify != null && kp != null) {
                        if (verifyCertificate(certToVerify, kp.getPublic())) {
                            System.out.println("Certificate verified successfully.");
                        } else {
                            System.out.println("Certificate verification failed.");
                        }
                    } else {
                        System.out.println("Certificate or key not found.");
                    }
                    break;

                case 5:
                    System.out.print("Enter user name to revoke certificate: ");
                    String revokeUser = scanner.nextLine();
                    X509Certificate certToRevoke = userCertificates.get(revokeUser);
                    if (certToRevoke != null) {
                        revokeCertificate(certToRevoke);
                        System.out.println("Certificate revoked.");
                    } else {
                        System.out.println("Certificate not found.");
                    }
                    break;

                case 6:
                    scanner.close();
                    System.out.println("Exiting system.");
                    System.exit(0);

                default:
                    System.out.println("Invalid choice.");
                    break;
            }
        }
    }
}
