package services.impl

import model.Host
import model.commans.CommandBody
import model.shop.Response
import org.koin.core.logger.Logger
import services.CommandsService
import services.OnlineShopService
import services.ShopService
import utils.PacketBuilder
import kotlin.system.exitProcess

class OnlineShopServiceImpl(
    private val logger: Logger,
    private val commandsService: CommandsService,
    private val shopService: ShopService
) : OnlineShopService {

    override fun open(host: Host) {
        logger.info("Connecting to the online shop")
        try {
            shopService.connect(host)
            logger.info("Successfully connected to server")
        } catch (t: Throwable) {
            logger.error("Can't connect to server cause: ${t.localizedMessage}")
            exitProcess(1)
        }
        loop@ while (true) {
            val command = commandsService.readCommand()

            when (command) {
                is CommandBody.Undefined -> {
                    logger.info("Undefined command")
                    continue@loop
                }
                is CommandBody.Purchase -> {
                    logger.info("Card number is : ${command.cardNumber}")
                }
                is CommandBody.AddGoods -> {
                    logger.info("Added: {${command.id}, ${command.name}, ${command.price}}")
                }
                is CommandBody.Remove -> {
                    logger.info("Removed: ${command.id}")
                }
                is CommandBody.Exit -> {
                    logger.info("Exited")
                }
                is CommandBody.PutGoodsToWishList -> {
                    logger.info("Put in bucket: ${command.id}")
                }
                is CommandBody.GetAllGoods -> {
                    logger.info("Get all gods list")
                }
                is CommandBody.Register -> {
                    logger.info("Register")
                }
                is CommandBody.Login -> {
                    logger.info("Login")
                }
            }

            val waitForGoods = command is CommandBody.GetAllGoods

            val packet = PacketBuilder.commandToByteArray(command)

            when (val answer = shopService.sendPacket(packet, waitForGoods)) {
                is Response.Goods -> {
                    logger.info("Got ${answer.goods.size} goods")
                    logger.info(answer.goods.toString())
                }
                is Response.Success -> {
                    logger.info("Success")
                }
                is Response.Failed -> {
                    val msg = when (answer.code) {
                        1 -> "Can't find id"
                        2 -> "Permission denied"
                        3 -> "User with that name already registered"
                        4 -> "Goods already exists"
                        5 -> "Can't buy. Bucket is empty"
                        6 -> "Can't login"
                        7 -> "You are already logged in"
                        500 -> "Internal server error"
                        -1 -> exitProcess(1)
                        else -> "Undefined error"
                    }
                    logger.info("Gor error: $msg")
                }
            }
        }
    }

}
