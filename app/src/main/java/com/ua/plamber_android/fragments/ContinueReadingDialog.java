package com.ua.plamber_android.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.ua.plamber_android.R;
import com.ua.plamber_android.activitys.BookReaderActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContinueReadingDialog extends DialogFragment {

    public static final String TAG = "ContinueReadingDialog";

    @BindView(R.id.rb_cloud_page)
    RadioButton mCloudPage;
    @BindView(R.id.rb_local_page)
    RadioButton mLocalPage;
    @BindView(R.id.cloud_page_value)
    TextView mCloudValue;
    @BindView(R.id.local_page_value)
    TextView mLocalValue;

    private int cloudPage;
    private int localPage;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cloudPage = getArguments().getInt(BookReaderActivity.CLOUD_PAGE);
        localPage = getArguments().getInt(BookReaderActivity.LOCAL_PAGE);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.continue_reading_dialog, null);
        ButterKnife.bind(this, view);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (mCloudPage.isChecked())
                    getParentActivity().continueRead(cloudPage);
                else
                    getParentActivity().continueRead(localPage);
            }
        })
        .setTitle("From where you want to continue?");
        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        AlertDialog dialog = (AlertDialog) getDialog();
        mCloudValue.setText(String.valueOf(cloudPage));
        mLocalValue.setText(String.valueOf(localPage));
    }

    private BookReaderActivity getParentActivity() {
       return  (BookReaderActivity) getActivity();
    }
}
