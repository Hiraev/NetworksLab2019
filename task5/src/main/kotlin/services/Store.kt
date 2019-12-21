package services

import model.Node

interface Store {

    fun root(): Node.Directory

}
