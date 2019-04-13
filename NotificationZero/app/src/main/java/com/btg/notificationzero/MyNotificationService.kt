package testapp.android.gradle.inutilfutil.com.myapplication

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log

import java.util.concurrent.Semaphore

class MyNotificationService : NotificationListenerService() {

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

    companion object {
        private val TAG = "MyNotificationService"
        internal var _this: MyNotificationService? = null
        internal var sem = Semaphore(0)

        fun get(): MyNotificationService? {
            sem.acquireUninterruptibly()
            val ret = _this
            sem.release()
            return ret
        }
    }
}