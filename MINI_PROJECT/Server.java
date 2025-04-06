import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.*;
import java.util.*;
import java.io.*;
import java.net.*;
import java.security.spec.X509EncodedKeySpec;
import java.nio.ByteBuffer;
import java.sql.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.KeyFactory;
import java.security.PrivateKey;

public class Server {

    // Generate RSA KeyPair (Public & Private)
    public static KeyPair generateRSAKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048); // 2048-bit key size for RSA
        return keyPairGenerator.generateKeyPair();
    }

    // Utility method to convert integer to byte array (4 bytes)
    public static byte[] intToBytes(int value) {
        return ByteBuffer.allocate(4).putInt(value).array();
    }

    // Generate AES key for encryption
    public static SecretKey generateAESKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256); // AES-256
        return keyGenerator.generateKey();
    }

    // Encrypt data using AES key
    public static String encryptData(String data, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        // Generate a random IV for each encryption to enhance security
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
        byte[] encrypted = cipher.doFinal(data.getBytes());

        // Send the IV along with the encrypted data (IV + Encrypted Message)
        byte[] ivWithEncryptedMessage = new byte[iv.length + encrypted.length];
        System.arraycopy(iv, 0, ivWithEncryptedMessage, 0, iv.length);
        System.arraycopy(encrypted, 0, ivWithEncryptedMessage, iv.length, encrypted.length);

        return Base64.getEncoder().encodeToString(ivWithEncryptedMessage);
    }

    // Encrypt AES key using RSA public key
    public static String encryptKeyWithRSA(SecretKey aesKey, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedKey = cipher.doFinal(aesKey.getEncoded());
        return Base64.getEncoder().encodeToString(encryptedKey);
    }

    // Method to create a digital signature using private key
    public static byte[] signDataRaw(String data, PrivateKey privateKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(data.getBytes());
        return signature.sign();
    }

    // Method to create a digital signature and encode it as Base64
    public static String signData(String data, PrivateKey privateKey) throws Exception {
        byte[] signatureBytes = signDataRaw(data, privateKey);
        return Base64.getEncoder().encodeToString(signatureBytes);
    }

    // Verify signature method
    public static boolean verifySignature(String data, byte[] signatureBytes, PublicKey publicKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(publicKey);
        signature.update(data.getBytes());
        return signature.verify(signatureBytes);
    }

    // Modified: Store RSA keys in the database or update if user_id exists
    public static void storeRSAKeysInDB(Connection conn, int userId, PublicKey publicKey, PrivateKey privateKey) throws SQLException {
        String insertSQL = "INSERT INTO rsa_keys (user_id, public_key, private_key) VALUES (?, ?, ?) " +
                           "ON DUPLICATE KEY UPDATE public_key = VALUES(public_key), private_key = VALUES(private_key)";
        try (PreparedStatement stmt = conn.prepareStatement(insertSQL)) {
            stmt.setInt(1, userId);
            stmt.setString(2, Base64.getEncoder().encodeToString(publicKey.getEncoded()));
            stmt.setString(3, Base64.getEncoder().encodeToString(privateKey.getEncoded()));
            stmt.executeUpdate();
        }
        System.out.println("RSA Key Pair successfully stored or updated in the database.");
    }

    // Store encrypted data in the database
    public static void storeEncryptedDataInDB(Connection conn, int userId, String encryptedKey, String encryptedMessage, String digitalSignature) throws SQLException {
        String insertSQL = "INSERT INTO encrypted_messages (user_id, encrypted_key, encrypted_message, digital_signature) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(insertSQL)) {
            stmt.setInt(1, userId);
            stmt.setString(2, encryptedKey);
            stmt.setString(3, encryptedMessage);
            stmt.setString(4, digitalSignature);
            stmt.executeUpdate();
        }
        System.out.println("Encrypted data successfully stored in the database.");
    }

    // Send byte[] with a 4-byte length header
    public static void sendData(OutputStream os, byte[] data) throws IOException {
        byte[] lengthBytes = ByteBuffer.allocate(4).putInt(data.length).array();
        os.write(lengthBytes);
        os.write(data);
    }

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        // Database connection details
        String dbUrl = "jdbc:mysql://localhost:3306/securedatabase";
        String dbUser = "root";
        String dbPassword = "Insdata1827#!!#";

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            // Generate RSA key pair (public and private keys)
            KeyPair keyPair = generateRSAKeyPair();
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            System.out.println("RSA key pair generated successfully.");

            // Generate AES key for encryption
            SecretKey aesKey = generateAESKey();
            System.out.println("AES key successfully generated.");

            // Get the message from the user
            System.out.print("Enter message to send: ");
            String message = scanner.nextLine();

            // Encrypt the message using AES
            String encryptedMessage = encryptData(message, aesKey);
            System.out.println("Message encrypted successfully.");

            // Encrypt the AES key using server's public key
            String encryptedAESKey = encryptKeyWithRSA(aesKey, publicKey);
            System.out.println("AES key encrypted with RSA successfully.");

            // Sign the encrypted message with the server's private RSA key
            byte[] signatureBytes = signDataRaw(encryptedMessage, privateKey);
            String digitalSignature = Base64.getEncoder().encodeToString(signatureBytes);

            // Verify our own signature - sanity check
            boolean verificationResult = verifySignature(encryptedMessage, signatureBytes, publicKey);
            System.out.println("Self-verification of signature: " + (verificationResult ? "Successful" : "Failed"));

            // Format and display the digital signature
            System.out.println("\n========== DIGITAL SIGNATURE (COPY THIS) ==========");
            System.out.println(digitalSignature);
            System.out.println("===================================================\n");

            // Store data in the database
            int userId = 1; // or dynamically set for each user
            storeRSAKeysInDB(conn, userId, publicKey, privateKey);
            storeEncryptedDataInDB(conn, userId, encryptedAESKey, encryptedMessage, digitalSignature);

            System.out.println("All data stored successfully in the database.");

            // ===== START TCP SOCKET COMMUNICATION =====
            try (ServerSocket serverSocket = new ServerSocket(8080)) {
                System.out.println("Waiting for client to connect on port 8080...");
                Socket socket = serverSocket.accept();
                System.out.println("Client connected!");

                OutputStream os = socket.getOutputStream();

                // Send public key
                sendData(os, publicKey.getEncoded());

                // Send encrypted AES key
                sendData(os, encryptedAESKey.getBytes());

                // Send encrypted message
                sendData(os, encryptedMessage.getBytes());

                // Send digital signature
                sendData(os, digitalSignature.getBytes());

                System.out.println("Data successfully sent to the client.");
                socket.close();
            } catch (IOException e) {
                System.err.println("Error during socket communication: " + e.getMessage());
            }
            // ===== END TCP SOCKET COMMUNICATION =====

        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}
