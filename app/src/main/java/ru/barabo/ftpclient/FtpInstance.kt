package ru.barabo.ftpclient

import android.util.Log
import org.apache.commons.net.ftp.FTP
import org.apache.commons.net.ftp.FTPClient
import org.apache.commons.net.ftp.FTPFile
import java.io.ByteArrayInputStream
import java.io.File

abstract class FtpInstance(private val ftpProp: FtpProp): Ftp {

    companion object {
        private const val BUFFER_SIZE = 1024 * 1024

        private const val ERROR_ACCESS_FTP = "Ошибка соединения с сервером :( "
    }

    private val ftpClient = FTPClient()

    protected abstract val imeiFilePath: String

    override fun readTextFile(remoteFullPath: String): String {
        connectTry()

        val text = readFtpTextFile(remoteFullPath)

        disconnect()

        return text
    }

    private fun readFtpTextFile(remoteFullPath: String): String = try {
        ftpClient.retrieveFileStream(remoteFullPath).bufferedReader().use { it.readText() }
    } catch (e: Exception) {
        "$ERROR_ACCESS_FTP ${e.message}"
    }

    override fun saveImei(imei: String) {
        connectTry()

        val imeiFile = "$imei.txt"

//        val existFile = ftpClient.listFiles("$imeiFilePath/")?.firstOrNull {
//            it.name == imeiFile
//        }
//
//        existFile?.let { readSaveCount(imeiFile) } ?: saveImeiFile(imeiFile, 1)

        saveImeiFile(imeiFile, 1)

        disconnect()
    }

    private fun saveImeiFile(imeiFile: String, count: Int) {

        val inputStream = ByteArrayInputStream(count.toString().toByteArray())

        val remotePathFile = "$imeiFilePath/$imeiFile"

        ftpClient.storeFile(remotePathFile, inputStream)
        inputStream.close()
    }

    private fun readSaveCount(imeiFile: String) {

        val remotePathFile = "$imeiFilePath/$imeiFile"

        val nextCount = try {
            readFtpTextFile(remotePathFile).toInt() + 1
        } catch (e: Exception) {
            1
        }

        saveImeiFile(imeiFile, nextCount)
    }

    override fun downloadFile(remoteFullPath: String, localFile: File) {

        connectTry()

        val isSuccess = localFile.outputStream().use { outStrim ->
            ftpClient.retrieveFile(remoteFullPath, outStrim)
        }

        disconnect()

        if(!isSuccess) {
            throw Exception("File $remoteFullPath not loaded by FTP")
        }
    }

    override fun listFiles(ftpPath: String): Array<FTPFile>? {

        connectTry()

        val listFiles = ftpClient.listFiles(ftpPath)

        disconnect()

        return listFiles
    }

    private fun disconnect() {
        if (!ftpClient.isConnected) return

        logOutTry()

        disconnectTry()
    }

    private fun logOutTry(): Boolean {
        try {
            ftpClient.logout()
        } catch (e: Exception) {}

        return !ftpClient.isConnected
    }

    private fun disconnectTry() {
        try {
            ftpClient.disconnect()
        } catch (e: Exception) {}
    }

    private fun connectTry() {
        //try {
            connect()
       /* } catch (e: Exception) {

            ftpClient.disconnect()

            throw Exception(e.message)
        }*/
    }

    private fun connect() {
        ftpClient.connect(ftpProp.host, ftpProp.port)

        ftpClient.login(ftpProp.user, ftpProp.password)

        if(ftpProp.isPassiveMode) {
            ftpClient.enterLocalPassiveMode()
        }

        ftpClient.setFileType(FTP.BINARY_FILE_TYPE, FTP.BINARY_FILE_TYPE)

        // ftpClient.setFileTransferMode(FTP.BINARY_FILE_TYPE)

        ftpClient.bufferSize = BUFFER_SIZE

        ftpClient.autodetectUTF8 = true

        Log.d("connect", "connect= ${ftpClient.isConnected}")
    }
}