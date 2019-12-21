package services

import model.Node
import java.net.Socket

interface ClientHandler {

    fun handle(socket: Socket)

    fun setDirectory(directory: Node.Directory)

}
