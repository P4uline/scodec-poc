package fr.poc.binary.codecs

import scodec._
import scodec.bits.BitVector

object StringCodecExample extends App {

  def example(): Unit = {

    val string = "TLR1000"

    // Seems unused, but actually necessary

    val binaryString: BitVector = Codec[String].encode(string).require
    println(binaryString)
    println(binaryString.toBin)
    val decodedString = Codec[String].decode(binaryString).require
    println(decodedString)
  }

  example()

}
