package com.ua.plamber_android.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.ua.plamber_android.R;
import com.ua.plamber_android.activitys.DetailBookActivity;
import com.ua.plamber_android.activitys.FilePickActivity;
import com.ua.plamber_android.activitys.SelectCategoryActivity;
import com.ua.plamber_android.activitys.SelectLanguageActivity;
import com.ua.plamber_android.activitys.UploadActivity;
import com.ua.plamber_android.api.APIUtils;
import com.ua.plamber_android.api.WorkAPI;
import com.ua.plamber_android.database.utils.BookUtilsDB;
import com.ua.plamber_android.fragments.dialogs.UploadBookDialog;
import com.ua.plamber_android.interfaces.callbacks.BooksCallback;
import com.ua.plamber_android.interfaces.callbacks.StatusCallback;
import com.ua.plamber_android.interfaces.callbacks.StringListCallback;
import com.ua.plamber_android.model.Book;
import com.ua.plamber_android.model.Upload;
import com.ua.plamber_android.utils.PreferenceUtils;
import com.ua.plamber_android.utils.Utils;
import com.ua.plamber_android.utils.Validate;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UploadFileFragment extends Fragment {

    @BindView(R.id.upload_book_name)
    TextInputLayout mTilBookName;
    @BindView(R.id.et_upload_book_name)
    AutoCompleteTextView mBookName;
    @BindView(R.id.upload_book_author)
    TextInputLayout mTilBookAuthor;
    @BindView(R.id.et_upload_book_author)
    AutoCompleteTextView mBookAuthor;
    @BindView(R.id.upload_book_category)
    TextInputLayout mTilBookCategory;
    @BindView(R.id.et_upload_book_category)
    EditText mBookCategory;
    @BindView(R.id.upload_book_file)
    TextInputLayout mTilBookFile;
    @BindView(R.id.et_upload_book_file)
    EditText mBookFile;
    @BindView(R.id.upload_book_language)
    TextInputLayout mTilBookLanguage;
    @BindView(R.id.et_upload_book_language)
    EditText mBookLanguage;
    @BindView(R.id.upload_book_about)
    TextInputLayout mTilBookAbout;
    @BindView(R.id.et_upload_book_about)
    EditText mBookAbout;
    @BindView(R.id.upload_is_private)
    CheckBox mBookIsPrivate;
    @BindView(R.id.author_progress_complete)
    ProgressBar mAuthorProgressComplete;
    @BindView(R.id.book_progress_complete)
    ProgressBar mBookProgressComplete;
    @BindView(R.id.btn_upload_select_file)
    ImageButton mUploadSelectFile;

    private Utils utils;
    private PreferenceUtils preferenceUtils;
    private APIUtils apiUtils;
    private WorkAPI workAPI;
    private Validate validate;
    private File mFile;
    private String currentAutoComplete;
    private BookUtilsDB bookUtilsDB;
    private boolean isBookOffline;
    private String bookId;

    private static final String CURRENT_AUTO_COMPLETE = "CURRENT_AUTO_COMPLETE";
    private static final String TAG = "UploadFileFragment";
    public static final String CATEGORY_NAME = "CATEGORY_NAME";
    public static final String BOOK_LANGUAGE = "BOOK_LANGUAGE";
    private Timer timer;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        utils = new Utils(getActivity());
        preferenceUtils = new PreferenceUtils(getActivity());
        apiUtils = new APIUtils(getActivity());
        workAPI = new WorkAPI(getActivity());
        validate = new Validate(getActivity());
        bookUtilsDB = new BookUtilsDB(getActivity());
        isBookOffline = getArguments().getBoolean(DetailBookFragmentOffline.IS_OFFLINE_BOOK);
        bookId = getArguments().getString(DetailBookFragmentOffline.BOOK_OFFLINE_KEY);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragament_upload_file, container, false);
        ButterKnife.bind(this, view);

        if (savedInstanceState != null)
            currentAutoComplete = savedInstanceState.getString(CURRENT_AUTO_COMPLETE);

        mBookAuthor.addTextChangedListener(setAuthorWatcher(500));
        mBookName.addTextChangedListener(setBookWatcher(500));
        initUploadOfflineFile();
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(CURRENT_AUTO_COMPLETE, currentAutoComplete);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null)
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    private void initUploadOfflineFile() {
        if (isBookOffline) {
            mUploadSelectFile.setEnabled(false);
            mBookName.setText(bookUtilsDB.readBookFromDB(bookId).getBookName());
            mBookFile.setText(bookUtilsDB.readBookFromDB(bookId).getBookName() + ".pdf");
            mBookFile.setEnabled(false);
            mFile = new File(utils.getPdfFileWithPath(bookId));
        }
    }

    public void updateOfflineBook(Book.BookData bookData) {
        if (isBookOffline) {
            bookUtilsDB.updateOfflineBook(bookId, bookData);
            utils.removeImage(bookId);
            workAPI.setLastPage(new StatusCallback() {
                @Override
                public void onSuccess(@NonNull int status) {

                }

                @Override
                public void onError(@NonNull Throwable t) {

                }
            }, bookData.getIdServerBook(), bookUtilsDB.readLastPage(bookId));
        }
    }

    @OnClick(R.id.btn_upload_file)
    public void uploadFile() {
        if (validateFields()) {
            Upload.UploadBookRequest uploadBookRequest = new Upload.UploadBookRequest(preferenceUtils.readPreference(PreferenceUtils.TOKEN), getText(mBookName), getText(mBookAuthor), getText(mBookCategory), mFile.getPath(), getText(mBookAbout), getText(mBookLanguage), mBookIsPrivate.isChecked());

            Bundle args = new Bundle();
            args.putString(UploadBookDialog.UPLOAD_BOOK, new Gson().toJson(uploadBookRequest, Upload.UploadBookRequest.class));
            UploadBookDialog uploadBookDialog = new UploadBookDialog();
            uploadBookDialog.setArguments(args);
            uploadBookDialog.setCancelable(false);
            uploadBookDialog.show(getFragmentManager(), UploadBookDialog.TAG);
        }
    }

    private String getText(EditText editText) {
        return editText.getText().toString().trim();
    }

    private boolean validateFields() {
        boolean isValid = false;
        if (validate.bookValidate(mBookName, mTilBookName, R.string.valid_book_name) &&
                validate.bookValidate(mBookAuthor, mTilBookAuthor, R.string.valid_book_author) &&
                validate.bookValidate(mBookCategory, mTilBookCategory, R.string.valid_book_category) &&
                validate.bookValidate(mBookFile, mTilBookFile, R.string.valid_book_file) &&
                validate.bookValidate(mBookLanguage, mTilBookLanguage, R.string.valid_book_language)
                && validate.bookValidate(mBookAbout, mTilBookAbout, R.string.valid_book_about)) {
            isValid = true;
        }
        return isValid;
    }

    @OnClick(R.id.btn_upload_select_file)
    public void selectBookFile() {
        startActivityForResult(FilePickActivity.startBookFilePickActivity(getActivity()), UploadActivity.REQUEST_SELECT_FILE);
    }

    @OnClick(R.id.et_upload_book_file)
    public void textSelectBookFile() {
        startActivityForResult(FilePickActivity.startBookFilePickActivity(getActivity()), UploadActivity.REQUEST_SELECT_FILE);
    }

    @OnClick(R.id.btn_upload_select_category)
    public void selectCategory() {
        startActivityForResult(SelectCategoryActivity.startCategoryUploadActivity(getActivity()), UploadActivity.REQUEST_SELECT_CATEGORY);
    }

    @OnClick(R.id.et_upload_book_category)
    public void textSelectCategory() {
        startActivityForResult(SelectCategoryActivity.startCategoryUploadActivity(getActivity()), UploadActivity.REQUEST_SELECT_CATEGORY);
    }

    @OnClick(R.id.btn_upload_select_language)
    public void selectBookLanguage() {
        startActivityForResult(SelectLanguageActivity.startSelectLanguageActivity(getActivity()), UploadActivity.REQUEST_SELECT_LANGUAGE);
    }

    @OnClick(R.id.et_upload_book_language)
    public void textBookLanguage() {
        startActivityForResult(SelectLanguageActivity.startSelectLanguageActivity(getActivity()), UploadActivity.REQUEST_SELECT_LANGUAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == UploadActivity.REQUEST_SELECT_FILE) {
            if (resultCode == Activity.RESULT_OK) {
                mFile = new File(data.getStringExtra(FilePickActivity.FILE_PATH));
                mBookFile.setText(mFile.getName());
            }
        } else if (requestCode == UploadActivity.REQUEST_SELECT_CATEGORY) {
            if (resultCode == Activity.RESULT_OK) {
                mBookCategory.setText(data.getStringExtra(CATEGORY_NAME));
            }
        } else if (requestCode == UploadActivity.REQUEST_SELECT_LANGUAGE) {
            if (resultCode == Activity.RESULT_OK) {
                mBookLanguage.setText(data.getStringExtra(BOOK_LANGUAGE));
            }
        }
    }

    private TextWatcher setAuthorWatcher(final int delay) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (timer != null) {
                    timer.cancel();
                }
            }

            @Override
            public void afterTextChanged(final Editable editable) {
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (editable.toString().length() > 1 && !editable.toString().equals(currentAutoComplete) && getActivity() != null) {
                            final ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line);
                            currentAutoComplete = "";
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mAuthorProgressComplete.setVisibility(View.VISIBLE);
                                }
                            });
                            workAPI.autoCompleteAuthor(new StringListCallback() {
                                @Override
                                public void onSuccess(@NonNull final List<String> stringsList) {
                                    adapter.clear();
                                    adapter.addAll(stringsList);
                                    adapter.notifyDataSetChanged();
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mBookAuthor.setAdapter(adapter);
                                            mAuthorProgressComplete.setVisibility(View.GONE);

                                            mBookAuthor.showDropDown();
                                            mBookAuthor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                    currentAutoComplete = stringsList.get(i);
                                                }
                                            });
                                        }
                                    });
                                }

                                @Override
                                public void onError(@NonNull Throwable t) {
                                    mAuthorProgressComplete.setVisibility(View.GONE);
                                }
                            }, editable.toString());
                        }
                    }
                }, delay);
            }
        };
    }

    private TextWatcher setBookWatcher(final int delay) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (timer != null) {
                    timer.cancel();
                }
            }

            @Override
            public void afterTextChanged(final Editable editable) {
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (editable.toString().length() > 1 && !editable.toString().equals(currentAutoComplete) && getActivity() != null) {
                            final ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line);
                            currentAutoComplete = "";
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mBookProgressComplete.setVisibility(View.VISIBLE);
                                }
                            });
                            workAPI.autoCompleteBook(new BooksCallback() {
                                @Override
                                public void onSuccess(@NonNull final List<Book.BookData> books) {
                                    final List<String> bookNames = new ArrayList<>();
                                    for (Book.BookData book : books) {
                                        bookNames.add(book.getBookName());
                                    }
                                    adapter.clear();
                                    adapter.addAll(bookNames);
                                    adapter.notifyDataSetChanged();
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mBookName.setAdapter(adapter);
                                            mBookProgressComplete.setVisibility(View.GONE);
                                            mBookName.showDropDown();
                                            mBookName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                    currentAutoComplete = bookNames.get(i);
                                                    Intent intent = DetailBookActivity.startDetailActivity(view.getContext());
                                                    intent.putExtra(DetailBookActivity.BOOK_SERVER_ID, books.get(i).getIdServerBook());
                                                    startActivityForResult(intent, BaseViewBookFragment.ADDED_REQUEST);
                                                }
                                            });
                                        }
                                    });
                                }

                                @Override
                                public void onError(@NonNull Throwable t) {

                                }
                            }, editable.toString());
                        }
                    }
                }, delay);
            }
        };
    }
}
