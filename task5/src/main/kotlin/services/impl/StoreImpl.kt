package services.impl

import model.Article
import model.Node
import services.Store

class StoreImpl : Store {

    private val rootNode: Node.Directory

    init {
        val rootsNodes = mutableListOf<Node>()
        val root = Node.Directory(null, "root", rootsNodes)
        val politics = mutableListOf<Node>()
        rootsNodes.add(
            Node.Directory(
                root,
                "politics",
                politics
            )
        )
        rootsNodes.add(
            Node.ArticleNode(
                root,
                Article(
                    "Malik",
                    "TheSpace",
                    "The Space is infinite"
                )
            )
        )
        politics.add(
            Node.ArticleNode(
                root,
                Article(
                    "Pasha",
                    "Politics",
                    "About politics"
                )
            )
        )
        rootNode = root
    }

    override fun root(): Node.Directory = rootNode

}
