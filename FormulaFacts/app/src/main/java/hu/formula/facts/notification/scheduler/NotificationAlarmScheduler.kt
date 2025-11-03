package hu.formula.facts.notification.scheduler

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.os.Build
import hu.formula.facts.R
import hu.formula.facts.domain.model.GrandPrix
import hu.formula.facts.notification.NotificationReceiver
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration.Companion.minutes

class NotificationAlarmScheduler(
    private val context: Context
): AlarmScheduler {
    private val alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager

    companion object {
        private val TIME_BEFORE_SESSION = 10.minutes
    }

    private fun Instant.laterThanNow(): Boolean {
        return this > Clock.System.now()
    }

    private fun setAlarm(startTime: Instant?, message: String = "", title: String? = null) {
        if (startTime == null || startTime.laterThanNow().not()) return
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("message", message)
            putExtra("title", title ?: context.getString(R.string.app_name))
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            startTime.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.S) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                startTime.minus(TIME_BEFORE_SESSION).toEpochMilliseconds(),
                pendingIntent
            )
        } else {
            if (alarmManager.canScheduleExactAlarms()) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    startTime.minus(TIME_BEFORE_SESSION).toEpochMilliseconds(),
                    pendingIntent
                )
            }
        }
    }

    private fun cancelAlarm(startTime: Instant?) {
        if (startTime == null) return
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            startTime.hashCode(),
            Intent(context, NotificationReceiver::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }

    override fun setRaceAlarm(gp: GrandPrix) {
        setAlarm(
            gp.startTime,
            message = context.getString(R.string.notification_message, context.getString(R.string.race)),
            title = gp.name
        )
        setAlarm(
            gp.sprintTime,
            message = context.getString(R.string.notification_message, context.getString(R.string.sprint_race)),
            title = gp.name
        )
    }

    override fun setPracticeAlarm(gp: GrandPrix) {
        setAlarm(
            gp.firstPracticeTime,
            message = context.getString(R.string.notification_message, context.getString(R.string.fp1)),
            title = gp.name
        )
        setAlarm(
            gp.secondPracticeTime,
            message = context.getString(R.string.notification_message, context.getString(R.string.fp2)),
            title = gp.name
        )
        setAlarm(
            gp.thirdPracticeTime,
            message = context.getString(R.string.notification_message, context.getString(R.string.fp3)),
            title = gp.name
        )
    }

    override fun setQualifyingAlarm(gp: GrandPrix) {
        setAlarm(
            gp.qualifyingTime,
            message = context.getString(R.string.notification_message, context.getString(R.string.qualifying)),
            title = gp.name
        )
        setAlarm(
            gp.sprintQualifyingTime,
            message = context.getString(R.string.notification_message, context.getString(R.string.sprint_qualifying)),
            title = gp.name
        )
    }

    override fun cancelRaceAlarm(gp: GrandPrix) {
        cancelAlarm(gp.startTime)
        cancelAlarm(gp.sprintTime)
    }

    override fun cancelPracticeAlarm(gp: GrandPrix) {
        cancelAlarm(gp.firstPracticeTime)
        cancelAlarm(gp.secondPracticeTime)
        cancelAlarm(gp.thirdPracticeTime)
    }

    override fun cancelQualifyingAlarm(gp: GrandPrix) {
        cancelAlarm(gp.qualifyingTime)
        cancelAlarm(gp.sprintQualifyingTime)
    }

}