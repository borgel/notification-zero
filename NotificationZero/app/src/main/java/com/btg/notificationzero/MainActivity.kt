package com.btg.notificationzero

import android.content.ComponentName
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.service.notification.StatusBarNotification
import android.support.v4.app.FragmentActivity
import testapp.android.gradle.inutilfutil.com.myapplication.MyNotificationService
import java.util.concurrent.Semaphore


class MainActivity : AppCompatActivity() {

    companion object {
        private val TAG = "NotificationZero"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //TODO disable action bar

        println("notificationzero started")
        Log.i("NZ", "started")

        isNotificationServiceEnabled()
    }

    override fun onStart() {
        isNotificationServiceEnabled()

        //TODO move to onclick?
        getNotifications()

        super.onStart()
    }

    override fun onStop() {
        super.onStop()
    }


    fun getNotifications() {
        Log.i(TAG, "Waiting for MyNotificationService")
        val myNotificationService = MyNotificationService.get()
        Log.i(TAG, "Active Notifications: [")
        if(myNotificationService != null) {
            for (notification in myNotificationService.getActiveNotifications()) {
                Log.i(TAG, "    " + notification.packageName + " / " + notification.tag)
            }
        }
        else {
            Log.i(TAG, "NotificationService handle was null, couldn't get notifications")
        }
        Log.i(TAG, "]")
    }

    private fun isNotificationServiceEnabled(): Boolean {
        val allNames = Settings.Secure.getString(contentResolver, "enabled_notification_listeners")
        if(allNames != null && !allNames.isEmpty()) {
            for (n in allNames.split(":")) {
                println("Service [$n]")
                if(packageName.equals(ComponentName.unflattenFromString(n).packageName)) {
                    println("Package matched, our notification service is running")
                    return true
                }
            }
        }


        return false;
    }
}
