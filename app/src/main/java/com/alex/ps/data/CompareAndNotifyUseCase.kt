package com.alex.ps.data

import com.alex.ps.infrastructure.Notifier

class CompareAndNotifyUseCase(
    val old: Shortages,
    val new: Shortages,
    val notifier: Notifier
) {
    operator fun invoke() {
        notifyIfExtraChanged()
        notifyIfTodayScheduleChanged()
    }

    private fun notifyIfExtraChanged() {
        if (old.extra.toSet() != new.extra.toSet()) {
            notifier.notifyExtraChanged()
        }
    }

    private fun notifyIfTodayScheduleChanged() {
        if (old.schedules.today() != new.schedules.today()) {
            notifier.notifyTodayScheduleChanged()
        }
    }
}