package API_Framework

import java.io.{BufferedInputStream, BufferedOutputStream, DataInputStream, DataOutputStream}
import java.net.Socket

import API_Framework.data_types.RequestMethod.RequestMethod
import API_Framework.data_types.StatusCode.StatusCode
import API_Framework.data_types.{HTTPRequest, HTTPResponse, MyJSON}

class ConnectionHandler(s: Socket, in: DataInputStream, out: DataOutputStream,
						handlers: Map[(RequestMethod,String), MyJSON => (StatusCode,String)]) extends Runnable{

/*	TODO: write wrappers for DataOutputStream and DataInputStream
     to make them mimic functional objects in terms of error handling */

	override def run(): Unit = {
		val packet = HTTPRequest.gatherPacket(in)
		val request = HTTPRequest(packet)
		for {
			request_val <- request
		} yield {
//			these all need wrappers to make them functional
			val handler = handlers.get((request_val.requestMethod,request_val.route));
			handler match{
				case None    => sendResponse(Responses.PAGE_NOT_FOUND); s.close()
				case Some(h) => processRequest(h(request_val.obj)) match {
									case Right((sc,body))   => sendResponse(sc, body); s.close()
									case Left(_)			=> sendResponse(Responses.INTERNAL_ERROR); s.close()
								}
			}
		}
	}

	//	wraps the handlers so that they won't cause a halting error
	def processRequest(handler: => (StatusCode, String)): Either[Exception, (StatusCode, String)] ={
		try{
			Right(handler)
		} catch {
			case _ => Left(new Exception("Error occured within the handler"))
		}
	}

	private def sendResponse(statusCode: StatusCode, body: String ): Unit ={
		val formattedResponse =  new HTTPResponse(statusCode, body)
		sendResponse(formattedResponse)
	}
	private def sendResponse(response: HTTPResponse): Unit ={
		println("sent response: \n" + response)
		out.write(response.toString.getBytes)
		out.flush()
	}
}


object ConnectionHandler{
	def apply(s: Socket, handlers: Map[(RequestMethod,String), MyJSON => (StatusCode, String)]) = {
		val bis = new BufferedInputStream(s.getInputStream)
		val bos = new BufferedOutputStream(s.getOutputStream)
		val in  = new DataInputStream(bis)
		val out = new DataOutputStream(bos)
		val ch = new ConnectionHandler(s, in, out, handlers)
		val th = new Thread(ch)
		th.start()

	}
}
