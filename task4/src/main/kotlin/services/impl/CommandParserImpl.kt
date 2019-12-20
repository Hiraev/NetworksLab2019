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
            Command.PutGoodsToWishList -> parsePutToWishListCommand(input)
            Command.Purchase -> parsePurchaseCommand(input)
            Command.AddGoods -> parseAddGoodsCommand(input)
            Command.RemoveGoods -> parseRemoveCommand(input)
            Command.Register -> parseRegisterCommand(input)
            Command.Login -> parseLoginCommand(input)
        }
    }

    private fun parsePutToWishListCommand(input: String): CommandBody {
        val id = input.split(" ").map(String::trim).lastOrNull { it != "" }
        return try {
            CommandBody.PutGoodsToWishList(id!!.toInt())
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

    private fun parseAddGoodsCommand(input: String): CommandBody {
        val parts = input.split(" ").map(String::trim).filter { it != "" }.drop(1)
        return try {
            val id = parts[0].toInt()
            val name = parts[1]
            val price = parts[2].toInt()
            CommandBody.AddGoods(id, name, price)
        } catch (t: Throwable) {
            CommandBody.Undefined
        }
    }

    private fun parseRemoveCommand(input: String): CommandBody {
        val parts = input.split(" ").map(String::trim).filter { it != "" }.drop(1)
        return try {
            val id = parts.first().toInt()
            CommandBody.Remove(id)
        } catch (t: Throwable) {
            CommandBody.Undefined
        }
    }

    private fun parseLoginCommand(input: String): CommandBody {
        val parts = input.split(" ").map(String::trim).filter { it != "" }.drop(1)
        return try {
            val name = parts[0]
            val pass = parts[1]
            return CommandBody.Login(name, pass)
        } catch (t: Throwable) {
            CommandBody.Undefined
        }
    }

    private fun parseRegisterCommand(input: String): CommandBody {
        val parts = input.split(" ").map(String::trim).filter { it != "" }.drop(1)
        return try {
            val name = parts[0]
            val pass = parts[1]
            return CommandBody.Register(name, pass)
        } catch (t: Throwable) {
            CommandBody.Undefined
        }
    }

}
