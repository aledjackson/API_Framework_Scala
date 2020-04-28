package API_Framework

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

	def formatJSON(json: MyJSON): String =
		"HTTP/1.1 404 Not Found\n" +
		"Date: " + Helper.getDateTime + "\n"
		"Cache-Control: no cache\n" +
		"Content-Length: 18\n" +
		"Connection: close\n" +
		"Content-Type: text/html\n" +
		"\n" +
		"404 page not found"

	def PAGE_NOT_FOUND : String =
	  "HTTP/1.1 404 OK\n" +
		"Date: " + Helper.getDateTime + "\n"
		"Cache-Control: no cache\n" +
		"Content-Length: 18\n" +
		"Connection: close\n" +
		"Content-Type: text/html\n" +
		"\n" +
		"404 page not found"
}
