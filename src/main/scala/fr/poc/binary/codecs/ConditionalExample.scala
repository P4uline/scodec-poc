package fr.poc.binary.codecs

import scodec.bits._
import scodec.codecs.{uint8, _}
import scodec.{Codec, _}

object ConditionalExample extends App {

  def example: Unit = {
    case class Flags(x: Boolean, y: Boolean, z: Boolean)
        extends Codec[Boolean] {
      override def encode(value: Boolean): Attempt[BitVector] = ???

      override def sizeBound: SizeBound = ???

      override def decode(bits: BitVector): Attempt[DecodeResult[Boolean]] = ???
    }

    // Let's build a codec for the binary form:
    //  x_included              bool(1)
    //  y_included              bool(1)
    //  z_included              bool(1)
    //                          ignore(5)
    //  if (x_included) {
    //    x                     uint8
    //  }
    //  if (y_included) {
    //    y                     int64
    //  }
    //  if (z_included) {
    //    z                     utf8_32
    //  }

    import shapeless._
    val flagsCodec: Codec[Flags] = (bool :: bool :: bool :: ignore(5)).as[Flags]

    // In order to decode the rest of the binary format, we need access to
    // the decoded flags -- we can do that with flatPrepend
    val conditionalCodec: Codec[
      Flags :: Option[Int] :: Option[Long] :: Option[String] :: HNil
    ] =
      flagsCodec.flatPrepend { flgs =>
        conditional(flgs.x, uint8) ::
          conditional(flgs.y, int64) ::
          conditional(flgs.z, utf8_32)
      }

    // The type of codec is Flags prepended to whatever tuple the body returned:
    val value =
      Flags(true, true, true) :: Some(1) :: Some(1L) :: Some("Hi") :: HNil
    val conditionalEncoded = conditionalCodec.encode(value).require
    val eq =
      conditionalEncoded == bin"11100000" ++ hex"010000000000000001000000024869".bits
    println(eq)
    println(s"flatPrepend $conditionalEncoded")
    println(
      s"flatPrepend ${conditionalCodec.decode(bin"11100000" ++ hex"010000000000000001000000024869".bits).require}"
    )

    val v2 = Flags(true, true, false) :: Some(1) :: Some(1L) :: None :: HNil
    val encoded2 = conditionalCodec.encode(v2).require
    val eq2 = encoded2 == bin"11000000" ++ hex"010000000000000001".bits
    println(eq2)
    println(s"flatPrepend $encoded2")
    println(
      s"flatPrepend ${conditionalCodec.decode(bin"11000000" ++ hex"010000000000000001".bits).require}"
    )

    val v3 = Flags(true, false, false) :: Some(1) :: None :: None :: HNil
    val encoded3 = conditionalCodec.encode(v3).require
    val eq3 = encoded3 == bin"10000000" ++ hex"01".bits
    println(eq3)
    println(s"flatPrepend $encoded3")
    println(
      s"flatPrepend ${conditionalCodec.decode(bin"10000000" ++ hex"01".bits).require}"
    )
  }

  example

}
