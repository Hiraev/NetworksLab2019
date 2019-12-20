package services

import model.shop.Response

interface ShopService {

    fun connect()

    fun disconnect()

    fun sendPacket(packet: ByteArray, waitForGoods: Boolean): Response

}
