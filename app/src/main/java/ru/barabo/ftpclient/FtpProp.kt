package ru.barabo.ftpclient

data class FtpProp(val host: String,
                   val user: String,
                   val password: String,
                   val port: Int = 21,
                   val isPassiveMode: Boolean = false)