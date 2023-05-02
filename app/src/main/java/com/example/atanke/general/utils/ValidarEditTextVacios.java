package com.example.atanke.general.utils;

import android.text.TextUtils;
import android.widget.EditText;

import java.util.List;

public class ValidarEditTextVacios {
    //se envia una lista de editText retorna false si algo esta incompleto
    public static boolean validarEditTextNoVacio(List<EditText> editTexts) {
        for (EditText editText : editTexts) {
            String texto = editText.getText().toString().trim();
            if (TextUtils.isEmpty(texto)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}
