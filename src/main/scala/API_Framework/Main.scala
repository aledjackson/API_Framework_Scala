package API_Framework

import java.io.{BufferedInputStream, DataInputStream}
import java.net.ServerSocket

import scala.annotation.tailrec
import API_Framework.MyJSON


object Main extends App{
	val hello: MyJSON => MyJSON = Helper.hello
	val handlers = Map( (RequestMethod.GET, "/") -> hello)
	new Framework(handlers).run(9000)

}
