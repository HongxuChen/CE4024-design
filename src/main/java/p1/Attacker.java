package p1;

import common.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

import static common.Utils.*;

@SuppressWarnings({"WeakerAccess", "FieldCanBeLocal", "SameParameterValue"})
public class Attacker {

    private static Logger logger = LoggerFactory.getLogger(Attacker.class);

    final private Oracle oracle;

    public Attacker() {
        this.oracle = new Oracle();
    }

    public Attacker(Oracle oracle) {
        this.oracle = oracle;
    }

    private static int BlockSize = getAlgoBlockSize(Config.ALGO);
    private final int BYTE_MAX = 256;

    private boolean isByteMatch(byte[] current, byte[] oracleBytes) {
        for (int i = 0; i < BlockSize; ++i) {
            if (current[i] != oracleBytes[i]) {
                return false;
            }
        }
        return true;
    }

    private byte guessOne(byte[] oracleBytes, byte[] known, byte[] given) {
        byte[] current = concat(given, known);
        require(current.length == BlockSize - 1,
                String.format("current.length=%d, BlockSize=%d", current.length, BlockSize));
        logger.info("current={}\noracleBytes={}\n", current, oracleBytes);
        for (int i = 0; i < BYTE_MAX; i++) {
            byte v = (byte) i;
            byte[] whole = concat(current, v);
            byte[] cipherText = oracle.compose(whole);
            if (isByteMatch(cipherText, oracleBytes)) {
                return v;
            }
        }
        throw new RuntimeException("haven't found new suffix byte");
    }

    public byte[] decryptSuffix() {
        byte[] bytes = "0A12B3c".getBytes();
//        byte[] bytes = "0A12B3c45D67".getBytes();
        return decryptSuffix(bytes);
    }

    @SuppressWarnings("unused")
    private byte[] decryptSuffix(String init) {
        logger.info("{}, {}", init, init.length());
        return decryptSuffix(init.getBytes());
    }

    private byte[] decryptSuffix(byte[] init) {
        require(init.length == BlockSize - 1,
                String.format("init=%s, len=%d, BlockSize=%s", ppBytes(init), init.length, BlockSize));
        byte[] known = {};
        for (int i = 0; i < BlockSize; ++i) {
            byte[] given = Arrays.copyOfRange(init, 0, BlockSize - 1 - i);
            byte[] oracleBytes = oracle.compose(given);
            byte newKnown = guessOne(oracleBytes, known, given);
            logger.info("suffix[{}]: 0x{}", i, Integer.toHexString(newKnown));
            known = concat(known, newKnown);
        }
        return known;
    }

    public static void main(String[] args) {
        Attacker attacker = new Attacker();
        byte[] res = attacker.decryptSuffix();
        logger.warn("succeed: {}", isConsistent(Config.p1Suffix, res));
    }
}
