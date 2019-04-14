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
        internal var _this: MyNotificationService? = null
        internal var sem = Semaphore(0)

        fun get(): MyNotificationService? {
            val ret = _this
            return ret
        }
    }

    // not safe to do work until here
    override fun onListenerConnected() {
        Log.i(TAG, "Connected")
        _this = this
    }

    override fun onListenerDisconnected() {
        Log.i(TAG, "Disconnected")
        _this = null
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {

    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        super.onNotificationRemoved(sbn)
    }


    // couple the notification list to the listview
    public class NotificationListAdapter(private val dataSet: Array<String>) :
        RecyclerView.Adapter<NotificationListAdapter.ViewHolder>() {
        companion object {
            private const val TAG = "NotificationListAdapter"
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
            viewHolder.textView.text = dataSet[position]
        }

        // Return the size of your dataset (invoked by the layout manager)
        override fun getItemCount() = dataSet.size
    }
}