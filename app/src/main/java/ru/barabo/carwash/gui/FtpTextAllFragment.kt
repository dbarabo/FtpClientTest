package ru.barabo.carwash.gui

import android.content.Intent
import android.os.AsyncTask
import android.support.v4.app.Fragment
import android.os.Bundle
import android.provider.Settings
import android.text.Html
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import ru.barabo.ftpclient.FtpObject
import ru.barabo.carwash.R
import ru.barabo.carwash.main.CarWashApp
import java.util.*
import kotlin.concurrent.thread
import kotlin.concurrent.timer

class FtpTextAllFragment : Fragment() {

    companion object {
        private const val MIN_MILLI_SEC_PERIOD: Long = 1_000

        private const val WAIT_SEC = 30

        private const val WAIT_MILLI_SEC = WAIT_SEC * MIN_MILLI_SEC_PERIOD

        private var timerFtp: Timer? = null
    }

    private var messageTextServer: TextView? = null

    private var infoTextServer: TextView? = null

    private var phoneTextServer: TextView? = null

    private var htmlPhone: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val viewFragment = inflater.inflate(R.layout.ftp_text_fragment, container, false)!!

        initTextElems(viewFragment)

        startConfig()

        return viewFragment
    }

    private fun initTextElems(viewFragment: View) {
        messageTextServer = viewFragment.findViewById(R.id.messageTextServer)

        messageTextServer?.text = "Wait repeat info from server..."

        infoTextServer = viewFragment.findViewById(R.id.infoTextServer)

        phoneTextServer = viewFragment.findViewById(R.id.phoneTextServer)

        val priceButton = viewFragment.findViewById<Button>(R.id.priceButton)

        priceButton?.setOnClickListener {
            showViewPrice()
        }
    }

    private fun showViewPrice() {

        val intent = Intent(CarWashApp.instance, PriceActivity::class.java)
        startActivity(intent)
    }

    override fun onDestroyView() {

        stoping()

        super.onDestroyView()
    }

    private fun stoping() {

        timerFtp?.cancel()
        timerFtp?.purge()

        timerFtp = null
    }

    private fun startConfig() {

        timerFtp = timer(name = this.javaClass.simpleName, initialDelay = 0, daemon = false, period = WAIT_MILLI_SEC) { timerRun() }
    }

    private fun timerRun() {
        AsyncFtp().execute()
    }

    private inner class AsyncFtp : AsyncTask<Unit, Unit, Unit>() {

        override fun doInBackground(vararg params: Unit?) {

            FtpObject.initReadServerText()
        }

        override fun onPostExecute(result: Unit?) {

            setPhoneServer()

            setTextServer()

            val infoView = infoTextServer ?: return
            synchronized(infoView.text) {
                infoView.text = FtpObject.infoServer
            }

            saveImei()
        }


        private fun saveImei() {
            if(CarWashApp.isSaved) return

            CarWashApp.isSaved = true

            saveImeiFtp()
        }

        private fun saveImeiFtp() {
            val imei = getImei()

            Log.d("CarWashApp", "imei=$imei")

            thread { FtpObject.saveImei(imei) }
        }

        private fun getImei(): String {
            val id = Settings.Secure.getString(CarWashApp.instance.contentResolver, Settings.Secure.ANDROID_ID)

            return id
        }

        private fun setTextServer() {
            val textView = messageTextServer ?: return

            synchronized(textView.text) {
                textView.text = concatServerTextPhone(FtpObject.textServer, htmlPhone)
            }
        }

        private fun concatServerTextPhone(textServer: String?, htmlPhone: String?): CharSequence? {
            return textServer

            if(textServer == null || htmlPhone == null) return null

            val allText = textServer + htmlPhone

            return Html.fromHtml(allText)
        }


        private fun setPhoneServer() {
            val phoneView = phoneTextServer ?: return

            val phoneNumber = FtpObject.phoneServer ?: return

            htmlPhone = "<a href=\"tel:+$phoneNumber\">+$phoneNumber</a>"

            synchronized(phoneView.text) {
                phoneView.text = Html.fromHtml(htmlPhone)
                phoneView.movementMethod = LinkMovementMethod.getInstance()
                phoneView.autoLinkMask = Linkify.PHONE_NUMBERS;

            }
        }
    }
}