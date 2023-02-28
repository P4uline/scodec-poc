package fr.poc.binary.codecs

object Main extends App {

  /*println("""
  ///////////////////////////////////////
  // First example : encode and decode binary data representing points and lines
  ///////////////////////////////////////
  """)
  FirstExample.example()*/

  /*println("""
  /////////////////////////////////////////////
  // Encode and Decode string
  /////////////////////////////////////////////
  """)
  StringCodecExample.example()*/

  /*println("""
    ///////////////////////////////////////
    // UUID
    ///////////////////////////////////////
    """)
  UuidCodecExample.example()*/

  /*println("""
  ///////////////////////////////////////
  // Decode / Encode bytes
  ///////////////////////////////////////
  """)
  ByteCodecExample.example()*/

  /*println("""
  ///////////////////////////////////////
  // Codec with mixed types
  // Create a codec for an 8-bit unsigned int followed by an 8-bit unsigned int followed by a 16-bit unsigned int
  ///////////////////////////////////////
  """)
  MixedCodecExample.example()*/

  /*println("""
    ///////////////////////////////////////
    // Streaming scodec example
    ///////////////////////////////////////
    """)
  // TODO*/

  /*println("""
  ///////////////////////////////////////
  // Decode Working with binary is easy!
  // variableSizeBytes
  ///////////////////////////////////////
  """)
  RotateStringScodecExample.example()*/

  /*println(
    """
    ///////////////////////////////////////
    // Conditional content example
    ///////////////////////////////////////
    """)
  ConditionalExample.example*/

  /*println(
  """
  ///////////////////////////////////////
  // stringWithPadding
  ///////////////////////////////////////
  """)
  PaddingExample.example()*/



  // One disadvantage of this approach is that the tuple is inhabited by illegal values.
  // For example, consider encoding the tuple: (Flags(true, true, true), None, None, None).
  // In this example, the tuple claims that x, y, and z are defined but then provides no values for those
  // fields. This is an example of allowing the binary structure leak in to the domain model.

}
