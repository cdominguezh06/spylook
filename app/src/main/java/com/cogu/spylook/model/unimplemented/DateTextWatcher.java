package com.cogu.spylook.model.unimplemented;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.Calendar;
import java.util.Locale;

public class DateTextWatcher implements TextWatcher {
    private final EditText editText;
    private String current = "";
    private final String ddmmyyyy = "DDMMYYYY";
    private final Calendar cal = Calendar.getInstance();

    public DateTextWatcher(EditText editText) {
        this.editText = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!s.toString().equals(current)) {
            String clean = s.toString().replaceAll("[^\\d]", "");
            String cleanC = current.replaceAll("[^\\d]", "");

            int cl = clean.length();
            int sel = cl;
            for (int i = 2; i <= cl && i < 6; i += 2) {
                sel++;
            }
            if (clean.equals(cleanC)) sel--;

            if (clean.length() < 8) {
                clean = clean + ddmmyyyy.substring(clean.length());
            } else {
                int day = Integer.parseInt(clean.substring(0, 2));
                int mon = Integer.parseInt(clean.substring(2, 4));
                int year = Integer.parseInt(clean.substring(4, 8));

                mon = Math.max(1, Math.min(mon, 12));
                cal.set(Calendar.MONTH, mon - 1);
                year = Math.max(1900, Math.min(year, 2100));
                cal.set(Calendar.YEAR, year);
                day = Math.max(1, Math.min(day, cal.getActualMaximum(Calendar.DAY_OF_MONTH)));
                clean = String.format(Locale.getDefault(), "%02d%02d%04d", day, mon, year);
            }

            clean = String.format("%s/%s/%s", clean.substring(0, 2),
                    clean.substring(2, 4),
                    clean.substring(4, 8));

            current = clean;
            editText.setText(current);
            editText.setSelection(Math.min(sel, current.length()));
        }
    }

    @Override
    public void afterTextChanged(Editable s) {}
}
