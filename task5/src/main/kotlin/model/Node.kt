package model

sealed class Node(val parent: Directory?) {

    class Directory(parent: Directory?, val name: String, val list: MutableList<Node>) : Node(parent)

    class ArticleNode(parent: Directory?, val article: Article) : Node(parent)

}
