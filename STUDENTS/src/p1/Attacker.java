package p1;

public class Attacker {
	
	// you are free to ADD fields, methods and implements, however DON'T REMOVE any

    final private Oracle oracle;

    public Attacker() {
        this.oracle = new Oracle();
    }

    public Attacker(Oracle oracle) {
        this.oracle = oracle;
    }


    public byte[] decryptSuffix() {
    	// TODO implement decryption code here
    	return null;
    }
}
