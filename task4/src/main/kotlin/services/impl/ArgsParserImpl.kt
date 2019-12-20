package services.impl

import model.Host
import services.ArgsParser

class ArgsParserImpl : ArgsParser {

    companion object {
        private const val FORMAT_INFO_MESSAGE = "Use format: hostname port"
    }

    override fun getIpAndPort(args: Array<String>): Host {
        if (args.size != 2) throw IllegalArgumentException(FORMAT_INFO_MESSAGE)
        val hostName = args[0]
        val port = args[1].toInt()
        return Host(hostName, port)
    }

}
