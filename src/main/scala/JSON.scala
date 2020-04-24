abstract class JSON
case class JObject(value: Map[String, JSON])
case class JArray(value: Array[JObject])
case class JValue(value: String)
// a method for parsing html headers from a packet
def headersToJSON(packet: String): Either[JSON, Exception]  = {
  val lines = packet.split("\n")


  def parseLine(line: String) = {}
}