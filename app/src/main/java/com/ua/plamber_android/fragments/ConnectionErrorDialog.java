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
import android.widget.TextView;

import com.ua.plamber_android.R;
import com.ua.plamber_android.activitys.LibraryActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ConnectionErrorDialog extends DialogFragment {

    public static final String TAG = "ConnectionErrorDialog";

    @BindView(R.id.connection_error_message)
    TextView mConnectionError;

    private String errorMessage;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        errorMessage = getArguments().getString(LibraryActivity.ERROR_MESSAGE);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.connection_error_dialog, null);
        ButterKnife.bind(this, view);
        mConnectionError.setText(errorMessage);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view)
                .setPositiveButton("Enable", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getLibraryActivity().switchToOffline();
                        dismiss();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dismiss();
            }
        }).setTitle(R.string.connetion_error_title);
        return builder.create();
    }

    private LibraryActivity getLibraryActivity() {
        return ((LibraryActivity) getActivity());
    }
}
