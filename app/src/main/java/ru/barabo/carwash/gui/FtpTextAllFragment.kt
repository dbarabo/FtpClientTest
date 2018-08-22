package ru.barabo.carwash.gui

import android.content.Intent
import android.graphics.*
import android.os.AsyncTask
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import ru.barabo.carwash.R
import ru.barabo.carwash.main.CarWashApp
import ru.barabo.ftpclient.FtpObject
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val viewFragment = inflater.inflate(R.layout.ftp_text_fragment, container, false)!!

        initTextElems(viewFragment)

        startConfig()

        return viewFragment
    }

    private fun initTextElems(viewFragment: View) {
        messageTextServer = viewFragment.findViewById(R.id.messageTextServer)

        infoTextServer = viewFragment.findViewById(R.id.infoTextServer)

        val priceButton = viewFragment.findViewById<Button>(R.id.priceButton)

        priceButton?.setOnClickListener {
            showViewPrice()
        }

        roundImageView(viewFragment)
    }

    private fun roundImageView(viewFragment: View) {
        val imageView = viewFragment.findViewById<ImageView>(R.id.fotoWash)


        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.fotowash)
        val imageRounded = Bitmap.createBitmap(bitmap.width, bitmap.height, bitmap.config)
        val canvas = Canvas(imageRounded)
        val paint = Paint()
        paint.isAntiAlias = true
        paint.shader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        canvas.drawRoundRect(RectF(0f, 0f, bitmap.width.toFloat(), bitmap.height.toFloat()), 100F, 100F, paint)// Round Image Corner 100 100 100 100
        imageView.setImageBitmap(imageRounded)


//        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.fotowash)
//        val roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(resources, bitmap)
//        roundedBitmapDrawable.isCircular = true
//        imageView.setImageDrawable(roundedBitmapDrawable)
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

            setTextServer()

            val infoView = infoTextServer ?: return
            synchronized(infoView.text) {
                infoView.text = "${FtpObject.infoServer}\n+${FtpObject.phoneServer}"
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

        private fun getImei(): String =
                Settings.Secure.getString(CarWashApp.instance.contentResolver, Settings.Secure.ANDROID_ID)

        private fun setTextServer() {
            val textView = messageTextServer ?: return

            synchronized(textView.text) {
                textView.text = FtpObject.textServer
            }
        }
    }
}