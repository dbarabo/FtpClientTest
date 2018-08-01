package ru.barabo.ftpclient

import java.io.File
import java.util.*

private val ftpProp = FtpProp("80.92.164.204", "konordvr", "fW50A7t8zv", isPassiveMode=true)

enum class FileTypeFtp {
    DIRECTORY,
    FILE,
    NULL;

    companion object {
        fun getFileTypeFtp(isDirectory: Boolean) = if(isDirectory) DIRECTORY else FILE
    }
}

typealias FileNameType = Pair<String, FileTypeFtp>

object FtpObject : FtpInstance(ftpProp) {

    private var activeDir: List<FileNameType>? = null

    var textServer: String? = null
    private set

    var priceServer: String? = null
    private set

    var infoServer: String? = null
    private set

    var phoneServer: String? = null
    private set

    @Synchronized
    fun initActiveDir() {
        activeDir = getRootListFiles()
    }

    @Synchronized
    fun getRootCountFiles(): Int = activeDir?.size ?: 0

    @Synchronized
    fun getActiveDirItemByIndex(index: Int): FileNameType =
            if(index >= 0 && activeDir?.size?:0 > index ) activeDir!![index] else FileNameType("", FileTypeFtp.NULL)

    private fun getRootListFiles(): List<FileNameType> {

        val result = ArrayList<FileNameType>()

        this.listFiles("")?.forEach {
            result += Pair(it.name, FileTypeFtp.getFileTypeFtp(it.isDirectory))
        }

        return result
    }

    @Synchronized
    fun initReadServerText() {
        textServer = readTextFile("/ftp/server.txt")

        textServer = textServer?.substringBeforeLast("-----")

        priceServer = priceServer?:readTextFile("/ftp/price.txt")

        infoServer = infoServer?:readTextFile("/ftp/info.txt")

        phoneServer = phoneServer?:infoServer?.readPhone()
    }

    private fun String.readPhone(): String? {
        val result = this.substringAfterLast('+', "").trim()

        infoServer = this.substringBeforeLast('+')

        return result
    }
}
