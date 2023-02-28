package fr.poc.binary.codecs

import akka.stream.{Attributes, FlowShape, Inlet, Outlet}
import akka.stream.scaladsl.{BidiFlow, Flow, Framing, Keep}
import akka.stream.stage.{GraphStage, GraphStageLogic, InHandler, OutHandler}
import akka.util.ByteString

import java.util.concurrent.ThreadLocalRandom

object AkkaStreamExample extends App {

  def example(): Unit = {
    /*import akka.stream.scaladsl.Framing
    val rawData = Source(
      List(
        ByteString("Hello World"),
        ByteString("\r"),
        ByteString("!\r"),
        ByteString("\nHello Akka!\r\nHello Streams!"),
        ByteString("\r\n\r\n")))
    val linesStream = rawData
      .map(Framing.simpleFramingProtocol(32))*/

    val rechunk = Flow[ByteString].via(new Rechunker).named("rechunker")
    val rechunkBidi = BidiFlow.fromFlowsMat(rechunk, rechunk)(Keep.left)
    val codecFlow =
      Framing
        .simpleFramingProtocol(maximumMessageLength = 4) // 32 bits
        .atop(rechunkBidi)
        .atop(Framing.simpleFramingProtocol(1024).reversed)
        .join(Flow[ByteString]) // Loopback
  }

  class Rechunker extends GraphStage[FlowShape[ByteString, ByteString]] {

    val out: Outlet[ByteString] = Outlet("Rechunker.out")
    val in: Inlet[ByteString] = Inlet("Rechunker.in")

    override val shape: FlowShape[ByteString, ByteString] = FlowShape(in, out)

    override def createLogic(inheritedAttributes: Attributes): GraphStageLogic =
      new GraphStageLogic(shape) with InHandler with OutHandler {

        private var rechunkBuffer = ByteString.empty

        private def rechunk() = {
          if (!isClosed(in) && ThreadLocalRandom.current().nextBoolean()) pull(in)
          else {
            val nextChunkSize =
              if (rechunkBuffer.isEmpty) 0
              else ThreadLocalRandom.current().nextInt(0, rechunkBuffer.size + 1)
            val newChunk = rechunkBuffer.take(nextChunkSize).compact
            rechunkBuffer = rechunkBuffer.drop(nextChunkSize).compact
            if (isClosed(in) && rechunkBuffer.isEmpty) {
              push(out, newChunk)
              completeStage()
            } else push(out, newChunk)
          }
        }

        override def onPush(): Unit = {
          rechunkBuffer ++= grab(in)
          rechunk()
        }

        override def onPull(): Unit = {
          rechunk()
        }

        override def onUpstreamFinish(): Unit = {
          if (rechunkBuffer.isEmpty) completeStage()
          else if (isAvailable(out))
            onPull()
        }

        setHandlers(in, out, this)
      }
  }

  example()

}


