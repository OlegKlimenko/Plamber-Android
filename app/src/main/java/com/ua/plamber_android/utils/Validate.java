package com.ua.plamber_android.utils;

import android.content.Context;
import android.util.Patterns;
import android.widget.EditText;

import com.ua.plamber_android.R;

public class Validate {

    private Context context;

    public Validate(Context context) {
        this.context = context;
    }

    public boolean emailValidate(EditText editText) {
        boolean isValid = true;
        String email = editText.getText().toString();

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            isValid = false;
            editText.setError(context.getString(R.string.validate_email));
        }

        return isValid;
    }

    public boolean passwordValidate(EditText editText) {
        boolean isValid = true;
        String regex = "\\w{6,16}\\b";
        String password = editText.getText().toString();

        if (!password.matches(regex)) {
            isValid = false;
            editText.setError(context.getString(R.string.validate_password));
        }

        return isValid;
    }

    public boolean userNameValidate(EditText editText) {
        boolean isValid = true;
        String regex = "[A-Za-z0-9_]{2,30}";
        String user = editText.getText().toString();

        if (!user.matches(regex)) {
            isValid = false;
            editText.setError(context.getString(R.string.validate_username));
        }

        return isValid;
    }

    public boolean passwordAgainValidate(EditText editTextFirst, EditText editTextSecond) {
        boolean isValid = true;
        String passFirst = editTextFirst.getText().toString();
        String passSecond = editTextSecond.getText().toString();

        if (!passwordValidate(editTextFirst)) {
            isValid = false;
            editTextFirst.setError(context.getString(R.string.validate_password));
        } else if (!passFirst.equals(passSecond)) {
            isValid = false;
            editTextSecond.setError(context.getString(R.string.validate_password_same));
        }

        return isValid;
    }
}
