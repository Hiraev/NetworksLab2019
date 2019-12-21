package model

sealed class CommandBody {

    class Get(val name: String) : CommandBody()

    object Ls : CommandBody()

    object CdBack : CommandBody()

    class Cd(val directory: String) : CommandBody()

    class Find(val author: String) : CommandBody()

    class Add(val article: Article) : CommandBody()

    object Exit : CommandBody()

    object Undefined : CommandBody()

}

enum class Command(val code: Byte) {

    GET(0),
    LS(1),
    CD_BACK(2),
    CD(3),
    FIND(4),
    ADD(5),
    EXIT(6),
    EOF(-1),
    UNDEFINED(-10)

}