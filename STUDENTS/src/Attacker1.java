import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;

import common.Config;
import static common.Utils.*;

public class Attacker1 {
	
	// you are free to ADD fields, methods and implements, however DON'T REMOVE any

    final private Oracle1 oracle;

    public Attacker1() {
        this.oracle = new Oracle1();
    }

    public Attacker1(Oracle1 oracle) {
        this.oracle = oracle;
    }

    ////////////////////////////////////////////////////////////////////////////////
    // you are allowed to add extra *private* member functions
    // however they won't be exposed to the unit tests
    // you are not allowed to add any fields
    
    /// --- start of implementation area    

    public byte[] decryptSuffix() {
    	// TODO implement decryption code here
    	return null;
    }
    
    /// --- end of implementation area
    ////////////////////////////////////////////////////////////////////////////////
    
    // sample test input
    // note: we internally use a JUnit like tool so this "main" function only serves as
    // a demo to show what the unit test will be like
    
    // we will use more inputs to test your implementation
    // in the unit tests, by
    // * changing Oracle1's key
    // * changing Oracle1's suffix
    public static void main(String[] args) {
        ///////////////////////////////////////////////////////////
        String key = "3%ac^`+=";  // a different key
        String suffix = "a19q-j*"; // a different suffix
        ///////////////////////////////////////////////////////////
        Oracle1 oracle = new Oracle1(key.getBytes(), suffix.getBytes());
        Attacker1 attacker = new Attacker1(oracle);
        byte[] res = attacker.decryptSuffix();
        ///////////////////////////////////////////////////////////
        // should be true
        System.out.println(isConsistent(suffix, res));
    }

}
