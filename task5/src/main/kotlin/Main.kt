import koin.appModule
import org.koin.core.context.startKoin
import services.ArgsParser
import services.InformationSystem
import utils.inject

fun main(args: Array<String>) {

    startKoin {
        printLogger()
        modules(appModule)
    }

    val argsParser by inject<ArgsParser>()
    val informationSystem by inject<InformationSystem>()
    val port = argsParser.parsePort(args)
    informationSystem.start(port)
}
