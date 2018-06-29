package ru.barabo.ftpclienttest.gui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import ru.barabo.ftpclienttest.R
import ru.barabo.gui.FragmentCreator

class MainActivity : AppCompatActivity(), FragmentCreator {

    override fun createFragment(): Fragment = FtpListFragment()

    override fun onCreate(savedInstanceState: Bundle?) {

        Log.d("MainActivity onCreate", " onCreate START!!!!!!!!!!!!!!!!!!!!!!")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addFragment(R.id.fragmentContainer)
    }
}
