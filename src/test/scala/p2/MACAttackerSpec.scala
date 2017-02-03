package p2

import java.nio.file.{Files, Paths}

import common.CryptoSpec
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils
import org.scalatest.Inspectors._
import org.scalatest.time.{Millis, Span}

class MACAttackerSpec extends CryptoSpec {
  val timeLimit = Span(1000, Millis)

  lazy val getTests: Array[(Array[Byte], Array[Byte])] = {
    val testFile = getClass.getResource("/p2cases.txt").toURI.getPath
    import collection.JavaConverters._
    val lines = Files.readAllLines(Paths.get(testFile)).asScala
    val buf = for (l <- lines) yield {
      val ss = l.split(",\\s*")
      val bytes = ss.map(ByteUtils.fromHexString)
      require(ss.length == 2, s"${ss.mkString(", ")}")
      (bytes.head, bytes.last)
    }
    buf.toArray
  }

  lazy val getInput = getTests.map(_._1)

  def check(attacker: Attacker, oracle: Oracle, input: Array[Byte], resOpt: Option[Array[Byte]]): Boolean = {
    val cracked = attacker.crack(input)
    resOpt match {
      case Some(given) => {
        //        require(given.deep == cracked.deep)
      }
      case None =>
    }
    oracle.check(input, cracked)
  }

  "default case" should "be checked" in {
    val oracle = new Oracle()
    val attacker = new Attacker(oracle)
    forAll(getTests) { case (input, c) =>
      check(attacker, oracle, input, Option(c)) should be(true)
    }

  }

  "MAC attacker" should "fool oracle to pass check" in {
    val macKey = ByteUtils.fromHexString("a10f773560408799")
    val oracle = new Oracle(macKey)
    val attacker = new Attacker(oracle)
    forAll(getTests) { case (input, _) =>
      check(attacker, oracle, input, None) should be(true)
    }

  }

}
