package API_Framework

import java.io.{BufferedOutputStream, DataOutputStream}
import java.text.SimpleDateFormat
import java.util.Date

object Helper {

	def hello(json: MyJSON): MyJSON = { JArray(Map("statusCode" -> JValue("200"),
												"body" -> JValue("hello")))}

	def getDateTime: String = new SimpleDateFormat("E, d MMM Y hh:mm:ss zzz").format(new Date())
}
