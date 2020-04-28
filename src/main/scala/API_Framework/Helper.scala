package API_Framework

import java.io.{BufferedOutputStream, DataOutputStream}
import java.text.SimpleDateFormat
import java.util.Date

import API_Framework.StatusCode.StatusCode

object Helper {

	def hello(json: MyJSON): (StatusCode, String)= { (StatusCode._200, "I can say whatever I want")}

	def getDateTime: String = new SimpleDateFormat("E, d MMM Y hh:mm:ss zzz").format(new Date())
}
