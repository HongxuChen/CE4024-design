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


    public byte[] decryptSuffix() {
    	// TODO implement decryption code here
    	return null;
    }
    
    // sample test input
    // we will use more inputs to test your implementation
    public static void main(String[] args) {
        Oracle1 oracle = new Oracle1(Config.p1Key, Config.p1Suffix);
        Attacker1 attacker = new Attacker1(oracle);
        byte[] res = attacker.decryptSuffix();
        // should be true
        System.out.println(isConsistent(Config.p1Suffix, res));
    }

}
