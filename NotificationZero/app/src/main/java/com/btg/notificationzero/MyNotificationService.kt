package com.btg.notificationzero

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log

import java.util.concurrent.Semaphore

class MyNotificationService : NotificationListenerService() {
    companion object {
        private const val TAG = "MyNotificationService"
        internal var _this: MyNotificationService? = null
        internal var sem = Semaphore(0)

        fun get(): MyNotificationService? {
            sem.acquireUninterruptibly()
            val ret = _this
            sem.release()
            return ret
        }
    }

    // not safe to do work until here
    override fun onListenerConnected() {
        Log.i(TAG, "Connected")
        _this = this
        sem.release()
    }

    override fun onListenerDisconnected() {
        Log.i(TAG, "Disconnected")
        sem.acquireUninterruptibly()
        _this = null
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {

    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        super.onNotificationRemoved(sbn)
    }
}