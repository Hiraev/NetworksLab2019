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
            if (command is CommandBody.Undefined) {
                logger.info("Undefined command")
                continue@loop
            } else if (command is CommandBody.Purchase) {
                logger.info("Card number is : ${command.cardNumber}")
                logger.info("Length : ${command.cardNumber.toByteArray()}")
            }
            val waitForGoods = command is CommandBody.GetAllGoods
            logger.info("Read command: $command")
            val packet = PacketBuilder.commandToByteArray(command)
            when (val answer = shopService.sendPacket(packet, waitForGoods)) {
                is Response.Goods -> {
                    logger.info("Got ${answer.goods.size} goods")
                    logger.info(answer.goods.toString())
                }
                else -> logger.info(answer.toString())
            }
        }
    }

}
