package com.ua.plamber_android.utils;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.widget.EditText;

import com.ua.plamber_android.R;

public class Validate {

    private Context context;

    public Validate(Context context) {
        this.context = context;
    }

    public boolean emailValidate(EditText editText, final TextInputLayout inputLayout) {
        boolean isValid = true;
        inputLayout.setErrorEnabled(true);
        String email = editText.getText().toString();

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            isValid = false;
            inputLayout.setError(context.getString(R.string.validate_email));
        }

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                inputLayout.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return isValid;
    }

    public boolean bookValidate(EditText editText, final TextInputLayout inputLayout, int text) {
        boolean isValid = true;
        inputLayout.setErrorEnabled(true);
        String regex = "[A-Za-z0-9_ .]{2,150}";
        String name = editText.getText().toString();
        if (name.length() <= 0) {
            isValid = false;
            inputLayout.setError(context.getString(text));
        }
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                inputLayout.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        return isValid;
    }

    public boolean bookValidate(EditText editText, final TextInputLayout inputLayout, int text, String  regex) {
        boolean isValid = true;
        inputLayout.setErrorEnabled(true);
        String name = editText.getText().toString();
        if (!name.matches(regex)) {
            isValid = false;
            inputLayout.setError(context.getString(text));
        }
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                inputLayout.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        return isValid;
    }

    public boolean passwordValidate(EditText editText, final TextInputLayout inputLayout) {
        boolean isValid = true;
        inputLayout.setErrorEnabled(true);
        String regex = "\\w{6,16}\\b";
        String password = editText.getText().toString();

        if (!password.matches(regex)) {
            isValid = false;
            inputLayout.setError(context.getString(R.string.validate_password));
        }

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                inputLayout.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return isValid;
    }

    public boolean userNameValidate(EditText editText, final TextInputLayout inputLayout) {
        boolean isValid = true;
        inputLayout.setErrorEnabled(true);
        String regex = "^[a-zA-Z0-9_]{2,30}|^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
        String user = editText.getText().toString();

        if (!user.matches(regex)) {
            isValid = false;
            inputLayout.setError(context.getString(R.string.validate_username));
        }
        else if (user.equals("admin")) {
            isValid = false;
            inputLayout.setError(context.getString(R.string.not_allowed_user_name));
        }

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                inputLayout.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return isValid;
    }

    public boolean passwordAgainValidate(EditText editTextFirst, EditText editTextSecond, final TextInputLayout inputLayoutFirst, final TextInputLayout inputLayoutSecond) {
        boolean isValid = true;
        inputLayoutFirst.setErrorEnabled(true);
        inputLayoutSecond.setErrorEnabled(true);
        String passFirst = editTextFirst.getText().toString();
        String passSecond = editTextSecond.getText().toString();

        if (!passwordValidate(editTextFirst, inputLayoutFirst) & !passwordValidate(editTextSecond, inputLayoutSecond)) {
            isValid = false;
        } else if (!passFirst.equals(passSecond)) {
            isValid = false;
            inputLayoutSecond.setError(context.getString(R.string.validate_password_same));
        }

        editTextFirst.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                inputLayoutFirst.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        editTextSecond.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                inputLayoutSecond.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return isValid;
    }

    public boolean passwordNoClone(EditText editTextFirst, EditText editTextSecond, final TextInputLayout inputLayoutFirst, final TextInputLayout inputLayoutSecond) {
        boolean isValid = true;
        inputLayoutFirst.setErrorEnabled(true);
        inputLayoutSecond.setErrorEnabled(true);
        String passFirst = editTextFirst.getText().toString();
        String passSecond = editTextSecond.getText().toString();

        if (!passwordValidate(editTextFirst, inputLayoutFirst)) {
            isValid = false;
        } else if (passFirst.equals(passSecond)) {
            isValid = false;
            inputLayoutSecond.setError(context.getString(R.string.validate_password_clone));
        }

        editTextFirst.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                inputLayoutFirst.setErrorEnabled(false);
                inputLayoutSecond.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        editTextSecond.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                inputLayoutSecond.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return isValid;
    }
}
