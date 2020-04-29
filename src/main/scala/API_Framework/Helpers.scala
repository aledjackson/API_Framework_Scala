package API_Framework

object Helpers {
	object Int {
		def unapply(s: String): Option[Int] = util.Try(s.toInt).toOption
	}
}
