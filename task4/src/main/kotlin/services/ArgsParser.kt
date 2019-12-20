package services

import model.Host

interface ArgsParser {

    fun getIpAndPort(args: Array<String>): Host

}
