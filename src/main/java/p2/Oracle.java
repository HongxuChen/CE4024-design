package p2;

import common.Config;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.security.Security;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static common.Utils.*;

@SuppressWarnings("WeakerAccess")
public class Oracle {
    private static Logger logger = LoggerFactory.getLogger(Oracle.class);

    private Cipher cipher;
    final private byte[] MACKey;
    private static String ALGO = Config.DES;
    private static int BlockSize = getAlgoBlockSize(ALGO);

    private List<byte[]> initOList() {
        List<byte[]> oList = new ArrayList<byte[]>();
        byte[] zero = new byte[BlockSize];
        Arrays.fill(zero, (byte) 0);
        oList.add(zero);
        return oList;
    }

    private void init() {
        final byte[] keyBytes = MACKey;
        Security.addProvider(new BouncyCastleProvider());
        SecretKey KEY;
        try {
            if (isSizeLegal(keyBytes, ALGO)) {
                KEY = new SecretKeySpec(keyBytes, ALGO);
            } else {
                throw new RuntimeException(String.format("illegal ALGO %s with key length %d", ALGO, keyBytes.length));
            }
            cipher = Cipher.getInstance(String.format("%s/%s/NoPadding", ALGO, Config.MODE));
            cipher.init(Cipher.ENCRYPT_MODE, KEY);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public Oracle(byte[] macKey) {
        this.MACKey = macKey;
        init();
    }

    public Oracle() {
        this(Config.p2MacKey.getBytes());
        init();
    }

    private byte[] encryptOne(byte[] finalInput) {
        byte[] cipherBytes = {};
        try {
            cipherBytes = cipher.doFinal(finalInput);
            //            logger.info("cipherBytes.len={}, finalInput.len={}", cipherBytes.length, finalInput.length);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return cipherBytes;
    }

    private List<byte[]> splitMsgBytes(byte[] msg) {
        List<byte[]> msgList = new ArrayList<byte[]>();
        int msgLength = msg.length;
        int lastBlockLength = msgLength % BlockSize;
        int initBlocks = msgLength - lastBlockLength;
        for (int i = 0; i < initBlocks; i += BlockSize) {
            byte[] block = Arrays.copyOfRange(msg, i, i + BlockSize);
            msgList.add(block);
        }
        // if lastBlockLength=0, no-op
        if (lastBlockLength != 0) {
            byte[] lastBlock = Arrays.copyOfRange(msg, initBlocks, msgLength);
            byte[] lastPadded = paddingBytes(lastBlock, BlockSize);
            msgList.add(lastPadded);
        }
        //        logger.info("lastBlockLength={}, {} is split into len={}, ", lastBlockLength, ppBytes(msg), msgList.size());
        return msgList;
    }

    private List<byte[]> encrypt(byte[] msg) {
        List<byte[]> msgList = splitMsgBytes(msg);
        List<byte[]> oList = initOList();
        for (int i = 0; i < msgList.size(); ++i) {
            byte[] oi = oList.get(i);
            byte[] d = msgList.get(i);
            require(d.length == BlockSize, "blockSize");
            byte[] finalInput = safeXor(oi, d);
            byte[] o = encryptOne(finalInput);
            oList.add(o);
            logger.info("===[i={}]===\noi: {}\no:  {}", i, ppBytes(oi), ppBytes(o));
        }
        return oList;
    }

    private byte[] mac(byte[] input) {
        List<byte[]> oList = encrypt(input);
        logger.info(dumpListsOfBytes(oList));
        return mac(oList);
    }

    private byte[] mac(List<byte[]> ol) {
        require(ol.size() >= 2, "oList should be >=2, oList[1] is zeros)");
        // an all-zero-byte array has no effect for safeXor
        byte[] res = ol.get(1);
        for (int i = 2; i < ol.size(); ++i) {
            byte[] oi = ol.get(i);
            //            logger.info("i={}, res={}\nol(i)={}", i, ppBytes(res), ppBytes(oi));
            res = safeXor(res, oi);
        }
        return res;
    }

    public byte[] mac1(byte[] input) {
        List<byte[]> oList = encrypt(input);
        logger.info(dumpListsOfBytes(oList));
        return mac1(oList);
    }

    private byte[] mac1(List<byte[]> ol) {
        return ol.get(1);
    }

    public boolean check(byte[] m, byte[] c) {
        //        require(c.length == BlockSize, String.format("c.len != %d", BlockSize));
        byte[] res = mac(m);
        return Arrays.equals(res, c);
    }

    //    public static void main(String[] args) throws BadPaddingException, IllegalBlockSizeException {
    //        Oracle oracle = new Oracle();
    //        byte[] res = oracle.cipher.doFinal("01234567".getBytes());
    //        System.out.println(ppBytes(res));
    //    }

}