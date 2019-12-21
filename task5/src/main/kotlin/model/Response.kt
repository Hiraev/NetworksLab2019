package model

sealed class Response {

    object Success : Response()

    class Failed(val code: Byte) : Response()

    class Nodes(val nodes: List<Node>) : Response()

    class ArticleNode(val artice: Article) : Response()

}
