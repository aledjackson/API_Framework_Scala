package API_Framework

import java.io.{BufferedInputStream, DataInputStream}
import java.net.{ServerSocket, Socket}

import API_Framework.RequestMethod.RequestMethod
import API_Framework.StatusCode.StatusCode
import akka.actor.ActorSystem

import scala.annotation.tailrec

class Framework(handlers: Map[(RequestMethod,String), MyJSON => (StatusCode, String)]) {
	def run(port: Int):Unit = {
		val server = new ServerSocket(port)
		loop()

		@tailrec
		def loop(): Unit = {
			println("waiting for new client")
			val s = server.accept()
			ConnectionHandler(s,handlers)
//			need something to be able to shut the server down
			loop()
		}
	}




}
