import koin.appModule
import org.koin.core.context.startKoin
import services.OnlineShopService
import utils.inject

fun main() {
    startKoin {
        printLogger()
        modules(appModule)
    }
    val shop by inject<OnlineShopService>()
    shop.open()
}
