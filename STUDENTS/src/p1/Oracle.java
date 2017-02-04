package p1;

import common.Config;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.security.Security;

import static common.Utils.*;

@SuppressWarnings({"UnnecessaryLocalVariable", "SameParameterValue", "WeakerAccess"})
public class Oracle {

    private static Logger logger = LoggerFactory.getLogger(Oracle.class);

    final private byte[] SuffixBytes;

    private Cipher cipher;
    private static String ALGO = Config.ALGO;
    private static int BlockSize = getAlgoBlockSize(ALGO);

    public Oracle() {
        this(Config.p1Key, Config.p1Suffix);
    }

    private void init(byte[] keyBytes) {
        Security.addProvider(new BouncyCastleProvider());
        SecretKey KEY;
        try {
            if (isSizeLegal(keyBytes, ALGO)) {
                KEY = new SecretKeySpec(keyBytes, ALGO);
            } else {
                throw new RuntimeException(String.format("illegal ALGO %s with key length %d", ALGO, keyBytes.length));
            }
            cipher = Cipher.getInstance(String.format("%s/ECB/NoPadding", ALGO));
            cipher.init(Cipher.ENCRYPT_MODE, KEY);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public Oracle(byte[] keyBytes, byte[] suffixBytes) {
        SuffixBytes = suffixBytes;
        init(keyBytes);
    }

    public Oracle(String keyString, String suffixString) {
        byte[] keyBytes = keyString.getBytes();
        SuffixBytes = suffixString.getBytes();
        init(keyBytes);
    }

    public byte[] compose(String plainText) {
        return compose(plainText.getBytes());
    }

    public byte[] compose(byte[] bytes) {
        int byteLength = bytes.length;
        if (byteLength > BlockSize) {
            logger.info("input length {} > {}", byteLength, BlockSize);
        }

        byte[] inputBytes = concat(bytes, SuffixBytes);
        byte[] finalBytes = paddingBytes(inputBytes, BlockSize);
        byte[] cipherBytes = new byte[0];
        try {
            cipherBytes = cipher.doFinal(finalBytes);
            logger.info("input:{}\tcipher:{}", finalBytes, cipherBytes);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return cipherBytes;
    }

}
