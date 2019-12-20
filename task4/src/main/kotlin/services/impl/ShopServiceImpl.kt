package services.impl

import model.shop.Response
import org.koin.core.logger.Logger
import services.ShopService
import utils.PacketBuilder
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.net.Socket

class ShopServiceImpl(
    private val logger: Logger
) : ShopService {

    private lateinit var connection: Socket
    private lateinit var outputStream: BufferedOutputStream
    private lateinit var inputStream: BufferedInputStream

    override fun connect() {
        connection = Socket("172.20.10.6", 5000)
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
        if (result is Response.Failed) return Response.Failed
        return if (waitForGoods) getGoods() else result
    }

    private fun getResult() = if (inputStream.read() == 0) Response.Success else Response.Failed

    private fun getGoods() = Response.Goods(PacketBuilder.parseAllGoods(inputStream))

}
