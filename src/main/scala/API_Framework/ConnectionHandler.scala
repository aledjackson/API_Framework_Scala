package API_Framework

import java.io.{BufferedInputStream, BufferedOutputStream, DataInputStream, DataOutputStream}
import java.net.Socket

import API_Framework.data_types.RequestMethod.RequestMethod
import API_Framework.data_types.StatusCode.StatusCode
import API_Framework.data_types.{HTTPHeader, HTTPRequest, HTTPResponse, MyJSON}
import API_Framework.exceptions.{MalformedPacketException, HandlerNotFoundException, WithinHandlerException}

class ConnectionHandler(s: Socket, in: DataInputStream, out: DataOutputStream,
						_handlers: Map[(RequestMethod,String), HTTPRequest => (StatusCode,String)]) extends Runnable{



	val handlers = new HandlerSet(_handlers);



/*	TODO: write wrappers for DataOutputStream and DataInputStream
     to make them mimic functional objects in terms of error handling */

	override def run(): Unit = {

		val errorCatch = for {
			request  <- HTTPRequest.gatherRequest(in)
			handler  <- handlers(request.header.requestMethod, request.header.route)
			response <- processRequest(handler(request))
		} yield sendResponse(response._1, response._2)

		errorCatch match {
			case Left(e: MalformedPacketException) => sendResponse(Responses.MALFORMED_PACKET)
			case Left(e: WithinHandlerException)   => sendResponse(Responses.INTERNAL_ERROR)
			case Left(e: HandlerNotFoundException) => sendResponse(Responses.PAGE_NOT_FOUND)
		}

		s.close()


	}

	//	wraps the handlers so that they won't cause a halting error
	def processRequest(handler: => (StatusCode, String)): Either[Exception, (StatusCode, String)] ={
		try{
			Right(handler)
		} catch {
//					there is good reason for this being exhaustive
			case _ => Left(new WithinHandlerException("Error occured within the handler"))
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
	def apply(s: Socket, handlers: Map[(RequestMethod,String), HTTPRequest => (StatusCode, String)]): Unit = {
		val bis = new BufferedInputStream(s.getInputStream)
		val bos = new BufferedOutputStream(s.getOutputStream)
		val in  = new DataInputStream(bis)
		val out = new DataOutputStream(bos)
		val ch = new ConnectionHandler(s, in, out, handlers)
		val th = new Thread(ch)
		th.start()

	}
}

class HandlerSet(handlers: Map[(RequestMethod,String), HTTPRequest => (StatusCode,String)]) {
	def apply(key: (RequestMethod,String)): Either[Exception, HTTPRequest => (StatusCode,String)] = {
		handlers.get(key) match {
			case Some(handler)  => Right(handler)
			case None           => Left(new HandlerNotFoundException(""))
		}
	}
}
