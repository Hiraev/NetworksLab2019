import koin.appModule
import org.koin.core.context.startKoin
import services.ArgsParser
import services.OnlineShopService
import utils.inject

fun main(args: Array<String>) {
    startKoin {
        printLogger()
        modules(appModule)
    }
    val shop by inject<OnlineShopService>()
    val argsParses by inject<ArgsParser>()
    val host = argsParses.getIpAndPort(args)
    shop.open(host)
}
