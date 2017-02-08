import common.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

import static common.Utils.*;

@SuppressWarnings({"WeakerAccess", "FieldCanBeLocal", "SameParameterValue"})
public class Attacker1 {

    private static Logger logger = LoggerFactory.getLogger(Attacker1.class);

    final private Oracle1 oracle;

    public Attacker1() {
        this.oracle = new Oracle1();
    }

    public Attacker1(Oracle1 oracle) {
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
        // sample test input
        Oracle1 oracle = new Oracle1(Config.p1Key, Config.p1Suffix);
        Attacker1 attacker = new Attacker1(oracle);
        byte[] res = attacker.decryptSuffix();
        // should be true
        System.out.println(isConsistent(Config.p1Suffix, res));
    }
}
