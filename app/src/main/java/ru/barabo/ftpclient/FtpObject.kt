package ru.barabo.ftpclient

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
}
