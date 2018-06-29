package ru.barabo.ftpclient

import org.apache.commons.net.ftp.FTPFile
import java.io.File

interface Ftp {

    fun downloadFile(remoteFullPath: String, localFile: File)

    fun listFiles(ftpPath: String): Array<FTPFile>?
}