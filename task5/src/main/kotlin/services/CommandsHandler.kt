package services

import model.CommandBody
import model.Node
import model.Response

interface CommandsHandler {

    fun handleCommand(clientHandler: ClientHandler, commandBody: CommandBody, node: Node.Directory): Response

}
