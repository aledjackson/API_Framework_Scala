package API_Framework.data_types

object StatusCode extends Enumeration {
	type StatusCode = Value
	val _100, _200, _400, _404, _500: Value = Value

	val meanings = Map(
		_100 -> "100 Continue",
		_200 -> "200 OK",
		_400 -> "400 Bad Request",
		_404 -> "404 Not Found",
		_500 -> "500 Internal Server Error"
	)


	implicit class SCValue(sc : StatusCode) {
		def asHeader: String = {
			meanings(sc)
		}
	}


}
