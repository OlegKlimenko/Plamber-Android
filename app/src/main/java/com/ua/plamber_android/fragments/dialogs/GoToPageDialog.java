package com.ua.plamber_android.fragments.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.ua.plamber_android.R;
import com.ua.plamber_android.activitys.BookReaderActivity;
import com.ua.plamber_android.activitys.BookReaderLocalActivity;
import com.ua.plamber_android.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GoToPageDialog extends DialogFragment {
    public static final String TAG = "GoToPageDialog";

    @BindView(R.id.page_number)
    EditText mPgaeNumber;
    @BindView(R.id.parent_go_to)
    LinearLayout mParentGoTo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onDestroyView() {
        if (getDialog() != null && getRetainInstance())
            getDialog().setOnDismissListener(null);
        super.onDestroyView();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflate = getActivity().getLayoutInflater();
        View v = inflate.inflate(R.layout.dialog_go_to_page, null);
        ButterKnife.bind(this, v);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(v)
                .setPositiveButton(R.string.go_to, null).setNegativeButton(R.string.cancel, (dialogInterface, i) -> dismiss());
        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        AlertDialog dialog = (AlertDialog) getDialog();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pageNumber = mPgaeNumber.getText().toString().trim();
                int page = pageNumber.isEmpty() ? 0 : Integer.parseInt(pageNumber) - 1;
                if (getArguments() != null && getArguments().getBoolean(BookReaderLocalActivity.TAG, false)){
                    localReader(page);
                    return;
                }

                reader(page);
            }
        });
    }

    private void localReader(int page) {
        if (page > getLocalReaderActivity().getCountPage()) {
            Utils.messageSnack(mParentGoTo, getString(R.string.number_of_page_in_this_book) + getLocalReaderActivity().getCountPage());
            return;
        }

        getLocalReaderActivity().goToPage(page);
        dismiss();
    }

    private void reader(int page) {
        if (page > getReaderActivity().getCountPage()) {
            Utils.messageSnack(mParentGoTo, getString(R.string.number_of_page_in_this_book) + getReaderActivity().getCountPage());
            return;
        }

        getReaderActivity().goToPage(page);
        dismiss();
    }

    private BookReaderActivity getReaderActivity() {
        return (BookReaderActivity) getActivity();
    }

    private BookReaderLocalActivity getLocalReaderActivity() {
        return (BookReaderLocalActivity) getActivity();
    }
}
