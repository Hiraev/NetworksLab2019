package services

import model.Host
import model.shop.Response

interface ShopService {

    fun connect(host: Host)

    fun disconnect()

    fun sendPacket(packet: ByteArray, waitForGoods: Boolean): Response

}
