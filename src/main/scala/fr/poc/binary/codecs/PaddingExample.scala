package fr.poc.binary.codecs

import scodec.bits.{BitVector, _}
import scodec.codecs.{uint8, _}
import scodec.{Codec, _}
object PaddingExample extends App {

  def example(): Unit = {
    val stringValue = "TLR"
    // val bitsWithPadding = BitVector(stringValue.getBytes("UTF-8"))

    /**
    Params:
      size
       – codec that encodes / decodes the size in bytes
      value
       – codec the encodes / decodes the value
      sizePadding
       – number of bytes to add to the size before encoding (and subtract from the size before decoding)
    */
    val stringCodec1: Codec[String] = variableSizeBytes(size = uint8, value = utf8, sizePadding = 3)
    val withPaddingEncoded1 = stringCodec1.encode(stringValue).require
    val decoded1 = stringCodec1.decode(withPaddingEncoded1).require
    println(decoded1.value)

    val stringCodec2: Codec[String] = variableSizeBytes(size = uint8, value = utf8, sizePadding = 1)
    val withPaddingEncoded2 = stringCodec2.encode(stringValue).require
    val decoded2 = stringCodec2.decode(withPaddingEncoded2).require
    println(decoded2.value)

    val stringCodec3: Codec[String] = variableSizeBytes(size = uint8, value = utf8, sizePadding = 0)
    val withPaddingEncoded3 = stringCodec3.encode(stringValue).require
    val decoded3 = stringCodec3.decode(withPaddingEncoded3).require
    println(decoded3.value)

    println(withPaddingEncoded1 == bin"00000110" ++ BitVector(stringValue.getBytes))
    println(withPaddingEncoded2 == bin"00000100" ++ BitVector(stringValue.getBytes))
    println(withPaddingEncoded3 == bin"00000011" ++ BitVector(stringValue.getBytes))

    /*println(withPaddingEncoded1.toBin)
    println(withPaddingEncoded2.toBin)
    println(withPaddingEncoded3.toBin)*/

    // Decode the value encoded with padding with a simple codec
    // so we can see the size changing according to the sizePadding value.
    val simpleCodec = uint8 :: utf8
    println(simpleCodec.decode(withPaddingEncoded1))
    println(simpleCodec.decode(withPaddingEncoded2))
    println(simpleCodec.decode(withPaddingEncoded3))
  }

  example()
}
