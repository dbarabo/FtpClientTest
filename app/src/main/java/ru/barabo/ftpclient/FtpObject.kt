package ru.barabo.ftpclient

import java.io.File
import java.util.*

private val ftpProp = FtpProp("80.92.164.204", "konordvr", "fW50A7t8zv", isPassiveMode=true)

object FtpObject : FtpInstance(ftpProp) {

    var textServer: String? = null
    private set

    var priceServer: String? = null
    private set

    var priceItems: List<List<String>>? = null

    var infoServer: String? = null
    private set

    var phoneServer: String? = null
    private set

    @Synchronized
    fun getPriceCount() = priceItems?.size?:0

    @Synchronized
    fun getPriceItem(position: Int): List<String>? =
            if(priceItems?.size?:0 > position)priceItems!![position] else null

    @Synchronized
    fun initReadServerText() {
        textServer = readTextFile("/ftp/server.txt")

        textServer = textServer?.substringBeforeLast("-----")

        priceServer = priceServer?:readPrice()

        infoServer = infoServer?:readTextFile("/ftp/info.txt")

        phoneServer = phoneServer?:infoServer?.readPhone()
    }

    private fun String.readPhone(): String? {
        val result = this.substringAfterLast('+', "").trim()

        infoServer = this.substringBeforeLast('+')

        return result
    }

    private fun readPrice(): String {
        priceServer = readTextFile("/ftp/price.txt")

        priceItems = priceServer?.lines()?.drop(1)?.map { it.split("\\.\\.\\.\\.\\.".toRegex())  }

        return priceServer!!
    }
}
