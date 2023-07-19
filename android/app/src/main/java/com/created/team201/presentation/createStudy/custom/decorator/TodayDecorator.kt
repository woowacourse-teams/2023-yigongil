package com.created.team201.presentation.createStudy.custom.decorator

import android.text.style.ForegroundColorSpan
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import org.threeten.bp.LocalDate

class TodayDecorator(private val color: Int) : DayViewDecorator {
    override fun shouldDecorate(day: CalendarDay): Boolean {
        return day.date == LocalDate.now()
    }

    override fun decorate(view: DayViewFacade) {
        return view.addSpan(ForegroundColorSpan(color))
    }
}
