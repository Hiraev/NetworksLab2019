package utils

import extensions.toByteArray
import model.Article
import model.Node
import model.Response

object PacketBuilder {

    fun build(response: Response): ByteArray = when (response) {
        is Response.Success -> byteArrayOf(0)
        is Response.Failed -> byteArrayOf(response.code)
        is Response.Nodes -> byteArrayOf(0) + buildNodes(response)
        is Response.ArticleNode -> byteArrayOf(0) + buildArticle(response.artice)
    }

    private fun buildNodes(nodes: Response.Nodes): ByteArray =
        nodes.nodes.size.toByteArray() + nodes.nodes
            .map(PacketBuilder::buildNode)
            .reduce { acc, bytes -> acc + bytes }

    private fun buildNode(node: Node): ByteArray = when (node) {
        is Node.Directory -> byteArrayOf(0) + buildDirectoryNode(node)
        is Node.ArticleNode -> byteArrayOf(1) + buildArticleNode(node)
    }

    private fun buildDirectoryNode(node: Node.Directory): ByteArray {
        val nameByteArray = node.name.toByteArray()
        return nameByteArray.size.toByteArray() + nameByteArray
    }

    private fun buildArticleNode(node: Node.ArticleNode): ByteArray {
        val nameByteArray = node.article.name.toByteArray()
        val authorByteArray = node.article.author.toByteArray()
        return nameByteArray.size.toByteArray() + nameByteArray + authorByteArray.size.toByteArray() + authorByteArray
    }

    private fun buildArticle(article: Article): ByteArray {
        val nameByteArray = article.name.toByteArray()
        val textByteArray = article.text.toByteArray()
        val authorByteArray = article.author.toByteArray()
        return authorByteArray.size.toByteArray() + authorByteArray + nameByteArray.size.toByteArray() + nameByteArray + textByteArray.size.toByteArray() + textByteArray
    }

}
