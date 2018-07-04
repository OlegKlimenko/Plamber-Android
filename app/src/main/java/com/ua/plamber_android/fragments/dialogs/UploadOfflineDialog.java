package com.ua.plamber_android.fragments.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.ua.plamber_android.R;
import com.ua.plamber_android.database.utils.BookUtilsDB;
import com.ua.plamber_android.fragments.UploadFileOfflineFragment;
import com.ua.plamber_android.utils.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UploadOfflineDialog extends DialogFragment {

    public static final String TAG = "UploadOfflineDialog";

    @BindView(R.id.tv_simple_progress)
    TextView mProgressMessage;

    private Utils utils;
    private BookUtilsDB bookUtilsDB;
    private String bookId;
    private File bookFile;
    private SaveFile saveFile;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        utils = new Utils(getActivity());
        bookUtilsDB = new BookUtilsDB(getActivity());
        bookId = Utils.generateId();
        bookFile = new File(getArguments().getString(UploadFileOfflineFragment.FILE_PATH));
        setRetainInstance(true);
    }

    @Override
    public void onDestroyView() {
        if (getDialog() != null && getRetainInstance())
            getDialog().setOnDismissListener(null);
        super.onDestroyView();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflate = getActivity().getLayoutInflater();
        View v = inflate.inflate(R.layout.simple_progress_dialog, null);
        ButterKnife.bind(this, v);
        mProgressMessage.setText(R.string.saving_file);
        if (saveFile == null || saveFile.isCancelled()) {
            saveFile = new SaveFile();
            saveFile.execute(bookFile);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(v)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (saveFile != null)
                            saveFile.cancel(true);
                    }
                });
        return builder.create();

    }

    private class SaveFile extends AsyncTask<File, Void, byte[]> {

        @Override
        protected byte[] doInBackground(File... files) {
            return utils.getFirstPageAsByte(files[0]);
        }

        @Override
        protected void onPostExecute(byte[] bytes) {
                saveByteAsImage(bytes);
                writeBookToDB();
                copyBookFile();
                if (getActivity() != null)
                getActivity().finish();
        }
    }

    private void saveByteAsImage(byte[] bytes) {
        try (FileOutputStream stream = new FileOutputStream(utils.getPngFileWithPath(bookId))) {
            stream.write(bytes);
        } catch (IOException e) {
            Log.i(TAG, e.getLocalizedMessage());
        }
    }

    private void writeBookToDB() {
        String bookName = getArguments().getString(UploadFileOfflineFragment.BOOK_NAME);
        bookUtilsDB.saveBookOffline(bookId, bookName);
    }

    private void copyBookFile(){
        try (FileInputStream in = new FileInputStream(bookFile)) {
            try (FileOutputStream out = new FileOutputStream(utils.getPdfFileWithPath(bookId))) {
                FileChannel inChannel = in.getChannel();
                FileChannel outChannel = out.getChannel();
                inChannel.transferTo(0, inChannel.size(), outChannel);
            }
        } catch (IOException e) {
            Log.i(TAG, e.getLocalizedMessage());
        }
    }
}
