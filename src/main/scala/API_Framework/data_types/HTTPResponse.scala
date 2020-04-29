package API_Framework.data_types

import java.text.SimpleDateFormat
import java.util.Date

import StatusCode.StatusCode

class HTTPResponse(sc: StatusCode, body: String) {
	val contentLength: Int = body.length
	val cacheControl: Boolean = false
	private val cacheControl_  = {
		cacheControl match {
			case false  => "no cache"
			case true   => ???
		}
	}
	val date: String = getDateTime
	val connection = "close"
	val contentType = "text/html"

	def getDateTime: String = new SimpleDateFormat("E, d MMM Y hh:mm:ss zzz").format(new Date())

	override def toString: String = {
		"HTTP/1.1 " + sc.asHeader + "\n" +
		"Date: " + date + "\n" +
		"Cache-Control: " + cacheControl_ + "\n" +
		"Content-Length: "+ contentLength + "\n" +
		"Connection: "+ connection +"\n" +
		"Content-Type: "+ contentType + "\n" +
		"\n" +
		body
	}

}
