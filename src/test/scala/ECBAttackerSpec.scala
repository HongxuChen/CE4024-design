import common.{Config, CryptoSpec, Utils}
import org.scalatest.time.{Millis, Span}

class ECBAttackerSpec extends CryptoSpec {
  // 800ms is enough on my machine for AES
  val timeLimit = Span(1000, Millis)

  "default suffix" should "match" in {
    val attacker = new Attacker1()
    val res = attacker.decryptSuffix()
    println(Utils.ppBytes(res))
    res should contain theSameElementsAs Config.p1Suffix.getBytes()
  }

  // computed suffix's length may not be fixed
  "shorter suffix" should "be same for first n bytes" in {
    //    val key1 = "abcdefghijklmnop"
    val key1 = "abcdefgh"
    val suffix1 = "123456"
    val attacker1 = new Attacker1(new Oracle1(key1, suffix1))
    val computed1 = attacker1.decryptSuffix()
    println(Utils.ppBytes(computed1))
    computed1.slice(0, suffix1.length) should contain theSameElementsAs suffix1.getBytes()
    //////////////////////////////////////////////////////
    //    val key2 = Array[Byte](-23, 4, 9, 98, -128, -77, 9, 7, 33, 127, 32, 45, 99, 4, 37, 24)
    val key2 = Array[Byte](-23, 4, 9, 98, -128, -77, 9, 7)
    val suffix2 = "!>!A@$`".getBytes()
    val attacker2 = new Attacker1(new Oracle1(key2, suffix2))
    val computed2 = attacker2.decryptSuffix()
    println(Utils.ppBytes(computed2))
    computed2.slice(0, suffix2.length) should contain theSameElementsAs suffix2
  }

  "byte suffix" should "be decrypted" in {
    //    val key = Array[Byte](-23, 4, 9, 98, -128, -0x42, 9, 7, 33, 0x7f, 0x32, 45, -99, 4, 0x35, 24)
    //    val suffix = Array[Byte](32, 99, -45, 78, 0, -87, 38, 98, 73, 92, 43, 0, 0, -22, -54, -93)
    val key = Array[Byte](-23, 4, 9, 98, -128, -0x42, 9, 7)
    val suffix = Array[Byte](32, 99, -45, 78, 0, -87, -38, 98)
    val attacker = new Attacker1(new Oracle1(key, suffix))
    val computed = attacker.decryptSuffix()
    println(Utils.ppBytes(computed))
    computed should contain theSameElementsAs suffix
  }

}
