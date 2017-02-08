import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;

import common.Config;
import static common.Utils.*;

public class Attacker2 {
	
	// you are free to ADD fields, methods and implements, however DON'T REMOVE any

    final private Oracle2 oracle;

    public Attacker2(Oracle2 oracle) {
        this.oracle = oracle;
    }

    public byte[] crack(byte[] input) {
    	// TODO implement your crack here
    	return null;
    }
    
    // sample test input
    // we will use more inputs to test your implementation
    public static void main(String[] args) {
        Oracle2 oracle = new Oracle2(Config.p2MacKey.getBytes());
        Attacker2 attacker = new Attacker2(oracle);
        String s = Config.p2INPUT;
        byte[] input = ByteUtils.fromHexString(s);
        byte[] res = attacker.crack(input);
        // matched should be true
        boolean matched = oracle.check(input, res);
        if (matched) {
            System.out.printf("%s\t%d\n%s\t%d\n",
                    ByteUtils.toHexString(input), input.length, ByteUtils.toHexString(res), res.length);
        } else {
            throw new RuntimeException("mismatch");
        }
    }

}
