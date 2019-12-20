package utils

import extensions.toByteArray
import model.commans.CommandBody
import model.shop.Goods
import java.io.BufferedInputStream
import java.nio.ByteBuffer

object PacketBuilder {

    fun commandToByteArray(commandBody: CommandBody): ByteArray = when (commandBody) {
        CommandBody.Exit -> byteArrayOf(0)
        CommandBody.GetAllGoods -> byteArrayOf(1)
        is CommandBody.AddGoodsToWishList -> byteArrayOf(2) + commandBody.id.toByteArray()
        is CommandBody.Purchase -> byteArrayOf(3) + commandBody.cardNumber.toByteArray()
        else -> throw IllegalArgumentException("Can't build packet for passed CommandBody subclass")
    }

    fun parseAllGoods(inputStream: BufferedInputStream): List<Goods> {
        val sizeArray = List(4) { inputStream.read().toByte() }.toByteArray()
        val goodsNum = ByteBuffer.wrap(sizeArray).int
        return List(goodsNum) {
            parseGoods(inputStream)
        }
    }

    private fun parseGoods(inputStream: BufferedInputStream): Goods {
        val idArray = List(4) { inputStream.read().toByte() }.toByteArray()
        val id = ByteBuffer.wrap(idArray).int.toString()
        val name = parseGoodsName(inputStream)

        val price = ByteBuffer.wrap(List(4) { inputStream.read().toByte() }.toByteArray()).int.toString()
        return Goods(id, name, price)
    }

    private fun parseGoodsName(inputStream: BufferedInputStream): String {
        val nameArray = List(4) { inputStream.read().toByte() }.toByteArray()
        val nameSize = ByteBuffer.wrap(nameArray).int
        return String(List(nameSize) {
            inputStream.read().toByte()
        }.toByteArray())
    }

}
