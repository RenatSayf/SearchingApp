package com.fl.searchingapp

import android.content.ComponentName
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity()
{

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val manufacturer = "xiaomi"
//        if (manufacturer.equals(Build.MANUFACTURER, ignoreCase = true))
//        {
//            //this will open auto start screen where user can enable permission for your app
//            val intent = Intent()
//            intent.component = ComponentName(
//                "com.miui.securitycenter",
//                "com.miui.permcenter.autostart.AutoStartManagementActivity"
//            )
//            startActivity(intent)
//        }
    }
}
