package services.impl

import model.Host
import model.shop.Response
import org.koin.core.logger.Logger
import services.ShopService
import utils.PacketParser
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.net.Socket

class ShopServiceImpl(
    private val logger: Logger
) : ShopService {

    private lateinit var connection: Socket
    private lateinit var outputStream: BufferedOutputStream
    private lateinit var inputStream: BufferedInputStream

    override fun connect(host: Host) {
        connection = Socket(host.host, host.port)
        if (connection.isConnected) {
            logger.info("Successfully connected")
        } else {
            logger.info("Failed")
        }
        outputStream = BufferedOutputStream(connection.getOutputStream())
        inputStream = BufferedInputStream(connection.getInputStream())
    }

    override fun disconnect() {
        connection.close()
    }

    override fun sendPacket(packet: ByteArray, waitForGoods: Boolean): Response {
        outputStream.write(packet)
        outputStream.flush()
        logger.info("Packet sent, waitForGoods: $waitForGoods")
        val result = getResult()
        if (result is Response.Failed) return result
        return if (waitForGoods) getGoods() else result
    }

    private fun getResult(): Response {
        val result = inputStream.read()
        return if (result == 0) Response.Success else Response.Failed(result)
    }

    private fun getGoods() = Response.Goods(PacketParser.parseAllGoods(inputStream))

}
