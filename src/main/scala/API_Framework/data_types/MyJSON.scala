package API_Framework.data_types

abstract class MyJSON {
	def toString(i: Int, indent: Int = 4): String
	def toString(indent: Int): String = this.toString(0, indent)
	override def toString: String = this.toString(0)
}


case class JArray(value: Map[String,MyJSON]) extends MyJSON{
	//	this is not tail recursive because if you have an object so big it'll cause an overflow
	//	you probably shouldn't be printing it
	override def toString(tbs: Int, indent: Int = 4):String = {
		val sb = new StringBuilder()
		sb.append(f"[\n" )
		for ( (k, v) <- this.value){sb.append( "-"*tbs + k + " : " + v.toString(tbs + indent, indent) + "\n")}
		sb.append("-"*(tbs - indent) + "]")
		sb.toString()
	}
}
case class JValue(value: String) extends MyJSON{
	override def toString(tbs: Int, indent: Int = 4): String = {
		this.toString
	}
	override def toString: String = {
		this.value
	}
}
// a method for parsing html headers from a packet

