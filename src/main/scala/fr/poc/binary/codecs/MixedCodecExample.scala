package fr.poc.binary.codecs

import scodec.Attempt.{Failure, Successful}
import scodec._
import scodec.bits.HexStringSyntax
import scodec.codecs.{uint16, uint8}

object MixedCodecExample extends App {

  def example(): Unit = {
    val codecUint8Uint8Uint16 = uint8 :: uint8 :: uint16

    // Decode a bit vector using that codec
    val result = codecUint8Uint8Uint16.decode(hex"102a03ff".bits)

    // Successful(DecodeResult(((16, 42), 1023), BitVector(empty)))
    // 10000 00101010 0000001111111111
    // 10000   101010       1111111111
    println(hex"102a03ff".toBin)

    println(result)
    result match {
      case Successful(s) =>
        println(s.value.head)
        println(s.value.tail.head)
        println(s.value.tail.tail.head)
      case Failure(cause) => println(s"An error occured $cause")
    }
  }

  example()
}
