package services.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import model.Command
import model.CommandBody
import model.Node
import model.Response
import org.koin.core.logger.Logger
import services.ClientHandler
import services.CommandsHandler
import services.Store
import utils.PacketBuilder
import utils.PacketParser
import java.io.InputStream
import java.io.OutputStream
import java.net.Socket

class ClientHandlerImpl(
    private val logger: Logger,
    private val commandsHandler: CommandsHandler,
    store: Store
) : ClientHandler {

    private var node: Node.Directory = store.root()

    private lateinit var socket: Socket
    private lateinit var host: String
    private lateinit var inputStream: InputStream
    private lateinit var outputStream: OutputStream
    private var isAlive: Boolean = true

    override fun handle(socket: Socket) {
        this.socket = socket
        host = socket.inetAddress.hostName
        inputStream = socket.getInputStream()
        outputStream = socket.getOutputStream()
        GlobalScope.launch(Dispatchers.IO) {
            while (isAlive) {
                try {
                    readCommand()
                } catch (t: Throwable) {
                    logger.error("Client $host disconnected")
                    break
                }
            }
        }
    }

    override fun setDirectory(directory: Node.Directory) {
        node = directory
    }

    private suspend fun readCommand() = withContext(Dispatchers.IO) {
        val commandBody = when (PacketParser.parseCommand(inputStream)) {
            Command.GET -> CommandBody.Get(PacketParser.parseTextWithSize(inputStream))
            Command.LS -> CommandBody.Ls
            Command.CD_BACK -> CommandBody.CdBack
            Command.CD -> CommandBody.Cd(PacketParser.parseTextWithSize(inputStream))
            Command.ADD -> CommandBody.Add(PacketParser.parseArticle(inputStream))
            Command.EXIT -> CommandBody.Exit
            Command.EOF -> CommandBody.Exit
            Command.FIND -> CommandBody.Find(PacketParser.parseTextWithSize(inputStream))
            Command.UNDEFINED -> CommandBody.Undefined
        }
        if (commandBody == CommandBody.Exit) {
            logger.info("Client $host exited")
            isAlive = false
            socket.close()
            return@withContext
        }
        val response = commandsHandler.handleCommand(this@ClientHandlerImpl, commandBody, node)
        sendResponse(response)
    }

    private fun sendResponse(response: Response) {
        val byteArray = PacketBuilder.build(response)
        outputStream.write(byteArray)
        outputStream.flush()
        logger.info("[$host] ByteArray sent to client")
    }

}
