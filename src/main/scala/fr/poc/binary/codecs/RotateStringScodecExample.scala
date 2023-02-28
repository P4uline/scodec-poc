package fr.poc.binary.codecs

import scodec.Codec
import scodec.bits.HexStringSyntax
import scodec.codecs._

object RotateStringScodecExample extends App {

  def example(): Unit = {
    val otp =
      hex"54686973206973206e6f74206120676f6f642070616420746f2075736521".bits
    // println(otp.toBin)
    // otp: scodec.bits.BitVector = BitVector(240 bits, 0x54686973206973206e6f74206120676f6f642070616420746f2075736521)
    val bits3 =
    hex"746be39ece241e0da28b7acd4fad63632249ec5e2e402d5a0b2cd95d0a05".bits
    // println(bits3.toBin)
    // bits: scodec.bits.BitVector = BitVector(240 bits, 0x746be39ece241e0da28b7acd4fad63632249ec5e2e402d5a0b2cd95d0a05)
    val decoded2 = (bits3 ^ otp) rotateLeft 3
    val stringCodec2: Codec[String] = variableSizeBytes(uint16, utf8)
    val msg = stringCodec2.decode(decoded2).require.value
    println(msg)
  }

  example()

}
