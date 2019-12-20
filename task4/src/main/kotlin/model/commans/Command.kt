package model.commans

sealed class CommandBody {

    object Undefined : CommandBody()

    object Exit : CommandBody()

    object GetAllGoods : CommandBody()

    class PutGoodsToWishList(val id: Int) : CommandBody()

    class Purchase(val cardNumber: String) : CommandBody()

    class Remove(val id: Int) : CommandBody()

    class AddGoods(val id: Int, val name: String, val price: Int) : CommandBody()

    class Login(val name: String, val password: String) : CommandBody()

    class Register(val name: String, val password: String) : CommandBody()

}

enum class Command(val regex: Regex) {

    Register(Regex("register[ |\t][A-Za-z_А-Яа-я]{1,16}[ |\t]+[A-Za-z_А-Яа-я]{6,16}")),
    Login(Regex("login[ |\t][A-Za-z_А-Яа-я]{1,16}[ |\t]+[A-Za-z_А-Яа-я]{6,16}")),
    PutGoodsToWishList(Regex("put[ |\t]+[0-9]+")),
    GetAllGoods(Regex("get[ |\t]+list")),
    Purchase(Regex("purchase[ |\t]+([0-9]{4}-){3}([0-9]{4})")),
    AddGoods(Regex("add[ |\t][0-9]{1,9}[ |\t][A-Za-zА-Яа-я\\-]{1,100}[ |\t][0-9]{1,9}")),
    RemoveGoods(Regex("remove[ |\t][0-9]{1,9}")),
    Exit(Regex("exit")),
    Undefined(Regex(""));

}
