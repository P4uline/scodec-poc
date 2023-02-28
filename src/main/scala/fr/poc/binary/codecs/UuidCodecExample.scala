package fr.poc.binary.codecs

import scodec._
import scodec.bits.{BitVector, HexStringSyntax}
import scodec.codecs.uint8

import java.util.UUID


object UuidCodecExample extends App {

  /////////////////////////////////////////////
  // Create bitvector of different sizes for testing
  /////////////////////////////////////////////
  val N = 1024

  def bitVectors(size: Int) =
    (0 to N).map { n =>
      BitVector.fromLong(n.toLong).take(size.toLong).compact
    }

  val bitVectors96bits = bitVectors(96)
  val bitVectors64bits = bitVectors(64)
  val bitVectors60bits = bitVectors(60)
  val bitVectors32bits = bitVectors(32)
  //val bitVectors32bitsNonCompacted = bitVectors(64).map(_.drop(32))
  val bitVectors24bits = bitVectors(24)
  val bitVectors16bits = bitVectors(16)
  val bitVectors14bits = bitVectors(14)
  val bitVectors8bits = bitVectors(8)

  def toInt_32bit_bigEndian_nonCompacted =
    bitVectors32bits.foldLeft(List[Int]())((acc, b) => b.toInt() :: acc)
  // println(toInt_32bit_bigEndian_nonCompacted)

  def example(): Unit = {
    val uuid = UUID.fromString("b0739ffd-d1f9-47b8-aa2e-b2be69733def")
    val uuidCodec = codecs.uuid
    val uuidEncoded: BitVector = uuidCodec.encode(uuid).toOption.get // YOLO
    val uuidDecoded: Attempt[DecodeResult[UUID]] = uuidCodec.decode(uuidEncoded)

    println(s"encoded UUID $uuidEncoded")
    println(s"encoded UUID ${uuidEncoded.toBin}")
    println(s"decoded UUID $uuidDecoded")
    // Successful(DecodeResult(b0739ffd-d1f9-47b8-aa2e-b2be69733def,BitVector(empty)))

    val u64Codec = codecs.bits(64)
    val u64Decoded = u64Codec.decode(hex"0xb0739ffdd1f947b8aa2eb2be69733def".bits)
    println(s"u64Decoded $u64Decoded")

    val mixCodec = uint8 :: u64Codec // uint8 :: uint8 :: uint16
    val bitVector: BitVector = bitVectors96bits.head
    val mixResult = mixCodec.decode(bitVector)

    println(mixResult)
    println(bitVectors96bits.head)
  }

  example()
}
