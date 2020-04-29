package API_Framework.data_types

import java.io.DataInputStream

class HTTPRequest (val header: HTTPHeader, val body: String)

object HTTPRequest {
	def gatherRequest(in: DataInputStream): Either[Exception, HTTPRequest] = {
		for {
			header <- HTTPHeader.gatherHeader(in)
			body   <- header.gatherBody(in)
		} yield {new HTTPRequest(header,body)}
	}

}
