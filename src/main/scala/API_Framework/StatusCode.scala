package API_Framework

object StatusCode extends Enumeration {
	type StatusCode = Value
	val _100, _200, _404: Value = Value

	val meanings = Map(
		_100 -> "100 Continue",
		_200 -> "200 OK",
		_404 -> "404 Not Found"
	)


	implicit class SCValue(sc : StatusCode) {
		def asHeader: String = {
			meanings(sc)
		}
	}


}
