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
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.RadioButton
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutManager: RecyclerView.LayoutManager

    companion object {
        private const val TAG = "NotificationZero"
        private const val ACTION_NOTIFICATION_LISTENER_SETTINGS = "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"
        private val notificationListAdapter = MyNotificationService.NotificationListAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.i(TAG, "Started")

        //TODO disable action bar

        //FIXME mv?
        button.setOnClickListener {
            showNotifications()
        }

        //TODO view init?
        recyclerView = main_recycler

        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        //applicationContext ?
        layoutManager = LinearLayoutManager(this)

        // Set CustomAdapter as the adapter for RecyclerView.
        recyclerView.layoutManager = layoutManager
        //recyclerView.adapter = MyNotificationService.NotificationListAdapter(dataset)
        // FIXME better null protection
        //recyclerView.adapter = MyNotificationService.get()?.getListAdapter()
        recyclerView.adapter = notificationListAdapter

        //FIXME move? trigger how?
        notificationListAdapter.setNotificationService(MyNotificationService.get())
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

    private fun showNotifications() {
        if (isNotificationServiceEnabled()) {
            Log.i(TAG, "Notification enabled -- trying to fetch it")
            getNotifications()
        } else {
            Log.i(TAG, "Notification disabled -- Opening settings")
            startActivity(Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS))
        }
    }

    //TODO rework
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
