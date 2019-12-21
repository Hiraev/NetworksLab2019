package extensions

fun Int.lowestShort() = toShort()

fun Int.highestShort() = ushr(Short.SIZE_BITS).toShort()

fun Int.toByteArray() = byteArrayOf(
    highestShort().highestByte(),
    highestShort().lowestByte(),
    lowestShort().highestByte(),
    lowestShort().lowestByte()
)
