package fr.poc.binary.codecs

import scodec._
import scodec.bits.{BitVector, _}
import scodec.codecs._
object ByteCodecExample extends App {

  def example(): Unit = {
    val bytesCodec = byte :: byte :: byte :: byte :: byte :: byte :: byte :: byte
    val vector1: BitVector = BitVector.fromValidBase64("TLRkjdfqafc")
    println(s"1) size ${vector1.size}")
    val stringBitVector: BitVector = vector1
    val stringResult = bytesCodec.decode(stringBitVector)
    println(s" bitvector: ${stringResult.require.remainder.toString()}")
    println(s" bitvector: ${stringResult.require.value}")

    val byteCodec2 = byte :: byte
    val vector2: ByteVector = bin"000000010000000100000001".toByteVector
    println(s"2) size ${vector2.size}")
    val stringBitVector2: ByteVector = vector2
    val stringResult2 = byteCodec2.decode(stringBitVector2.bits)
    println(s" bitvector : ${stringResult2.require.remainder.toString()}")
    println(s" bitvector : ${stringResult2.require.value}")

    val bytetlrCodec3 = byte :: byte
    val vector3: ByteVector = bin"0000001000000010".toByteVector
    println(s"3) size ${vector3.size}")
    val stringBitVector3: ByteVector = vector3
    val stringResult3 = bytetlrCodec3.decode(stringBitVector3.bits)
    println(s" bitvector : ${stringResult3.require.remainder.toString()}")
    println(s" bitvector : ${stringResult3.require.value}")
  }

  example()

}
