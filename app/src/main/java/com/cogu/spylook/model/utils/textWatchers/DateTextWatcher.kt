package com.cogu.spylook.model.utils.textWatchers

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.time.LocalDate
import java.util.Calendar
import java.util.Locale
import kotlin.math.max
import kotlin.math.min

class DateTextWatcher(private val editText: EditText) : TextWatcher {
    private var current = ""
    private val ddmmyyyy = "DDMMYYYY"
    private val cal: Calendar = Calendar.getInstance()

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        if (s.toString() != current) {
            var clean = s.toString().replace("[^\\d]".toRegex(), "")
            val cleanC = current.replace("[^\\d]".toRegex(), "")

            val cl = clean.length
            var sel = cl
            var i = 2
            while (i <= cl && i < 6) {
                sel++
                i += 2
            }
            if (clean == cleanC) sel--

            if (clean.length < 8) {
                clean = clean + ddmmyyyy.substring(clean.length)
            } else {
                var day = clean.substring(0, 2).toInt()
                var mon = clean.substring(2, 4).toInt()
                var year = clean.substring(4, 8).toInt()

                year = max(1900.0, min(year.toDouble(), LocalDate.now().year.toDouble())).toInt()
                if (year == LocalDate.now().year) {
                    mon =
                        max(1.0, min(mon.toDouble(), LocalDate.now().monthValue.toDouble())).toInt()
                } else {
                    mon = max(1.0, min(mon.toDouble(), 12.0)).toInt()
                }
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, mon - 1)
                if (mon == LocalDate.now().monthValue && year == LocalDate.now().year) {
                    day = max(
                        1.0,
                        min(day.toDouble(), LocalDate.now().dayOfMonth.toDouble())
                    ).toInt()
                }else{
                    day = max(
                        1.0,
                        min(day.toDouble(), cal.getActualMaximum(Calendar.DAY_OF_MONTH).toDouble())
                    ).toInt()
                }
                clean = String.format(Locale.getDefault(), "%02d%02d%04d", day, mon, year)
            }

            clean = String.format(
                "%s/%s/%s", clean.substring(0, 2),
                clean.substring(2, 4),
                clean.substring(4, 8)
            )

            current = clean
            editText.setText(current)
            editText.setSelection(min(sel.toDouble(), current.length.toDouble()).toInt())
        }
    }

    override fun afterTextChanged(s: Editable?) {}
}
