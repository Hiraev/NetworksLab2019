package utils

import model.Article
import model.Command
import java.io.InputStream
import java.nio.ByteBuffer

object PacketParser {

    fun parseCommand(inputStream: InputStream): Command {
        val code = inputStream.read().toByte()
        return Command.values().find { code == it.code } ?: Command.UNDEFINED
    }

    fun parseArticle(inputStream: InputStream): Article {
        val author = parseTextWithSize(inputStream)
        val name = parseTextWithSize(inputStream)
        val text = parseTextWithSize(inputStream)
        return Article(author, name, text)
    }

    fun parseTextWithSize(inputStream: InputStream): String {
        val textSizeArray = List(4) {
            inputStream.read().toByte()
        }.toByteArray()
        val textSize = ByteBuffer.wrap(textSizeArray).int

        return String(
            List(textSize) { inputStream.read().toByte() }.toByteArray()
        )
    }

}
