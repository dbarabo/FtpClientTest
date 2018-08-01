package ru.barabo.carwash.gui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.Window
import ru.barabo.carwash.R
import ru.barabo.gui.FragmentCreator

class MainActivity : AppCompatActivity(), FragmentCreator {

    override fun createFragment(): Fragment = FtpTextAllFragment()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE)

        setContentView(R.layout.activity_main)

        addFragment(R.id.fragmentContainer)

        window.setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.main_title)
    }
}
