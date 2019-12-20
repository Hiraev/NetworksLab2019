package model.shop

sealed class Response {

    object Success : Response()

    class Failed(val code: Int) : Response()

    class Goods(val goods: List<model.shop.Goods>) : Response()

}
