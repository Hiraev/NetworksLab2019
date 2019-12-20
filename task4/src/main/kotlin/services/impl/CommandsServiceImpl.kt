package services.impl

import model.commans.CommandBody
import services.CommandParser
import services.CommandsService
import java.util.*

class CommandsServiceImpl(
    private val commandParser: CommandParser
) : CommandsService {

    override fun readCommand(): CommandBody = commandParser.parse(Scanner(System.`in`).nextLine())

}
