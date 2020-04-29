package API_Framework

import java.io.{BufferedInputStream, DataInputStream}
import java.net.{ServerSocket, Socket}

import API_Framework.data_types.RequestMethod.RequestMethod
import API_Framework.data_types.StatusCode.StatusCode
import API_Framework.data_types.{HTTPRequest, MyJSON}
import akka.actor.ActorSystem

import scala.annotation.tailrec

class Framework(handlers: Map[(RequestMethod,String), HTTPRequest => (StatusCode, String)]) {
	def run(port: Int):Unit = {
		val server = new ServerSocket(port)
		loop()

		@tailrec
		def loop(): Unit = {
			println("waiting for client")
			val s = server.accept()

			ConnectionHandler(s,handlers)
//			need something to be able to shut the server down
			loop()
		}
	}

}
object Framework{
	val MAX_HEADER_SIZE = 1000
	val MAX_POST_SIZE = 2000

}
