package hu.formula.facts.notification.scheduler

import hu.formula.facts.domain.model.GrandPrix

interface AlarmScheduler {
    fun setRaceAlarm(gp: GrandPrix)
    fun setPracticeAlarm(gp: GrandPrix)
    fun setQualifyingAlarm(gp: GrandPrix)
    fun cancelRaceAlarm(gp: GrandPrix)
    fun cancelPracticeAlarm(gp: GrandPrix)
    fun cancelQualifyingAlarm(gp: GrandPrix)
}