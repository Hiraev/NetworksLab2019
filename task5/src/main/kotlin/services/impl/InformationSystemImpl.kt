package services.impl

import org.koin.core.logger.Logger
import services.ClientHandler
import services.InformationSystem
import utils.inject
import java.net.ServerSocket

class InformationSystemImpl(
    private val logger: Logger
) : InformationSystem {

    lateinit var serverSocket: ServerSocket

    override fun start(port: Int) {
        logger.info("Starting server on port: $port")
        try {
            serverSocket = ServerSocket(port)
        } catch (t: Throwable) {
            logger.error("Error on opening serve socket cause: ${t.localizedMessage}")
        }
        while (true) {
            val client = serverSocket.accept()
            logger.info("Accepted client with ip: ${client.inetAddress.hostName}")
            val clientHandler by inject<ClientHandler>()
            clientHandler.handle(client)
        }
    }

}
