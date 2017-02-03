package p2;

import common.Config;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static common.Utils.*;

public class Attacker {

    private Oracle oracle;

    public Attacker(Oracle oracle) {
        this.oracle = oracle;
    }

    private static Logger logger = LoggerFactory.getLogger(Attacker.class);

    private static final int BlockSize = getAlgoBlockSize(Config.DES);

    @SuppressWarnings({"unused", "Duplicates"})
    // only for attacker
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
        logger.info(dumpListsOfBytes(msgList));
        return msgList;
    }

    private List<byte[]> initOList() {
        List<byte[]> oList = new ArrayList<byte[]>();
        byte[] zero = new byte[BlockSize];
        Arrays.fill(zero, (byte) 0);
        oList.add(zero);
        return oList;
    }

    private List<byte[]> fakeEncrypt(byte[] input) {
        List<byte[]> msgList = splitMsgBytes(input);
        List<byte[]> oList = initOList();
        for (int i = 0; i < msgList.size(); ++i) {
            byte[] oi = oList.get(i);
            byte[] d = msgList.get(i);
            byte[] finalInput = safeXor(oi, d);
            require(d.length == BlockSize, "blockSize");
            // the effect should be the safe as cipher.doFinal(finalInput)
            byte[] o = oracle.mac1(finalInput);
            oList.add(o);
        }
        return oList;
    }

    // oList.get(0) is NOT guaranteed to be same length as others
    private byte[] calculateMAC(List<byte[]> oList) {
        require(oList.size() >= 2, "oList.len should >=2");
        byte[] res = oList.get(1);
        for (int i = 2; i < oList.size(); ++i) {
            res = safeXor(res, oList.get(i));
        }
        return res;
    }

    public byte[] crackMAC(byte[] input) {
        List<byte[]> oList = fakeEncrypt(input);
        logger.info(dumpListsOfBytes(oList));
        return calculateMAC(oList);
    }

    public static void main(String[] args) {
        Oracle oracle = new Oracle();
        Attacker attacker = new Attacker(oracle);
        String s = Config.p2INPUT;
        byte[] input = ByteUtils.fromHexString(s);
        byte[] res = attacker.crackMAC(input);
        boolean matched = oracle.check(input, res);
        if (matched) {
            System.out.printf("%s\t%d\n%s\t%d\n",
                    ByteUtils.toHexString(input), input.length, ByteUtils.toHexString(res), res.length);
        } else {
            throw new RuntimeException("mismatch");
        }
    }

}
