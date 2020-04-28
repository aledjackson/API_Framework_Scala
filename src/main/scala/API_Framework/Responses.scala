package API_Framework

import API_Framework.StatusCode.StatusCode

object Responses {

	val template: String =
		"HTTP/1.1 "+ StatusCode._200.asHeader +"\n" +
	  "Date: Sun, 18 Oct 2009 08:56:53 GMT\n" +
	  "Cache-Control: no cache\n" +
	  "Content-Length: 16\n" +
	  "Connection: close\n" +
	  "Content-Type: text/html\n" +
	  "\n" +
	  "this is a test 2"

	def formatResponse(sc: StatusCode, body: String): String =
		"HTTP/1.1 " + sc.asHeader + "\n" +
		"Date: " + Helper.getDateTime + "\n" +
		"Cache-Control: no cache\n" +
		"Content-Length: " + body.length +"\n" +
		"Connection: close\n" +
		"Content-Type: text/html\n" +
		"\n" +
		body

	def PAGE_NOT_FOUND : String =
	    "HTTP/1.1 404 OK\n" +
		"Date: " + Helper.getDateTime + "\n" +
		"Cache-Control: no cache\n" +
		"Content-Length: 18\n" +
		"Connection: close\n" +
		"Content-Type: text/html\n" +
		"\n" +
		"404 page not found"
}
