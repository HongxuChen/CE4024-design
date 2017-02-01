package common;

public class Config {

    public static String ALGO = "DES";
    public static String p1Key = "abcdefgh";
    public static String p1Suffix = "01234567";

    //    public static String ALGO = "AES";
    //    public static String p1Key = "a,b.c?d!e|f<g>h}";
    //    public static String p1Suffix = "0123456789abcdef";

    // http://stackoverflow.com/questions/1220751/how-to-choose-an-aes-encryption-mode-cbc-ecb-ctr-ocb-cfb
    // XTS, CBC/OFB/CFB
    public static String p2INPUT = "00a0a1a2a3a4a5a6a70f1f2f3f4f5f6f7fcff176210f519ff2d5366ae5e853491b00";
    public static String MODE = "CBC";
    public static String DES = "DES";
    public static String p2MacKey = "abcdefgh";

}
