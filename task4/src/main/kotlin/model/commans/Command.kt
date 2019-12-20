package model.commans

sealed class CommandBody {

    object Undefined : CommandBody()

    object Exit : CommandBody()

    object GetAllGoods : CommandBody()

    class AddGoodsToWishList(val id: Int) : CommandBody()

    class Purchase(val cardNumber: String) : CommandBody()

}

enum class Command(val regex: Regex) {

    AddGoodsToWishList(Regex("add[ |\t]+[0-9]+")),
    GetAllGoods(Regex("get[ |\t]+list")),
    Purchase(Regex("purchase[ |\t]+([0-9]{4}-){3}([0-9]{4})")),
    Exit(Regex("exit")),
    Undefined(Regex(""))

}
