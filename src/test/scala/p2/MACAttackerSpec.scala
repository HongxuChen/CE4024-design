package p2

import common.CryptoSpec
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils
import org.scalatest.Inspectors._
import org.scalatest.time.{Millis, Span}

class MACAttackerSpec extends CryptoSpec {
  val timeLimit = Span(1000, Millis)


  lazy val getInput: List[Array[Byte]] = List(
    "a1d38873932046d3",
    "0000000000000000000000000000000000000000000000000000000000000000a1d38873932046d3",
    "619d336b7439146d335a1d243873936d3204c9a1d8739320253832ff9e",
    "a1d8739320325389932619d336b7439146d35a1d24773873936d3204c931ff9e"
  ).map(ByteUtils.fromHexString)


  def check(attacker: Attacker, oracle: Oracle, input: Array[Byte]): Boolean = {
    val cracked = attacker.crack(input)
    oracle.check(input, cracked)
  }

  "default case" should "be checked" in {
    val oracle = new Oracle()
    val attacker = new Attacker(oracle)
    forAll(getInput) { input =>
      check(attacker, oracle, input) should be(true)
    }

  }

  "MAC attacker" should "fool oracle to pass check" in {
    val macKey = ByteUtils.fromHexString("a10f773560408799")
    val oracle = new Oracle(macKey)
    val attacker = new Attacker(oracle)
    forAll(getInput) { input =>
      check(attacker, oracle, input) should be(true)
    }

  }

}
