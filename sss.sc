import org.bouncycastle.pqc.math.linearalgebra.ByteUtils

val key2 = scala.Array[Byte](-23, 4, 9, 98, -128, -77, 9, 7)
ByteUtils.toHexString(key2)
ByteUtils.fromHexString("e904096280b30907")

val k = scala.Array[Byte](-23, 4, 9, 98, -128, -0x42, 9, 7)

ByteUtils.toHexString(Array[Byte](32, 99, -45, 78, 0, -87, -38, 98))


"!>!--A@$`".getBytes()
