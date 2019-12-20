package services.impl

import model.commans.Command
import model.commans.CommandBody
import services.CommandParser

class CommandParserImpl : CommandParser {

    override fun parse(input: String): CommandBody {
        val commandBodyClass = Command.values()
            .find { input.matches(it.regex) }
            ?: Command.Undefined
        return when (commandBodyClass) {
            Command.Exit -> CommandBody.Exit
            Command.Undefined -> CommandBody.Undefined
            Command.GetAllGoods -> CommandBody.GetAllGoods
            Command.AddGoodsToWishList -> parseAddToWishListCommand(input)
            Command.Purchase -> parsePurchaseCommand(input)
        }
    }

    private fun parseAddToWishListCommand(input: String): CommandBody {
        val id = input.split(" ").map(String::trim).lastOrNull { it != "" }
        return try {
            CommandBody.AddGoodsToWishList(id!!.toInt())
        } catch (t: Throwable) {
            CommandBody.Undefined
        }
    }

    private fun parsePurchaseCommand(input: String): CommandBody {
        val creditCardNumber = input.split(" ").map(String::trim).lastOrNull { it != "" }?.filter { it != '-' }
        return try {
            CommandBody.Purchase(creditCardNumber!!)
        } catch (t: Throwable) {
            CommandBody.Undefined
        }
    }

}
