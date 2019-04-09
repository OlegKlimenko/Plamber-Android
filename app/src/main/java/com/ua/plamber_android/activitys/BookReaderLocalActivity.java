package com.ua.plamber_android.activitys;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.github.barteksc.pdfviewer.PDFView;
import com.ua.plamber_android.R;
import com.ua.plamber_android.database.model.LocalBookDB;
import com.ua.plamber_android.database.utils.LocalBookUtils;
import com.ua.plamber_android.fragments.dialogs.GoToPageDialog;
import com.ua.plamber_android.utils.FileHelper;
import com.ua.plamber_android.utils.PreferenceUtils;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookReaderLocalActivity extends AppCompatActivity {

    public static final String LOCAL_BOOK_NAME = "LOCAL_BOOK_NAME";
    public static final String LOCAL_BOOK_FILE = "LOCAL_BOOK_FILE";
    public static final String TAG = BookReaderLocalActivity.class.getSimpleName();

    @BindView(R.id.pdf_view)
    PDFView mPdfView;
    @BindView(R.id.reader_drawer_layout)
    DrawerLayout mReaderDrawer;
    @BindView(R.id.reader_navigation_view)
    NavigationView mNavigationView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    private String mBookName;
    private String mBookId;
    private String mBookPath;
    private LocalBookUtils mBookUtils;
    private PreferenceUtils preferenceUtils;
    private boolean isLoadPdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_reader);
        ButterKnife.bind(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        initBook();
        mBookUtils = new LocalBookUtils(this);
        preferenceUtils = new PreferenceUtils(this);
        initToolbar();
        initNavigationView();
        if (!mBookUtils.isLocalBookSaveDB(mBookPath)) {
            mBookId = mBookUtils.saveBookLocal(createBook());
            viewPdf(0);
            return;
        }

        mBookId = mBookUtils.getIdLocalBook(mBookPath);
        //maybe work better??????????
        mBookUtils.updateDate(mBookPath, System.currentTimeMillis());
        viewPdf(mBookUtils.getLastLocalBookPage(mBookId));
    }

    private void initBook() {
        try {
            if (getIntent().getData() == null) {
                mBookName = getIntent().getStringExtra(LOCAL_BOOK_NAME);
                mBookPath = getIntent().getStringExtra(LOCAL_BOOK_FILE);
                return;
            }
            File file = new File(FileHelper.getPathFromURIFiles(getBaseContext(), getIntent().getData()));
            mBookName = file.getName();
            mBookPath = file.getAbsolutePath();

        } catch (Exception e) {
           e.printStackTrace();
        }
    }

    private LocalBookDB createBook() {
        LocalBookDB book = new LocalBookDB();
        book.setBookName(mBookName);
        book.setBookPath(mBookPath);
        book.setBookAvatar("");
        return book;
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(mBookName);
            getSupportActionBar().setElevation(10);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        savePage();
    }

    private void initNavigationView() {
        setHeaderBackground();
        setBookInformation();
        initHideElement(PreferenceUtils.TOOLBAR_HIDE, R.id.reader_nav_hide_toolbar);
        initHideElement(PreferenceUtils.STATUS_BAR_HIDE, R.id.reader_nav_hide_status_bar);
        initSwitchListener();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mReaderDrawer, mToolbar, R.string.drawer_open, R.string.drawer_close) {
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                setPages(mPdfView.getCurrentPage(), getCountPage());
            }
        };
        mReaderDrawer.addDrawerListener(toggle);
        toggle.syncState();
        mNavigationView.getMenu().findItem(R.id.reader_synchronize).setVisible(false);
        mNavigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            switch (id) {
                case R.id.reader_nav_go_to_page:
                    startGoToPage();
                    mReaderDrawer.closeDrawers();
                    break;
                case R.id.reader_nav_align:
                    fitWidth();
                    mReaderDrawer.closeDrawers();
                    break;
                case R.id.reader_exit:
                    finish();
                    break;
            }
            return true;
        });
    }

    private void setBookInformation() {
        TextView name = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.reader_book_name);
        name.setText(mBookName);
        viewCover();
    }

    private void viewCover() {
        ImageView photo = (ImageView) mNavigationView.getHeaderView(0).findViewById(R.id.reader_book_photo);
        Glide.with(getApplicationContext()).load(R.drawable.pdf_book).into(photo);
    }

    private void initHideElement(String element, int menu) {
        if (!preferenceUtils.checkPreference(element)) {
            preferenceUtils.writeLogic(element, false);
        } else {
            boolean status = preferenceUtils.readLogic(element);
            getSwitchHide(menu).setChecked(status);
            if (element.equals(PreferenceUtils.STATUS_BAR_HIDE) && status) {
                hideStatusBar(true);
            } else if (element.equals(PreferenceUtils.TOOLBAR_HIDE) && status) {
                hideToolbar(true);
            }
        }
    }

    private void savePage() {
        if (isLoadPdf)
            mBookUtils.updatePage(mBookId, mPdfView.getCurrentPage());
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (preferenceUtils.readLogic(PreferenceUtils.STATUS_BAR_HIDE)) {
            menu.findItem(R.id.reader_nav_hide_status_bar).setTitle(R.string.show_status_bar);
        } else {
            menu.findItem(R.id.reader_nav_hide_status_bar).setTitle(R.string.hide_status_bar);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mReaderDrawer.openDrawer(GravityCompat.START);
                break;
            case R.id.reader_nav_go_to_page:
                startGoToPage();
            case R.id.reader_nav_align:
                fitWidth();
                break;
            case R.id.reader_nav_hide_status_bar:
                getSwitchHide(R.id.reader_nav_hide_status_bar).setChecked(!preferenceUtils.readLogic(PreferenceUtils.STATUS_BAR_HIDE));
                break;
            case R.id.reader_nav_hide_toolbar:
                getSwitchHide(R.id.reader_nav_hide_toolbar).setChecked(!preferenceUtils.readLogic(PreferenceUtils.TOOLBAR_HIDE));
                break;
            case R.id.reader_exit:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initSwitchListener() {
        getSwitchHide(R.id.reader_nav_hide_toolbar).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                preferenceUtils.writeLogic(PreferenceUtils.TOOLBAR_HIDE, b);
                hideToolbar(b);
            }
        });

        getSwitchHide(R.id.reader_nav_hide_status_bar).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                preferenceUtils.writeLogic(PreferenceUtils.STATUS_BAR_HIDE, b);
                hideStatusBar(b);
            }
        });
    }

    private void postFitWidth() {
        if (isLoadPdf)
            new Handler().post(this::fitWidth);
    }

    private void hideStatusBar(boolean view) {
        if (view) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            postFitWidth();
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            postFitWidth();
        }
    }

    private void hideToolbar(boolean view) {
        if (getSupportActionBar() != null) {
            if (view) {
                getSupportActionBar().hide();
                postFitWidth();
            } else {
                getSupportActionBar().show();
                postFitWidth();
            }
        }
    }

    private SwitchCompat getSwitchHide(int menu) {
        return (SwitchCompat) mNavigationView.getMenu().findItem(menu).getActionView().findViewById(R.id.menu_switch);
    }

    private void startGoToPage() {
        GoToPageDialog goToPageDialog = new GoToPageDialog();
        goToPageDialog.setCancelable(false);
        Bundle args = new Bundle();
        args.putBoolean(TAG, true);
        goToPageDialog.setArguments(args);
        goToPageDialog.show(getSupportFragmentManager(), GoToPageDialog.TAG);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.book_reader_drawer, menu);
        menu.findItem(R.id.reader_synchronize).setVisible(false);
        return true;
    }

    private void fitWidth() {
        mPdfView.zoomTo((mPdfView.getWidth() - 1) / mPdfView.getOptimalPageWidth());
        mPdfView.jumpTo(mPdfView.getCurrentPage());
    }

    public void goToPage(int page) {
        mPdfView.jumpTo(page);
    }

    private void viewPdf(final int currentPage) {
        mPdfView.fromFile(new File(mBookPath))
                .onRender((nbPages, pageWidth, pageHeight) -> {
                    mPdfView.zoomTo((mPdfView.getWidth() - 1) / mPdfView.getOptimalPageWidth());
                    mPdfView.jumpTo(currentPage);
                    setPages(mPdfView.getCurrentPage(), mPdfView.getPageCount());
                    isLoadPdf = true;
                }).enableAntialiasing(true).spacing(10).load();

    }

    public int getCountPage() {
        return mPdfView.getPageCount();
    }

    private void setPages(int current, int all) {
        TextView currentText = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.reader_current_page);
        currentText.setText(String.valueOf(current));
        TextView allPageText = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.all_page);
        allPageText.setText(String.valueOf(all));
    }

    public static Intent startLocalReaderActivity(Context context) {
        return new Intent(context, BookReaderLocalActivity.class);
    }

    private void setHeaderBackground() {
        ImageView headerImage = (ImageView) mNavigationView.getHeaderView(0).findViewById(R.id.nav_reader_header_image);
        Glide.with(getApplicationContext()).load(R.drawable.main_background).apply(new RequestOptions().transform(new CenterCrop())).into(headerImage);
    }
}
