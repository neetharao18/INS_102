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
import java.util.Base64;

public class Client {

    public static int readInt(InputStream is) throws IOException {
        byte[] intBytes = new byte[4];
        int bytesRead = is.read(intBytes);
        if (bytesRead != 4) {
            throw new IOException("Failed to read integer from stream");
        }
        return ByteBuffer.wrap(intBytes).getInt();
    }

    public static byte[] readBytes(InputStream is, int length) throws IOException {
        byte[] data = new byte[length];
        int totalBytesRead = 0;
        while (totalBytesRead < length) {
            int bytesRead = is.read(data, totalBytesRead, length - totalBytesRead);
            if (bytesRead == -1) {
                throw new IOException("End of stream reached before reading all bytes");
            }
            totalBytesRead += bytesRead;
        }
        return data;
    }

    public static boolean verifySignature(byte[] data, byte[] signatureBytes, PublicKey publicKey) throws Exception {
        try {
            Signature sig = Signature.getInstance("SHA256withRSA");
            sig.initVerify(publicKey);
            sig.update(data);
            return sig.verify(signatureBytes);
        } catch (Exception e) {
            System.err.println("Error during signature verification: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private static byte[] decodeBase64(String base64Str) {
        base64Str = base64Str.trim();
        int paddingCount = base64Str.length() % 4;
        if (paddingCount > 0) {
            base64Str += "=".repeat(4 - paddingCount);
        }
        return Base64.getDecoder().decode(base64Str);
    }

    private static PrivateKey getPrivateKeyFromDatabase() throws Exception {
        String dbUrl = "jdbc:mysql://localhost:3306/securedatabase";
        String dbUsername = "root";
        String dbPassword = "Insdata1827#!!#";

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
            stmt = conn.createStatement();
            String query = "SELECT private_key FROM rsa_keys WHERE user_id = 1";
            rs = stmt.executeQuery(query);

            if (rs.next()) {
                String privateKeyBase64 = rs.getString("private_key");
                byte[] privateKeyBytes = decodeBase64(privateKeyBase64);

                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
                return keyFactory.generatePrivate(keySpec);
            } else {
                throw new Exception("Private key not found in the database.");
            }
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.println("Connecting to server...");
            Socket socket = new Socket("localhost", 8080);
            System.out.println("Connected to server...");

            InputStream is = socket.getInputStream();

            int publicKeyLength = readInt(is);
            byte[] publicKeyBytes = readBytes(is, publicKeyLength);

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
            PublicKey serverPublicKey = keyFactory.generatePublic(keySpec);
            System.out.println("Received server's public key");

            int encryptedAESKeyLength = readInt(is);
            byte[] encryptedAESKeyBytes = readBytes(is, encryptedAESKeyLength);
            String encryptedAESKey = new String(encryptedAESKeyBytes);
            System.out.println("Received encrypted AES key");

            int encryptedMessageLength = readInt(is);
            byte[] encryptedMessageBytes = readBytes(is, encryptedMessageLength);
            String encryptedMessage = new String(encryptedMessageBytes);
            System.out.println("Received encrypted message");

            int digitalSignatureLength = readInt(is);
            byte[] digitalSignatureBytes = readBytes(is, digitalSignatureLength);
            String receivedSignature = new String(digitalSignatureBytes);
            System.out.println("Received digital signature from server");

            byte[] rawSignatureBytes = decodeBase64(receivedSignature);

            socket.close();

            System.out.println("\nDigital signature is successfully received from server (Base64 encoded)");
            //System.out.println(receivedSignature);

            System.out.println("\nPlease enter the digital signature received from the server (Base64 encoded):");
            String userProvidedSignature = scanner.nextLine();
            byte[] userSigBytes = decodeBase64(userProvidedSignature);

            System.out.println("Verifying signature...");

            boolean verificationResult = verifySignature(encryptedMessageBytes, rawSignatureBytes, serverPublicKey);
            System.out.println("Digital signature verification: " + 
                               (verificationResult ? "✓ Success" : "✗ Failed"));

            if (!verificationResult) {
                System.out.println("\n❌ The signature is invalid, it is tampered! 🚨🚨🚨");
                System.out.println("⚠️ The integrity of the message has been compromised!");
            }

            if (!Arrays.equals(rawSignatureBytes, userSigBytes)) {
                System.out.println("\n❌ The signature has been modified! 🚨🚨🚨");
                System.out.println("⚠️ The received signature and the provided signature do not match.");
                System.out.println("The integrity of the message has been compromised!");
            } else {
                System.out.println("\n✓ The provided signature matches the server's signature.");
            }

            // ========== DECRYPTION ONLY IF SIGNATURE IS VALID ==========
            if (verificationResult && Arrays.equals(rawSignatureBytes, userSigBytes)) {
                PrivateKey clientPrivateKey = getPrivateKeyFromDatabase();

                Cipher rsaCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
                rsaCipher.init(Cipher.DECRYPT_MODE, clientPrivateKey);
                byte[] aesKeyBytes = rsaCipher.doFinal(Base64.getDecoder().decode(encryptedAESKey));
                SecretKey originalAESKey = new SecretKeySpec(aesKeyBytes, 0, aesKeyBytes.length, "AES");

                byte[] ivAndEncrypted = Base64.getDecoder().decode(encryptedMessage);
                byte[] iv = Arrays.copyOfRange(ivAndEncrypted, 0, 16);
                byte[] actualEncryptedMessage = Arrays.copyOfRange(ivAndEncrypted, 16, ivAndEncrypted.length);

                Cipher aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                aesCipher.init(Cipher.DECRYPT_MODE, originalAESKey, new IvParameterSpec(iv));
                byte[] decryptedBytes = aesCipher.doFinal(actualEncryptedMessage);
                String decryptedMessage = new String(decryptedBytes);

                System.out.println("\n🔓 Decrypted message from server:");
                System.out.println(decryptedMessage);
            } else {
                System.out.println("\n🔒 Message decryption skipped due to failed signature verification.");
            }

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }
}
