package services

import model.commans.CommandBody

interface CommandsService {

    fun readCommand(): CommandBody

}
