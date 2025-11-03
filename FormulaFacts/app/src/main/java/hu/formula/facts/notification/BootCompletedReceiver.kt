package hu.formula.facts.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import hu.formula.facts.data.datasource.FormulaOneRepository
import hu.formula.facts.data.settings.Settings
import hu.formula.facts.notification.scheduler.AlarmScheduler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject

@AndroidEntryPoint
class BootCompletedReceiver : BroadcastReceiver() {
    @Inject
    lateinit var settings: Settings

    @Inject
    lateinit var formulaRepository: FormulaOneRepository

    @Inject
    lateinit var notificationAlarmScheduler: AlarmScheduler

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            CoroutineScope(Dispatchers.IO).launch {
                if (settings.isNotificationEnabled.first()) {
                    setAlarms()
                }
            }
        }
    }

    private suspend fun setAlarms() {
        runCatching {
            val season = formulaRepository.getRacesOfSeason()
            settings.saveYear(
                Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).year
            )
            for (race in season) {
                if (settings.raceEnabled.first()) {
                    notificationAlarmScheduler.setRaceAlarm(race)
                }
                if (settings.qualifyingEnabled.first()) {
                    notificationAlarmScheduler.setQualifyingAlarm(race)
                }
                if (settings.practiceEnabled.first()) {
                    notificationAlarmScheduler.setPracticeAlarm(race)
                }
            }
        }
    }
}
