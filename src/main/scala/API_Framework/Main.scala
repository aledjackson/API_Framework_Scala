package API_Framework

import java.io.{BufferedInputStream, DataInputStream}
import java.net.ServerSocket

import scala.annotation.tailrec
import API_Framework.data_types.StatusCode.StatusCode
import API_Framework.data_types.{HTTPRequest, MyJSON, RequestMethod}


object Main extends App{
	val hello: HTTPRequest => (StatusCode, String) = DefaultResponse.hello
	val handlers = Map( (RequestMethod.GET, "/") -> hello)
	new Framework(handlers).run(9000)

}
