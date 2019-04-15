package com.btg.notificationzero

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import java.util.concurrent.Semaphore

class MyNotificationService : NotificationListenerService() {
    companion object {
        private const val TAG = "MyNotificationService"
        internal var notificationServiceInstance: MyNotificationService? = null
        internal var sem = Semaphore(0)

        fun get(): MyNotificationService? {
            return notificationServiceInstance
        }
    }

    // not safe to do work until here
    override fun onListenerConnected() {
        Log.i(TAG, "Connected")
        notificationServiceInstance = this
    }

    override fun onListenerDisconnected() {
        Log.i(TAG, "Disconnected")
        notificationServiceInstance = null
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {

    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        super.onNotificationRemoved(sbn)
    }


    // couple the notification list to the listview
    class NotificationListAdapter() : RecyclerView.Adapter<NotificationListAdapter.ViewHolder>() {
        companion object {
            private const val TAG = "NotificationListAdapter"
            private var notificationService : MyNotificationService? = null
            //FIXME replace
            //private val dataset = Array(50, {i -> "This is element # $i"})
        }

        fun setNotificationService(t: MyNotificationService?) {
            if(t != null) {
                notificationService = t
            }
        }

        /**
         * Provide a reference to the type of views that you are using (custom ViewHolder)
         */
        class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
            val textView: TextView

            init {
                // Define click listener for the ViewHolder's View.
                v.setOnClickListener { Log.d(TAG, "Element $adapterPosition clicked.") }
                textView = v.findViewById(R.id.textView)
            }
        }

        // Create new views (invoked by the layout manager)
        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
            // Create a new view.
            val v = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.text_row_item, viewGroup, false)

            return ViewHolder(v)
        }

        // Replace the contents of a view (invoked by the layout manager)
        override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
            Log.d(TAG, "Element $position set.")

            //TODO

            // Get element from your dataset at this position and replace the contents of the view
            // with that element
            //viewHolder.textView.text = dataset[position]
            viewHolder.textView.text = notificationService!!.getActiveNotifications()[position]!!.packageName
        }

        // Return the size of your dataset (invoked by the layout manager)
        override fun getItemCount() : Int {
            //dataset.size
            if(notificationService == null) {
                Log.d(TAG, "Got item count but was 0")
                return 0
            }
            return notificationService!!.getActiveNotifications()!!.size
        }
    }
}