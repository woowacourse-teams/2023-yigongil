package com.created.team201.presentation.common.customview.dayofselector

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.children
import androidx.databinding.BindingAdapter
import com.created.team201.R
import com.created.team201.databinding.ViewDayOfWeekSelectorBinding

class DayOfWeekSelector @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : ConstraintLayout(context, attrs) {

    private val binding: ViewDayOfWeekSelectorBinding by lazy {
        ViewDayOfWeekSelectorBinding.inflate(LayoutInflater.from(context), this, true)
    }

    private val dayTextViews: Map<DayOfWeek, TextView> by lazy { initDayTextViews() }

    private val textViewDays: Map<TextView, DayOfWeek> by lazy { initTextViewDays() }

    private var canMultiSelect: Boolean = false

    private var dayOnClick: DayOnClickListener? = null

    fun interface DayOnClickListener {
        fun onClick(day: DayOfWeek)
    }

    init {
        attrs?.let { setTypedArray(getAttrs(it)) }
        setupDayTexts()
        setupDayClickListeners()
    }

    private fun initDayTextViews(): Map<DayOfWeek, TextView> =
        DayOfWeek.getValuesWithStartDay()
            .zip(binding.clDayOfWeekBackground.children.filterIsInstance<TextView>().toList())
            .toMap()

    private fun initTextViewDays(): Map<TextView, DayOfWeek> =
        dayTextViews.entries.associateBy({ it.value }, { it.key })

    private fun getAttrs(attrs: AttributeSet): TypedArray {
        return context.obtainStyledAttributes(attrs, R.styleable.DayOfWeekSelector)
    }

    private fun setTypedArray(typedArray: TypedArray) {
        canMultiSelect =
            typedArray.getBoolean(R.styleable.DayOfWeekSelector_canMultipleSelect, false)

        if (canMultiSelect) {
            dayTextViews.values.forEach {
                it.background = ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.bg_day_of_week_selector_multi_select,
                    null,
                )
            }
        } else {
            dayTextViews.values.forEach {
                it.background = ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.bg_day_of_week_selector_single_select,
                    null,
                )
            }
        }

        typedArray.recycle()
    }

    private fun setupDayClickListeners() {
        dayTextViews.values.forEach { textView ->
            textView.setOnClickListener { it ->
                val clickedDay: DayOfWeek = textViewDays[it] ?: throw NullPointerException()
                if (canMultiSelect) {
                    selectDayTextView(textView)
                    dayOnClick?.onClick(clickedDay)
                    return@setOnClickListener
                }

                if (it.isSelected) return@setOnClickListener

                selectDayTextView(textView)
                dayTextViews.values
                    .filterNot { it == textView }
                    .forEach { it.isSelected = false }
                dayOnClick?.onClick(clickedDay)
            }
        }
    }

    private fun setupDayTexts() {
        val days: List<DayOfWeek> = DayOfWeek.getValuesWithStartDay()
        dayTextViews.values.forEachIndexed { index, textView ->
            textView.text = resources.getString(days[index].stringRes)
        }
    }

    fun setSelectableDays(selectableDays: List<DayOfWeek>) {
        dayTextViews.keys.forEach {
            if (selectableDays.contains(it)) {
                dayTextViews[it]?.isEnabled = true
                return@forEach
            }
            dayTextViews[it]?.isEnabled = false
        }
    }

    fun selectDay(day: DayOfWeek) {
        dayTextViews[day]?.isSelected = true
    }

    fun selectDays(days: List<DayOfWeek>) {
        days.forEach { day ->
            val textView: TextView = dayTextViews[day]
                ?: throw NullPointerException("$day 에 해당하는 TextView가 존재하지 않습니다.")

            textView.isSelected = !(textView.isSelected)
        }
    }

    private fun selectDayTextView(day: TextView) {
        day.isSelected = !day.isSelected
    }

    fun getSelectedDays(): List<DayOfWeek> {
        val selectedDayTextViews: List<TextView> = dayTextViews.values.filter { it.isSelected }
        return selectedDayTextViews.map { textViewDays[it] ?: throw NullPointerException() }
    }

    fun setDayOnClickListener(dayOnClickListener: DayOnClickListener) {
        dayOnClick = dayOnClickListener
    }

    fun getSelectedDaysSize(): Int {
        return dayTextViews.values.filter { it.isSelected }.size
    }

    companion object {

        @JvmStatic
        @BindingAdapter("selectableDays")
        fun setSelectableDays(
            dayOfWeekSelector: DayOfWeekSelector,
            selectableDays: List<DayOfWeek>,
        ) {
            dayOfWeekSelector.setSelectableDays(selectableDays)
        }

        @JvmStatic
        @BindingAdapter("dayOnClick")
        fun setDayOnClickListener(
            dayOfWeekSelector: DayOfWeekSelector,
            dayOnClickListener: DayOnClickListener,
        ) {
            dayOfWeekSelector.setDayOnClickListener(dayOnClickListener)
        }
    }
}
