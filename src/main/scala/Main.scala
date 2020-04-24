import java.io.{BufferedInputStream, DataInputStream, EOFException}
import java.net.ServerSocket

import scala.annotation.tailrec

object Main extends App{
  val PORT = 9000

  val server = new ServerSocket(PORT)
  println("waiting for client")
  val s = server.accept()
  println("found a client")
  val bis = new BufferedInputStream(s.getInputStream)
  val in  = new DataInputStream(bis)
  val packet = helper.gatherPacket(in)
  println(packet)



}
object helper{

  def gatherPacket(in: DataInputStream): String = {
    @tailrec def _gp(in: DataInputStream, string: StringBuilder, newLineCount: Int): String = {
      if(newLineCount < 2) {
        val char = in.readByte().asInstanceOf[Char]
        char match {
          case '\n'  => _gp(in, string += '\n', newLineCount + 1)
          case '\r'  => _gp(in, string += '\r', newLineCount)
          case other => _gp(in, string += other, 0)
        }
      } else {
        string.toString()
      }
    }

    _gp(in, new StringBuilder(), 0)
  }



}
