package io.github.panekd.stroopswap.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import io.github.panekd.stroopswap.data.SettingsManager
import kotlinx.coroutines.flow.first
import java.util.Calendar

class DailyAlarmManager(private val context: Context) {
    private val settingsManager = SettingsManager(context)

    private val alarmManager =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    private val alarmId = 0 // single-alarm design

    private fun pendingIntent(): PendingIntent {
        val intent = Intent(context, AlarmReceiver::class.java)

        return PendingIntent.getBroadcast(
            context,
            alarmId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    suspend fun set() {

        val settings = settingsManager.settingsFlow.first()

        val hour = settings.remindersHour
        val minute = settings.remindersMinute

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)

            if (before(Calendar.getInstance())) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }

        try {
            alarmManager.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent()
            )
        } catch (_: SecurityException) {
            cancel()
        }
    }

    fun cancel() {
        alarmManager.cancel(pendingIntent())
        pendingIntent().cancel()
    }
}