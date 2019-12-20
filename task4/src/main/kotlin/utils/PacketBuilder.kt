package utils

import extensions.toByteArray
import model.commans.CommandBody

object PacketBuilder {

    fun commandToByteArray(commandBody: CommandBody): ByteArray = when (commandBody) {
        CommandBody.Exit -> byteArrayOf(0)
        CommandBody.GetAllGoods -> byteArrayOf(1)
        is CommandBody.PutGoodsToWishList -> byteArrayOf(2) + commandBody.id.toByteArray()
        is CommandBody.Purchase -> byteArrayOf(3) + commandBody.cardNumber.toByteArray()
        is CommandBody.AddGoods -> byteArrayOf(6) + 1.toByteArray() + getGoodsByteArray(commandBody)
        is CommandBody.Remove -> byteArrayOf(7) + commandBody.id.toByteArray()
        is CommandBody.Register -> byteArrayOf(4) + getNameAndPassword(commandBody.name, commandBody.password)
        is CommandBody.Login -> byteArrayOf(5) + getNameAndPassword(commandBody.name, commandBody.password)
        else -> throw IllegalArgumentException("Can't build packet for passed CommandBody subclass")
    }

    private fun getNameAndPassword(name: String, password: String): ByteArray {
        val nameByteArray = name.toByteArray()
        val passByteArray = password.toByteArray()
        return nameByteArray.size.toByteArray() + nameByteArray + passByteArray.size.toByteArray() + passByteArray
    }

    private fun getGoodsByteArray(addGoods: CommandBody.AddGoods): ByteArray {
        val nameArray = addGoods.name.toByteArray()
        return addGoods.id.toByteArray() + nameArray.size.toByteArray() + nameArray + addGoods.price.toByteArray()
    }

}
