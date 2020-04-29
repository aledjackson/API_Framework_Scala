package API_Framework.data_types

object RequestMethod extends Enumeration {
	type RequestMethod = Value
	val GET, HEAD, POST, PUT, DELETE, CONNECT, OPTIONS, TRACE, PATCH = Value


	private val stringMap = Map(
	  "GET" -> RequestMethod.GET,
	  "HEAD" -> RequestMethod.HEAD,
	  "POST" -> RequestMethod.POST,
	  "PUT"-> RequestMethod.PUT,
	  "DELETE" -> RequestMethod.DELETE,
	  "CONNECT" -> RequestMethod.CONNECT,
	  "OPTIONS" -> RequestMethod.OPTIONS,
	  "TRACE" -> RequestMethod.TRACE,
	  "PATCH" -> RequestMethod.PATCH
	  )

	def stringToEnum(str: String): Either[Exception, RequestMethod] = {
		if(stringMap.contains(str)) {
			Right(stringMap(str))
		} else{
			Left(new Exception("INVALID REQUEST METHOD"))
		}
	}
}
