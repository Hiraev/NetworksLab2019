package model.shop

sealed class Response {

    object Success : Response()

    object Failed : Response()

    class Goods(val goods: List<model.shop.Goods>) : Response()

}
