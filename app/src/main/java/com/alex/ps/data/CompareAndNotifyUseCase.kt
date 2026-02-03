package com.alex.ps.data

import com.alex.ps.infrastructure.Notifier

class CompareAndNotifyUseCase(
    val old: Shortages,
    val new: Shortages,
    val notifier: Notifier
) {
    operator fun invoke() {
        notifyIfGavChanged()
        notifyIfSpecGavChanged()
        notifyIfTodayScheduleChanged()
        notifyIfTomorrowScheduleChanged()
    }

    private fun notifyIfGavChanged() {
        if (old.isGav != new.isGav) {
            notifier.notifyGav(new.isGav)
        }
    }

    private fun notifyIfSpecGavChanged() {
        if (old.isSpecGav != new.isSpecGav) {
            notifier.notifySpecGav(new.isSpecGav)
        }
    }

    private fun notifyIfTodayScheduleChanged() {
        if (old.schedules.today() != new.schedules.today()) {
            if (tomorrowScheduleNotBecomesTodaySchedule()) {
                notifier.notifyTodayScheduleChanged()
            }
        }
    }

    private fun notifyIfTomorrowScheduleChanged() {
        if (old.schedules.tomorrow() != new.schedules.tomorrow()) {
            if (old.schedules.tomorrow() == null) {
                notifier.notifyTomorrowScheduleAvailable()
            }
            else if (tomorrowScheduleNotBecomesTodaySchedule()) {
                notifier.notifyTomorrowScheduleChanged()
            }
        }
    }

    private fun tomorrowScheduleNotBecomesTodaySchedule(): Boolean {
        val oldTomorrow = old.schedules.tomorrow()
        val newToday = new.schedules.today()

        return oldTomorrow != newToday
    }
}