package koin

import org.koin.core.logger.Logger
import org.koin.core.logger.PrintLogger
import org.koin.dsl.module
import services.*
import services.impl.*

val appModule = module {
    single<Logger> { PrintLogger() }
    single<ArgsParser> { ArgsParserImpl() }
    single<InformationSystem> { InformationSystemImpl(get()) }
    single<Store> { StoreImpl() }
    single<CommandsHandler> { CommandHandlerImpl(get()) }
    factory<ClientHandler> { ClientHandlerImpl(get(), get(), get()) }
}
