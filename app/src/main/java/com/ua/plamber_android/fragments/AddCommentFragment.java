package com.ua.plamber_android.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.ua.plamber_android.R;
import com.ua.plamber_android.activitys.DetailBookActivity;
import com.ua.plamber_android.api.WorkAPI;
import com.ua.plamber_android.interfaces.callbacks.CommentCallback;
import com.ua.plamber_android.model.Comment;
import com.ua.plamber_android.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddCommentFragment extends DialogFragment {

    public static final String TAG = "AddCommentFragment";
    public static final String BOOK_ID_COMMENT = "BOOK_ID_COMMENT";
    private long bookId;

    @BindView(R.id.et_comment_text)
    TextView mCommentText;
    WorkAPI workAPI;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        workAPI = new WorkAPI(getActivity());
        bookId = getArguments().getLong(BOOK_ID_COMMENT);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.fragamnt_add_comment, null);
        ButterKnife.bind(this, v);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(v)
                .setPositiveButton(R.string.add_comment_btn, null)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();
                    }
                });
        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        AlertDialog dialog = (AlertDialog) getDialog();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(mCommentText.getText())) {
                    workAPI.addComment(new CommentCallback() {
                        @Override
                        public void onSuccess(@NonNull Comment.CommentRespond comment) {
                                getDetailBookFragment().updateComment(comment.getCommentData(), getString(R.string.thank_you_for_your_opinion));
                                dismiss();

                        }

                        @Override
                        public void onError(@NonNull Throwable t) {
                            Log.i(TAG, t.getLocalizedMessage());
                            dismiss();
                            getDetailBookFragment().viewMessage(getString(R.string.error_has_occurred));
                        }
                    }, bookId, mCommentText.getText().toString().trim());
                } else {
                    Utils.messageSnack(view, getString(R.string.please_enter_comment));
                }
            }
        });
    }

    private DetailBookFragment getDetailBookFragment() {
        return ((DetailBookFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.detail_fragment_container));
    }
}
