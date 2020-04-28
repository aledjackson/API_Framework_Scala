package API_Framework

import java.io.DataInputStream

import API_Framework.RequestMethod.RequestMethod

import scala.annotation.tailrec
import scala.collection.mutable.ListBuffer
import scala.util.matching.Regex.Match


class HTTPRequest(val requestMethod: RequestMethod,
				  val route: String,
				  val url: String,
				  val obj: MyJSON)

object HTTPRequest{
//	TODO: add exceptions ( there will be exceptions )

	def apply(packet: String): Either[Exception, HTTPRequest] = {
		val lines = packet.split("\n")
		val headers = lines.slice(0, lines.length - 1).toList
		val json = generateJSON(headers)
//		need some validation here
		val url = getURL(lines(0))
		val route = getRoute(url)
		val method = getMethod(lines(0))
		for {
			method_val <- method
			route_val  <- route
			json_val   <- json
		} yield new HTTPRequest(method_val, route_val, url, json_val)
	}


	private def getRoute(url: String): Either[Exception, String] = {
		//			method route protocol
		val statusAndQuery = url.split("""\?""")

		//	need to improve this regex
		val validRoute = """(/.*)""".r

		statusAndQuery match {
			case Array(validRoute(x),_) => Right(x)
			case Array(validRoute(x))   => Right(x)
			case _ 						=> Left(new Exception("Malformed URL"))
		}

	}

	def gatherPacket(in: DataInputStream): String = {
	  @tailrec def _gp(in: DataInputStream, string: StringBuilder, newLineCount: Int): String = {
		if(newLineCount < 2) {
		  val char = in.readByte().asInstanceOf[Char]
		  char match {
			case '\n'  => _gp(in, string += '\n', newLineCount + 1)
			case '\r'  => _gp(in, string += '\r', newLineCount)
			case other => _gp(in, string += other, 0)
		  }
		} else {
		  string.toString()
		}
	  }

	  _gp(in, new StringBuilder(), 0)
	}

	private def getURL(firstLine: String) = firstLine.split(" ")(1)
	private def getMethod(firstLine:String) = {
			RequestMethod.stringToEnum(firstLine.split(" ")(0))
	}

	private def generateJSON(lines: List[String]): Either[Exception, MyJSON] ={
		@tailrec
		def pH(lines: List[String], output: Map[String,MyJSON]): Either[Exception, MyJSON] = {
			lines match{
				case x :: xs  =>  parseLine(x) match {
					case Left(e) => Left(e)
					case Right((key,json))  => pH(xs, output + (key -> json))
				}
				case _        => Right(JArray(output))
			}
		}

		def parseLine(line: String): Either[Exception, (String, MyJSON)] = {
			val split = line.split(":")
			if (split.length == 1){
				for( x <- split) println(x)
				Left(new Exception("Malformed header"))
			} else{
				val value = JValue(split.tail.mkString)
				val key = split.head
				Right(
					(key, value)
				)
			}
		}


		val map = Map( "url" -> JValue(getURL(lines.head)))
		pH(lines.tail, map)
	}






}
