package com.ua.plamber_android.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ua.plamber_android.R;
import com.ua.plamber_android.api.APIUtils;
import com.ua.plamber_android.api.WorkAPI;
import com.ua.plamber_android.fragments.BaseViewBookFragment;
import com.ua.plamber_android.fragments.UploadDialogFragment;
import com.ua.plamber_android.interfaces.callbacks.BooksCallback;
import com.ua.plamber_android.interfaces.callbacks.StringListCallback;
import com.ua.plamber_android.model.Book;
import com.ua.plamber_android.model.Upload;
import com.ua.plamber_android.utils.TokenUtils;
import com.ua.plamber_android.utils.Utils;
import com.ua.plamber_android.utils.Validate;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UploadActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
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


    @BindString(R.string.upload_book_activity)
    String title;

    Utils utils;
    TokenUtils tokenUtils;
    APIUtils apiUtils;
    WorkAPI workAPI;
    Validate validate;
    File mFile;
    String currentAutoComplete;

    private static final String TAG = "UploadActivity";
    private static final int REQUEST_SELECT_FILE = 205;
    private static final int REQUEST_SELECT_CATEGORY = 105;
    private static final int REQUEST_SELECT_LANGUAGE = 123;
    public static final String FILE_PATH = "FILEPATH";
    public static final String CATEGORY_NAME = "CATEGORY_NAME";
    public static final String BOOK_LANGUAGE = "BOOK_LANGUAGE";
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        ButterKnife.bind(this);
        utils = new Utils(this);
        tokenUtils = new TokenUtils(this);
        apiUtils = new APIUtils(this);
        workAPI = new WorkAPI(this);
        validate = new Validate(this);

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setElevation(10);
            getSupportActionBar().setTitle(title);
        }

        mBookAuthor.addTextChangedListener(setAuthorWatcher(500));
        mBookName.addTextChangedListener(setBookWatcher(500));
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    @OnClick(R.id.btn_upload_file)
    public void uploadFile() {
        if (validateFields()) {
            Upload.UploadRequest uploadRequest = new Upload.UploadRequest(tokenUtils.readToken(), getText(mBookName), getText(mBookAuthor), getText(mBookCategory), mFile.getPath(), getText(mBookAbout), getText(mBookLanguage), mBookIsPrivate.isChecked());

            Bundle args = new Bundle();
            args.putString(UploadDialogFragment.UPLOAD_BOOK, new Gson().toJson(uploadRequest, Upload.UploadRequest.class));
            UploadDialogFragment uploadDialogFragment = new UploadDialogFragment();
            uploadDialogFragment.setArguments(args);
            uploadDialogFragment.setCancelable(false);
            uploadDialogFragment.show(getSupportFragmentManager(), "UploadDialog");
        }
    }

    private String getText(EditText editText) {
        return editText.getText().toString().trim();
    }

    private boolean validateFields() {
        boolean isValid = false;
        if (validate.bookValidate(mBookName, mTilBookName, R.string.valid_book_name) &
                validate.bookValidate(mBookAuthor, mTilBookAuthor, R.string.valid_book_author) &
                validate.bookValidate(mBookCategory, mTilBookCategory, R.string.valid_book_category) &
                validate.bookValidate(mBookFile, mTilBookFile, R.string.valid_book_file) &
                validate.bookValidate(mBookLanguage, mTilBookLanguage, R.string.valid_book_language) &
                validate.bookValidate(mBookAbout, mTilBookAbout, R.string.valid_book_about)) {

            isValid = true;
        }
        return isValid;
    }

    @OnClick(R.id.btn_upload_select_file)
    public void selectBookFile() {
        startActivityForResult(BookFilePickActivity.startBookFilePickActivity(this), REQUEST_SELECT_FILE);
    }

    @OnClick(R.id.btn_upload_select_category)
    public void selectCategory() {
        startActivityForResult(SelectCategoryActivity.startCategoryUploadActivity(this), REQUEST_SELECT_CATEGORY);
    }

    @OnClick(R.id.btn_upload_select_language)
    public void selectBookLanguage() {
        startActivityForResult(SelectLanguageActivity.startSelectLanguageActivity(this), REQUEST_SELECT_LANGUAGE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SELECT_FILE) {
            if (resultCode == Activity.RESULT_OK) {
                mFile = new File(data.getStringExtra(FILE_PATH));
                mBookFile.setText(mFile.getName());
            }
        } else if (requestCode == REQUEST_SELECT_CATEGORY) {
            if (resultCode == Activity.RESULT_OK) {
                mBookCategory.setText(data.getStringExtra(CATEGORY_NAME));
            }
        } else if (requestCode == REQUEST_SELECT_LANGUAGE) {
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
                        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_dropdown_item_1line);
                        if (editable.toString().length() > 1 && !editable.toString().equals(currentAutoComplete)) {
                            currentAutoComplete = "";
                            runOnUiThread(new Runnable() {
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
                                    runOnUiThread(new Runnable() {
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
                        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_dropdown_item_1line);

                        if (editable.toString().length() > 1 && !editable.toString().equals(currentAutoComplete)) {
                            currentAutoComplete = "";
                            runOnUiThread(new Runnable() {
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
                                   runOnUiThread(new Runnable() {
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
                                                   intent.putExtra(BaseViewBookFragment.BOOKKEY, books.get(i).getIdBook());
                                                   startActivityForResult(intent, BaseViewBookFragment.ADDEDREQUEST);
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



    public static Intent startUploadActivity(Context context) {
        return new Intent(context, UploadActivity.class);
    }

    private void message(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
