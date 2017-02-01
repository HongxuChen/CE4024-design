import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

// java.util.Random can not generate 64 bit random values since the seed used is 48 bits Therefore we need SecureRandom

public class Alice {

    private SecretKey sharedKey;
    private String secretMessage;

    private static Cipher ecipher;
    private static Cipher dcipher;
    private byte[] random64bit;

    private SecretKey sessionKey;
    private Cipher sessionCipher;

    public Alice() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {

        // Secret key shared Alice and Bob.
        // This is just a sample key. Its exact value will be changed during the assessment of the project.
        // So DO NOT assume this will the actual key used in the assessment.
        sharedKey = new SecretKeySpec("ABCDEFGH".getBytes(), "DES");

        random64bit = new byte[8];
        ecipher = Cipher.getInstance("DES/ECB/NoPadding");
        dcipher = Cipher.getInstance("DES/ECB/NoPadding");

        // initialize the ciphers with the given key
        // these are the objects that stores the shared key in them to be used for later encryptions
        ecipher.init(Cipher.ENCRYPT_MODE, sharedKey);
        dcipher.init(Cipher.DECRYPT_MODE, sharedKey);

        // session key will be used for session cipher for encryptions of the secret message
        sessionCipher = Cipher.getInstance("DES");

        // The secret message always begins with the string '[SECRET]'.
        // The following is just a sample secret message for testing.
        // During the assessment this will be changed to something else.
        secretMessage = "[SECRET] Congratulations, you have solved Problem 3!";
    }

    public byte[] Step1() throws IllegalStateException, IllegalBlockSizeException, BadPaddingException {
        SecureRandom sr = new SecureRandom();
        sr.nextBytes(random64bit);

        return ecipher.doFinal(random64bit);
    }

    public String Step3(byte[] step2package) throws IllegalStateException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {

        //decrypting the received package
        byte[] decryptedPackage = dcipher.doFinal(step2package);

        //dividing the package into two
        byte[] randomValue = new byte[(decryptedPackage.length) / 2];
        byte[] XORedSessionKey = new byte[decryptedPackage.length / 2];
        System.arraycopy(decryptedPackage, 0, randomValue, 0, randomValue.length);
        System.arraycopy(decryptedPackage, randomValue.length, XORedSessionKey, 0, XORedSessionKey.length);

        //checking if the response is for the challenge Alice sent for
        if (!Arrays.equals(random64bit, randomValue)) {
            System.out.println("This is not the answer to my challenge, sorry!");
            return null;
        }

        //calculating random + 1
        byte[] swap = CommonFunctions.swap(random64bit);

        //extracting the session key
        byte[] s = CommonFunctions.XOR(XORedSessionKey, swap);

        //regenerating the session key
        sessionKey = new SecretKeySpec(s, 0, s.length, "DES");

        //encrypting the important message
        sessionCipher.init(Cipher.ENCRYPT_MODE, sessionKey);

        return CommonFunctions.encrypt(sessionCipher, secretMessage);
    }


}
