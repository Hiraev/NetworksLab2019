package services

import model.commans.CommandBody

interface CommandParser {

    fun parse(input: String): CommandBody

}
