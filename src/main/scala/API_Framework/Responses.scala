package API_Framework

import API_Framework.data_types.{HTTPResponse, StatusCode}

object Responses {

	def INTERNAL_ERROR = new HTTPResponse(StatusCode._500,
		"There was an internal server error processing your request")

	def MALFORMED_PACKET = new HTTPResponse(StatusCode._400,
				"The HTTP Request was invalid")

	def PAGE_NOT_FOUND = new HTTPResponse(StatusCode._404,
		"The page you requested does not exist")
}
