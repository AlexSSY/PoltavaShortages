package com.alex.ps.domain

import com.alex.ps.data.settings.Settings
import com.alex.ps.infrastructure.Notifier

class CompareAndNotifyUseCase(
    val notifier: Notifier,
) {
    operator fun invoke(
        old: Shortages,
        new: Shortages,
        settings: Settings
    ) {
        notifyIfGavChanged(old, new)
        notifyIfSpecGavChanged(old, new)

        val major = settings.selectedQueue.major
        val minor = settings.selectedQueue.minor

        val oldQueue = old.queues.getOrNull(major, minor)
        val newQueue = new.queues.getOrNull(major, minor)

        val oldSchedules = oldQueue?.schedules ?: emptyList()
        val newSchedules = newQueue?.schedules ?: emptyList()

        notifyIfTodayScheduleChanged(oldSchedules, newSchedules)
        notifyIfTomorrowScheduleChanged(oldSchedules, newSchedules)
    }

    private fun notifyIfGavChanged(old: Shortages, new: Shortages) {
        if (old.isGav != new.isGav) {
            notifier.notifyGav(new.isGav)
        }
    }

    private fun notifyIfSpecGavChanged(old: Shortages, new: Shortages) {
        if (old.isSpecGav != new.isSpecGav) {
            notifier.notifySpecGav(new.isSpecGav)
        }
    }

    private fun notifyIfTodayScheduleChanged(
        oldSchedules: List<Schedule>,
        newSchedules: List<Schedule>
    ) {
        if (oldSchedules.today() != newSchedules.today()) {
            if (tomorrowScheduleNotBecomesTodaySchedule(oldSchedules, newSchedules)) {
                notifier.notifyTodayScheduleChanged()
            }
        }
    }

    private fun notifyIfTomorrowScheduleChanged(
        oldSchedules: List<Schedule>,
        newSchedules: List<Schedule>
    ) {
        if (oldSchedules.tomorrow() != newSchedules.tomorrow()) {
            if (oldSchedules.tomorrow() == null) {
                notifier.notifyTomorrowScheduleAvailable()
            }
            else if (tomorrowScheduleNotBecomesTodaySchedule(oldSchedules, newSchedules)) {
                notifier.notifyTomorrowScheduleChanged()
            }
        }
    }

    private fun tomorrowScheduleNotBecomesTodaySchedule(
        oldSchedules: List<Schedule>,
        newSchedules: List<Schedule>
    ): Boolean {
        val oldTomorrow = oldSchedules.tomorrow()
        val newToday = newSchedules.today()

        return oldTomorrow != newToday
    }
}