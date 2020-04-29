package API_Framework

import java.io.{BufferedOutputStream, DataOutputStream}
import java.text.SimpleDateFormat
import java.util.Date

import API_Framework.data_types.StatusCode.StatusCode
import API_Framework.data_types.{MyJSON, StatusCode}

object DefaultResponse {

	def hello(json: MyJSON): (StatusCode, String)= {(StatusCode._200, "I can say whatever I want")}


}
