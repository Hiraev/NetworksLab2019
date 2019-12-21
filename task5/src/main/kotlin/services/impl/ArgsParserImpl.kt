package services.impl

import services.ArgsParser

class ArgsParserImpl : ArgsParser {

    override fun parsePort(args: Array<String>): Int {
        return try {
            args[0].toInt()
        } catch (t: Throwable) {
            throw IllegalArgumentException("Format is : port")
        }
    }

}
