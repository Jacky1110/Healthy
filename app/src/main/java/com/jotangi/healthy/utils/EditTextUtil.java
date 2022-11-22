package com.jotangi.healthy.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class EditTextUtil {

    public interface OnChangedListener {
        void onChanged(String text);
    }

    public static void setOnTextChangeListener(EditText ed, final OnChangedListener listener) {

        ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                listener.onChanged(s.toString());
            }
        });
    }
}
