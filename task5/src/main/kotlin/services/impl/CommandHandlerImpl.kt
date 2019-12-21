package services.impl

import model.Article
import model.CommandBody
import model.Node
import model.Response
import org.koin.core.logger.Logger
import services.ClientHandler
import services.CommandsHandler
import java.util.concurrent.locks.ReentrantLock


class CommandHandlerImpl(
    private val logger: Logger
) : CommandsHandler {

    private val lock = ReentrantLock()

    override fun handleCommand(clientHandler: ClientHandler, commandBody: CommandBody, node: Node.Directory): Response {
        lock.lock()
        val response = when (commandBody) {
            is CommandBody.Ls -> ls(node)
            is CommandBody.CdBack -> cdBack(clientHandler, node)
            is CommandBody.Cd -> cd(clientHandler, node, commandBody.directory)
            is CommandBody.Find -> find(node, commandBody.author)
            is CommandBody.Add -> add(node, commandBody.article)
            is CommandBody.Get -> get(node, commandBody.name)
            else -> Response.Failed(50)
        }
        logger.info("Handled command: ${commandBody.javaClass}")
        lock.unlock()
        return response
    }

    private fun get(node: Node.Directory, name: String): Response {
        val article = node.list.filterIsInstance(Node.ArticleNode::class.java).find {
            it.article.name == name
        }
        return if (article != null) {
            Response.ArticleNode(article.article)
        } else {
            Response.Failed(5)
        }
    }

    private fun add(node: Node.Directory, article: Article): Response {
        val exists = node.list.filterIsInstance(Node.ArticleNode::class.java).find {
            it.article.name == article.name
        } != null
        return if (!exists) {
            node.list.add(Node.ArticleNode(node, article))
            Response.Success
        } else {
            Response.Failed(4)
        }
    }

    private fun find(node: Node.Directory, author: String): Response {
        val list = node.list.filterIsInstance(Node.ArticleNode::class.java).filter { it.article.author == author }
        return if (list.isNotEmpty()) {
            Response.Nodes(list)
        } else {
            Response.Failed(3)
        }
    }

    private fun ls(node: Node.Directory): Response {
        return Response.Nodes(node.list)
    }

    private fun cdBack(clientHandler: ClientHandler, node: Node.Directory): Response {
        return if (node.parent != null) {
            clientHandler.setDirectory(node.parent)
            Response.Success
        } else {
            Response.Failed(1)
        }
    }

    private fun cd(clientHandler: ClientHandler, node: Node.Directory, directoryName: String): Response {
        val newDir = node.list.filterIsInstance(Node.Directory::class.java).find { it.name == directoryName }
        return if (newDir != null) {
            clientHandler.setDirectory(newDir)
            Response.Success
        } else {
            Response.Failed(2)
        }
    }

}