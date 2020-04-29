package API_Framework.data_types

import java.io.DataInputStream

import API_Framework.Helpers.Int

import API_Framework.{Framework, Helpers}
import RequestMethod.RequestMethod
import com.fasterxml.jackson.annotation.ObjectIdGenerators.StringIdGenerator
import sun.security.util.Length

import scala.annotation.tailrec


class HTTPHeader(val requestMethod: RequestMethod,
				 val route: String,
				 val url: String,
				 val headers: Map[String,String]){



	def gatherBody(in: DataInputStream): Either[Exception, String] = {

		//	TODO: introduce a time out to close connection and reserve server computation
		@tailrec def gb(in: DataInputStream, sb: StringBuilder, length: Int): String = {
			if (length <= 0) {
				gb(in, sb += in.readChar(), length - 1)
			} else{
				sb.toString()
			}
		}

		requestMethod match {
			case RequestMethod.GET  => Right("")
			case RequestMethod.POST => headers.get("content-length") match {
				case Some(Int(x)) => Right(gb(in, new StringBuilder(), x.toInt))
				case Some(_)      => Left(new Exception("invalid content-length format"))
				case None         => Left(new Exception("a content length was not given"))
			}
		}



	}
}

object HTTPHeader{
//	TODO: add exceptions ( there will be exceptions )

	def apply(packet: String): Either[Exception, HTTPHeader] = {
		val lines = packet.split("\n")
		val headers = lines.slice(0, lines.length - 1).toList
		val json = parseHeaders(headers)
//		need some validation here
		val url = getURL(lines(0))
		val route = getRoute(url)
		val method = getMethod(lines(0))
		for {
			method_val <- method
			route_val  <- route
			json_val   <- json
		} yield new HTTPHeader(method_val, route_val, url, json_val)
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



	def gatherHeader(in: DataInputStream): Either[Exception, HTTPHeader] = {

		@tailrec def _gh(in: DataInputStream, string: StringBuilder,
						 newLineCount: Int, length: Int):Either[Exception, String] = {
			if(length > Framework.MAX_HEADER_SIZE){
				Left(new Exception("Malformed Packet, header too big"))
			}
			else if(newLineCount < 2) {
				val char = in.readByte().asInstanceOf[Char]
				char match {
					case '\n'  => _gh(in, string += '\n', newLineCount + 1, length + 1)
					case '\r'  => _gh(in, string += '\r', newLineCount, length + 1)
					case other => _gh(in, string += other, 0, length + 1)
				}
			} else {
				Right(string.toString())
			}
		}

		val header = _gh(in, new StringBuilder(), 0, 0)


		header match{
			case Left(exception) => Left(exception) // TODO: THIS EXCEPTION IS NOT DEALT WITH
			case Right(str)      => HTTPHeader(str)
		}

	}


	private def getURL(firstLine: String) = firstLine.split(" ")(1)
	private def getMethod(firstLine:String) = {
			RequestMethod.stringToEnum(firstLine.split(" ")(0))
	}
	private def parseHeaders(lines: List[String]): Either[Exception, Map[String,String]] ={
		@tailrec
		def pH(lines: List[String], output: Map[String,String]): Either[Exception, Map[String,String]] = {
			lines match{
				case x :: xs  =>  parseLine(x) match {
					case Left(e) => Left(e)
					case Right((key,json))  => pH(xs, output + (key -> json))
				}
				case _        => Right(output)
			}
		}

		def parseLine(line: String): Either[Exception, (String, String)] = {
			val split = line.split(":")
			if (split.length == 1){
				for( x <- split) println(x)
				Left(new Exception("Malformed header"))
			} else{
				val value = split.tail.mkString
				val key = split.head
				Right(
					(key, value)
				)
			}
		}


		val map = Map( "url" -> getURL(lines.head))
		pH(lines.tail, map)
	}






}
