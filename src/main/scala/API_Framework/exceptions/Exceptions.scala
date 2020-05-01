package API_Framework.exceptions

class MalformedPacketException(msg: String) extends Exception(msg)

class HandlerNotFoundException(msg: String) extends Exception(msg)

class WithinHandlerException(msg: String) extends Exception(msg)

