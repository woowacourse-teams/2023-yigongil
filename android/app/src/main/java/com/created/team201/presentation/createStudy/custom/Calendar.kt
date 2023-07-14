package com.created.team201.presentation.createStudy.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.created.team201.R
import com.created.team201.databinding.CalendarBinding
import com.created.team201.presentation.createStudy.custom.decorator.SaturdayDecorator
import com.created.team201.presentation.createStudy.custom.decorator.SundayDecorator
import com.created.team201.presentation.createStudy.custom.decorator.TodayDecorator
import com.prolificinteractive.materialcalendarview.CalendarDay
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

class Calendar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleArr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleArr) {
    private val binding: CalendarBinding by lazy {
        CalendarBinding.inflate(LayoutInflater.from(context), this, true)
    }

    private var maximumMonthRange: Long = 1
    private val maximumDate: LocalDate by lazy {
        LocalDate.now().plusMonths(maximumMonthRange)
    }

    private val dateFormat: String by lazy {
        context.getString(R.string.calendar_formatter_information_date)
    }

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.Calendar, 0, 0).apply {
            runCatching {
                maximumMonthRange = getInteger(R.styleable.Calendar_maximumMonthRange, 1).toLong()
            }.also {
                recycle()
            }
        }
        initStartDate()
        setDecorators()
    }

    private fun initStartDate() {
        binding.calendar.state().edit()
            .setMaximumDate(CalendarDay.from(maximumDate))
            .setMinimumDate(CalendarDay.from(LocalDate.now()))
            .commit()
    }

    private fun setDecorators() {
        binding.calendar.addDecorators(
            SaturdayDecorator(maximumDate),
            SundayDecorator(maximumDate),
            TodayDecorator(context.getColor(R.color.green05_3AD353)),
        )
    }

    fun setOnDateChangedListener(listener: CalendarChangeListener) {
        binding.calendar.setOnDateChangedListener { _, date, _ ->
            listener.onChange(date.date.format(DateTimeFormatter.ofPattern(dateFormat)))
        }
    }
}
