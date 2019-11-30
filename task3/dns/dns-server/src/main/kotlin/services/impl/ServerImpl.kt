package services.impl

import data.ConfigProvider
import domain.model.DNSFlags
import domain.model.DNSHeader
import domain.model.DNSPacket
import domain.model.DNSResourceRecord
import domain.model.enums.DNSMessageType
import domain.model.enums.DNSOpCode
import domain.model.enums.DNSQueryType
import domain.model.enums.DNSRCode
import org.koin.core.logger.Logger
import services.Server
import utils.DNSPacketBuilder
import utils.DNSPacketCompressor
import utils.inject
import java.net.InetSocketAddress
import java.net.SocketAddress
import java.nio.ByteBuffer
import java.nio.channels.DatagramChannel

class ServerImpl : Server {

    companion object {
        private const val MAX_SIZE = 65_512
    }

    private val provider: ConfigProvider by inject()
    private val logger: Logger by inject()
    private val zoneInfo = provider.provideZoneInfo()

    private val channel = DatagramChannel.open().bind(InetSocketAddress(53))
    private val buffer = ByteBuffer.allocate(MAX_SIZE)

    override fun start() {
        while (true) {
            try {
                val clientAddr = channel.receive(buffer)
                val copiedReceivedData = copyReceivedData()
                buffer.flip()
                // Ready to async handler
                handleQuery(clientAddr, copiedReceivedData)
            } catch (t: Throwable) {
                buffer.flip()
                logger.error(t.localizedMessage)
                continue
            }
        }
    }

    private fun copyReceivedData() = buffer.array().copyOfRange(0, buffer.position())

    private fun handleQuery(clientAddr: SocketAddress, receivedData: ByteArray) {
        val receivedPacket = DNSPacketBuilder.build(ByteBuffer.wrap(receivedData))
        logger.info("Received queries (id: ${receivedPacket.header.id}): ${receivedPacket.queries}")
        val packet = buildAnswers(receivedPacket)
        channel.send(ByteBuffer.wrap(DNSPacketCompressor.compress(packet)), clientAddr)
    }

    private fun buildAnswers(dnsQueryPacket: DNSPacket): DNSPacket {
        val id = dnsQueryPacket.header.id
        val queryHasUnsupportedType = dnsQueryPacket.queries.find { it.type == DNSQueryType.UNDEFINED } != null
        val answers = dnsQueryPacket.queries.flatMap { query ->
            zoneInfo.filter { it.type == query.type && query.name == it.name }
        }
        return if (answers.isEmpty()) {
            if (queryHasUnsupportedType) {
                logger.info("${DNSRCode.UNSUPPORTED} for id: $id")
                buildUnsupportedType(id)
            } else {
                logger.info("${DNSRCode.NAME_ERROR} for id: $id")
                buildNotFound(id)
            }
        } else {
            val res = wrap(id, answers, DNSRCode.NO_ERROR)
            logger.info("${DNSRCode.NO_ERROR} for id: $id")
            logger.info(res.toString())
            res
        }
    }

    private fun buildUnsupportedType(id: Short) = wrap(id, emptyList(), DNSRCode.UNSUPPORTED)

    private fun buildNotFound(id: Short) = wrap(id, emptyList(), DNSRCode.NAME_ERROR)

    private fun buildInternalServerError(id: Short) = wrap(id, emptyList(), DNSRCode.INTERNAL_ERROR)

    private fun wrap(id: Short, resourceRecordList: List<DNSResourceRecord>, rCode: DNSRCode) = DNSPacket(
        header = DNSHeader(
            id = id,
            flags = DNSFlags(
                qr = DNSMessageType.ANSWER,
                opCode = DNSOpCode.STANDARD,
                aa = true,
                tc = false,
                ra = false,
                rd = false,
                rCode = rCode
            ),
            numOfAnswers = resourceRecordList.size.toShort(),
            numOfQueries = 0,
            numOfAdditionalAnswers = 0,
            numOfAuthorityAnswers = 0
        ),
        queries = emptyList(),
        answers = resourceRecordList,
        additionalAnswers = emptyList(),
        authorityAnswers = emptyList()
    )

}