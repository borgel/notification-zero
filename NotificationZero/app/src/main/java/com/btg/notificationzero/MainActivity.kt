package com.btg.notificationzero

import android.content.ComponentName
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.service.notification.StatusBarNotification
import android.support.v4.app.FragmentActivity
import com.btg.notificationzero.MyNotificationService
import java.util.concurrent.Semaphore
import android.provider.Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS
import android.content.Intent
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "NotificationZero"
        private const val ACTION_NOTIFICATION_LISTENER_SETTINGS = "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //TODO disable action bar

        println("notificationzero started")
        Log.i("NZ", "started")

        //FIXME mv?
        button.setOnClickListener {
            showNotifications()
        }
    }

    override fun onStart() {
        //TODO move to onclick?
        showNotifications()

        super.onStart()
    }

    public override fun onResume() {
        showNotifications()
        super.onResume()
    }

    private fun doclick() {
        Log.i(TAG, "clicked")
    }

    private fun showNotifications() {
        if (isNotificationServiceEnabled()) {
            Log.i(TAG, "Notification enabled -- trying to fetch it")
            getNotifications()
        } else {
            Log.i(TAG, "Notification disabled -- Opening settings")
            startActivity(Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS))
        }
    }

    private fun getNotifications() {
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
                if (packageName.equals(ComponentName.unflattenFromString(n).packageName)) {
                    println("Package matched, our notification service is running")
                    return true
                }
            }
        }
        return false
    }
}
