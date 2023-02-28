package fr.poc.binary.codecs

import scodec.{Codec, _}
import scodec.bits.BitVector
import scodec.codecs.implicits._

object FirstExample extends App {

  def example(): Unit = {
    case class Point(x: Int, y: Int)

    case class Line(start: Point, end: Point)

    case class Arrangement(lines: Vector[Line])

    val arr = Arrangement(
      Vector(Line(Point(0, 0), Point(10, 10)), Line(Point(0, 10), Point(10, 0)))
    )
    val arrBinary: BitVector = Codec.encode(arr).require
    println(arrBinary)

    val decoded: DecodeResult[Arrangement] =
      Codec[Arrangement].decode(arrBinary).require
    println(decoded)
  }

  example()

}
