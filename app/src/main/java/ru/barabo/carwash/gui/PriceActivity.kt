package ru.barabo.carwash.gui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import kotlinx.android.synthetic.main.activity_price.*
import ru.barabo.ftpclient.FtpObject
import ru.barabo.carwash.R

class PriceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE)

        setContentView(R.layout.activity_price)

        window.setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.price_title)

        setPriceText()
    }

    private fun setPriceText() {

        allPriceText.text = FtpObject.priceServer
    }
}
