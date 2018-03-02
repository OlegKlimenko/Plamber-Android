package com.ua.plamber_android.fragments.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.ua.plamber_android.R;
import com.ua.plamber_android.api.WorkAPI;
import com.ua.plamber_android.fragments.DetailBookFragment;
import com.ua.plamber_android.interfaces.callbacks.StatusCallback;
import com.ua.plamber_android.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddRatedDialog extends DialogFragment {
    public static final String TAG = "AddRatedDialog";
    public static final String BOOK_ID_RATED = "BOOK_ID_RATED";
    private long bookId;

    @BindView(R.id.rating_bar)
    RatingBar mRatingBar;
    @BindView(R.id.tv_please_rated)
    TextView mRatedInfo;
    @BindView(R.id.rated_number)
    TextView mRatedNumber;
    WorkAPI workAPI;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        workAPI = new WorkAPI(getActivity());
        bookId = getArguments().getLong(BOOK_ID_RATED);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_add_rated, null);
        ButterKnife.bind(this, view);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view)
                .setPositiveButton(R.string.add_rated_btn, null)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();
                    }
                });
        initRatedBar();
        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        final AlertDialog dialog = (AlertDialog) getDialog();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mRatingBar.getRating() > 0) {
                    workAPI.addRated(new StatusCallback() {
                        @Override
                        public void onSuccess(@NonNull int status) {
                            dismiss();
                            getDetailBookFragment().viewMessage(getString(R.string.thank_you_for_your_opinion));
                        }

                        @Override
                        public void onError(@NonNull Throwable t) {
                            dismiss();
                            getDetailBookFragment().viewMessage(getString(R.string.error_has_occurred));
                            Log.i(TAG, t.getLocalizedMessage());
                        }
                    }, bookId, (int) mRatingBar.getRating());
                } else {
                    Utils.messageSnack(view, getString(R.string.rating_must_be_greater_0));
                }
            }
        });
    }

    private void initRatedBar() {
        mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                if (v > 0.0) {
                    mRatedInfo.setText(R.string.your_rating_is);
                    mRatedNumber.setVisibility(View.VISIBLE);
                    mRatedNumber.setText(String.valueOf((int) v));
                } else {
                    mRatedInfo.setText(R.string.please_rated_book);
                    mRatedNumber.setVisibility(View.GONE);
                }
            }
        });
    }

    private DetailBookFragment getDetailBookFragment() {
        return ((DetailBookFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.detail_fragment_container));
    }
}
