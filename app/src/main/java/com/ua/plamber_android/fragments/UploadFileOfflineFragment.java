package com.ua.plamber_android.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.ua.plamber_android.R;
import com.ua.plamber_android.activitys.FilePickActivity;
import com.ua.plamber_android.activitys.UploadActivity;
import com.ua.plamber_android.fragments.dialogs.UploadOfflineDialog;
import com.ua.plamber_android.utils.Validate;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UploadFileOfflineFragment extends Fragment {

    @BindView(R.id.upload_offline_parent)
    LinearLayout mPrentLayout;
    @BindView(R.id.upload_book_offline_name)
    TextInputLayout mTilUploadBoookName;
    @BindView(R.id.et_upload_offline_book_name)
    EditText mUploadBookName;
    @BindView(R.id.upload_book_offline_file)
    TextInputLayout mTilUploadFile;
    @BindView(R.id.et_upload_book_offline_file)
    EditText mUploadFile;

    private File mFile;
    private Validate validate;
    public static final String BOOK_NAME = "BOOK_NAME";
    public static final String FILE_PATH = "FILE_PATH";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        validate = new Validate(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upload_file_offline, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.btn_upload_offline_file)
    public void uploadOffline() {
        if (validateFields()) {
            UploadOfflineDialog uploadDialog = new UploadOfflineDialog();
            Bundle args = new Bundle();
            args.putString(BOOK_NAME, mUploadBookName.getText().toString().trim());
            args.putString(FILE_PATH, mFile.getAbsolutePath());
            uploadDialog.setArguments(args);
            uploadDialog.setCancelable(false);
            uploadDialog.show(getFragmentManager(), UploadOfflineDialog.TAG);
        }
    }

    @OnClick(R.id.btn_upload_select_offline_file)
    public void selectBookFile() {
        startActivityForResult(FilePickActivity.startBookFilePickActivity(getActivity()), UploadActivity.REQUEST_SELECT_FILE);
    }

    private boolean validateFields() {
        boolean isValid = false;
        if (validate.bookValidate(mUploadBookName, mTilUploadBoookName, R.string.valid_book_name)
                & validate.bookValidate(mUploadFile, mTilUploadFile, R.string.valid_book_file))
            isValid = true;

        return isValid;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == UploadActivity.REQUEST_SELECT_FILE) {
            if (resultCode == Activity.RESULT_OK) {
                mFile = new File(data.getStringExtra(FilePickActivity.FILE_PATH));
                mUploadFile.setText(mFile.getName());
            }
        }
    }
}
