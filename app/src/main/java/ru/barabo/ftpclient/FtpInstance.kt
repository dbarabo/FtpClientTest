package ru.barabo.ftpclient

import android.util.Log
import org.apache.commons.net.ftp.FTP
import org.apache.commons.net.ftp.FTPClient
import org.apache.commons.net.ftp.FTPFile
import java.io.File

open class FtpInstance(private val ftpProp: FtpProp): Ftp {

    companion object {
        private const val BUFFER_SIZE = 1024 * 1024
    }

    private val ftpClient = FTPClient()

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