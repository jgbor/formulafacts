package hu.formula.facts.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Bundle
import androidx.core.app.NotificationCompat
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import hu.formula.facts.MainActivity
import hu.formula.facts.R

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val msg = intent?.getStringExtra("message") ?: return
        val title = intent.getStringExtra("title")
        context?.let {
            triggerImmediateNotification(it, title, msg)
        }
    }

    private fun triggerImmediateNotification(context: Context, title: String?, msg: String) {
        val notificationManager =
            context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val channelId = "formula_facts.notification"
        val channelName = context.getString(R.string.notification_channel_name)
        notificationManager.createNotificationChannel(
            NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )
        )

        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            1,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(msg)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val notificationId = 1
        notificationManager.notify(notificationId, notificationBuilder.build())

        Firebase.analytics.logEvent(
            "notification_triggered",
            Bundle().apply {
                putString("title", title)
                putString("message", msg)
            }
        )
    }
}
