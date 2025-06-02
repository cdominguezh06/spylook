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
    private val calendario: Calendar = Calendar.getInstance()
    private var lastNumberIndex = 0

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        if (s.toString() != current) {
            var clean = s.toString().replace("\\D".toRegex(), "")
            val cleanCurrent = current.replace("\\D".toRegex(), "")
            val cleanLength = clean.length
            var selection = cleanLength
            var i = 2

            while (i <= cleanLength && i < 6) {
                selection++
                i += 2
            }

            if (clean == cleanCurrent) selection--

            if (clean.length < 8) {
                clean = clean + ddmmyyyy.substring(clean.length)
            } else {
                var dia = clean.substring(0, 2).toInt()
                var mes = clean.substring(2, 4).toInt()
                var anno = clean.substring(4, 8).toInt()

                anno = max(1970.0, min(anno.toDouble(), LocalDate.now().year.toDouble())).toInt()
                if (anno == LocalDate.now().year) {
                    mes =
                        max(1.0, min(mes.toDouble(), LocalDate.now().monthValue.toDouble())).toInt()
                } else {
                    mes = max(1.0, min(mes.toDouble(), 12.0)).toInt()
                }

                calendario.set(Calendar.YEAR, anno)
                calendario.set(Calendar.MONTH, mes - 1)

                if (mes == LocalDate.now().monthValue && anno == LocalDate.now().year) {
                    dia =
                        max(1.0, min(dia.toDouble(), LocalDate.now().dayOfMonth.toDouble())).toInt()
                } else {
                    dia = max(
                        1.0,
                        min(
                            dia.toDouble(),
                            calendario.getActualMaximum(Calendar.DAY_OF_MONTH).toDouble()
                        )
                    ).toInt()
                }

                clean = String.format(Locale.getDefault(), "%02d%02d%04d", dia, mes, anno)
            }

            clean = String.format(
                "%s/%s/%s", clean.substring(0, 2),
                clean.substring(2, 4),
                clean.substring(4, 8)
            )

            current = clean
            editText.setText(current)

            if (count > 0 && before == 0) {
                if(start > lastNumberIndex){
                    editText.setSelection(min(selection.toDouble(), current.length.toDouble()).toInt())
                }else
                    editText.setSelection(min((start+count).toDouble(), current.length.toDouble()).toInt())
            }else{
                editText.setSelection(start)
            }

            lastNumberIndex = s.toString().lastIndexOf(
                current.replace("\\D".toRegex(), "")[current.replace("\\D".toRegex(), "").length - 1])

        }
    }

    override fun afterTextChanged(s: Editable?) {}
}
