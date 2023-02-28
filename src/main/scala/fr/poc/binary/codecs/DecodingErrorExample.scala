package fr.poc.binary.codecs

import scodec.Attempt.{Failure, Successful}
import scodec.bits.BitVector
import scodec.codecs.uint16

object DecodingErrorExample extends App {

  def example(): Unit = {
    val value = 10
    /**
     * In failure because if we create a binary representation like this:
     *      BitVector(10)
     *   the bit vector encode the value on 8 bits, so it fails when we try to decode
     *   with uint16 codec.
     */
    val failed = uint16.decode(BitVector(value))
    println(s"isFailure: ${failed.isFailure}")
    failed match {
      case Successful(value) => println(s"success $value")
      case Failure(err) => println(s"error ${err.messageWithContext}")
    }

    /**
     * Here the value is encoded with uint16 codec, we are sure to not loose information.
     * We can safely decode.
     */
    val success = uint16.decode(uint16.encode(value).require)
    println(s"isFailure: ${success.isFailure}")
    success match {
      case Successful(value) => println(s"success $value")
      case Failure(err) => println(s"error ${err.messageWithContext}")
    }
  }

  example()
}
