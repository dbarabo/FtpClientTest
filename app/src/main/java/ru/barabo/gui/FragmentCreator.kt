package ru.barabo.gui

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.FrameLayout

interface FragmentCreator {
    fun createFragment(): Fragment

    fun addFragment(resourceLayout: Int) {

       if(this is FragmentActivity) {
            val layout = supportFragmentManager.findFragmentById(resourceLayout)

           Log.d("addFragment", " layout=$layout")

           if(layout == null) {
               supportFragmentManager.beginTransaction()
                       .add(resourceLayout, createFragment() )
                       .commit()
           }
        }
    }
}
