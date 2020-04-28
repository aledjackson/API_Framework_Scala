package API_Framework

import java.io.{BufferedInputStream, BufferedOutputStream, DataInputStream, DataOutputStream}
import java.net.Socket

import API_Framework.RequestMethod.RequestMethod

class ConnectionHandler(s: Socket, in: DataInputStream, out: DataOutputStream,
						handlers: Map[(RequestMethod,String), MyJSON => MyJSON]) extends Runnable{

/*	TODO: write wrappers for DataOutputStream and DataInputStream to make them mimic functional objects in terms of error handling */

	override def run(): Unit = {
		val packet = HTTPRequest.gatherPacket(in)
		val request = HTTPRequest(packet)
		for {
			request_val <- request
		} yield {
//			these all need wrappers to make them functional
			val handler = handlers.get((request_val.requestMethod,request_val.route));
			handler match{
				case Some(h) => sendResponse(h(request_val.obj)); s.close()
				case None    => sendResponse(Responses.PAGE_NOT_FOUND); s.close()
			}
		}
	}

	private def sendResponse(response: MyJSON): Unit ={
//		TODO: insert any error checking here or give users the option to have their own error checking functions
		val strResponse = Responses.template
		println("sent response: \n" + strResponse)
		out.write(strResponse.getBytes)
		out.flush()
	}
	private def sendResponse(response: String): Unit ={
		println("sent response: \n" + response)
		out.write(response.getBytes)
		out.flush()
	}
}
object ConnectionHandler{
	def apply(s: Socket, handlers: Map[(RequestMethod,String), MyJSON => MyJSON]) = {
		val bis = new BufferedInputStream(s.getInputStream)
		val bos = new BufferedOutputStream(s.getOutputStream)
		val in  = new DataInputStream(bis)
		val out = new DataOutputStream(bos)
		val ch = new ConnectionHandler(s, in, out, handlers)
		val th = new Thread(ch)
		th.start()

	}
}
