package koin

import org.koin.core.logger.Logger
import org.koin.core.logger.PrintLogger
import org.koin.dsl.module
import services.ArgsParser
import services.CommandParser
import services.CommandsService
import services.OnlineShopService
import services.ShopService
import services.impl.ArgsParserImpl
import services.impl.CommandParserImpl
import services.impl.CommandsServiceImpl
import services.impl.OnlineShopServiceImpl
import services.impl.ShopServiceImpl

val appModule = module {
    single<CommandParser> { CommandParserImpl() }
    single<CommandsService> { CommandsServiceImpl(get()) }
    single<Logger> { PrintLogger() }
    single<ShopService> { ShopServiceImpl(get()) }
    single<OnlineShopService> { OnlineShopServiceImpl(get(), get(), get()) }
    single<ArgsParser> { ArgsParserImpl() }
}
