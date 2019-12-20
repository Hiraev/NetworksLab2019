package utils

import model.shop.Goods
import java.io.BufferedInputStream
import java.nio.ByteBuffer

object PacketParser {

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
