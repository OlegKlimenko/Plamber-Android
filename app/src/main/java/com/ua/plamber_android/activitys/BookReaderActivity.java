package com.ua.plamber_android.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.github.barteksc.pdfviewer.listener.OnRenderListener;
import com.ua.plamber_android.R;
import com.ua.plamber_android.api.PlamberAPI;
import com.ua.plamber_android.api.WorkAPI;
import com.ua.plamber_android.database.utils.PageUtilsDB;
import com.ua.plamber_android.fragments.GoToPageDialog;
import com.ua.plamber_android.interfaces.callbacks.PageCallback;
import com.ua.plamber_android.interfaces.callbacks.StatusCallback;
import com.ua.plamber_android.model.Page;
import com.ua.plamber_android.utils.FileUtils;
import com.ua.plamber_android.utils.PreferenceUtils;
import com.ua.plamber_android.utils.Utils;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookReaderActivity extends AppCompatActivity {

    @BindView(R.id.pdf_view)
    PDFView mPdfView;
    @BindView(R.id.reader_drawer_layout)
    DrawerLayout mReaderDrawer;
    @BindView(R.id.reader_navigation_view)
    NavigationView mNavigationView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    private static final String TAG = "BookReaderActivity";
    private static final String CURRENT_PAGE = "CURRENT_PAGE";
    private static final String COUNT_PAGE = "COUNT_PAGE";


    private PreferenceUtils preferenceUtils;
    private WorkAPI workAPI;
    private PageUtilsDB pageUtilsDB;
    private File file;
    private long bookId;
    private String bookPhoto;
    private String bookAuthor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_reader);
        ButterKnife.bind(this);
        preferenceUtils = new PreferenceUtils(this);
        workAPI = new WorkAPI(this);
        Intent intent = getIntent();
        String bookPath = intent.getStringExtra(DetailBookActivity.PDF_PATH);
        file = new File(bookPath);
        bookPhoto = intent.getStringExtra(DetailBookActivity.BOOK_PHOTO);
        bookAuthor = intent.getStringExtra(DetailBookActivity.BOOK_AUTHOR);
        bookId = intent.getLongExtra(DetailBookActivity.BOOK_ID, 0);
        pageUtilsDB = new PageUtilsDB(this, bookId);

        if (savedInstanceState != null) {
            setPages(savedInstanceState.getInt(CURRENT_PAGE), savedInstanceState.getInt(COUNT_PAGE));
        }
        initToolbar();
        initNavigationView();
        if (!preferenceUtils.readStatusOffline()) {
            viewFromCloud();
        } else {
            viewFromDB();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(CURRENT_PAGE, getCurrentPage());
        outState.putInt(COUNT_PAGE, getCountPage());
        super.onSaveInstanceState(outState);
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(FileUtils.removeType(file.getName()));
            getSupportActionBar().setElevation(10);
        }
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
                setPages(getCurrentPage(), getCountPage());
            }

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);

            }
        };
        mReaderDrawer.addDrawerListener(toggle);
        toggle.syncState();
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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
                    case R.id.reader_synchronize:
                        saveCurrentPage();
                        Utils.messageSnack(mReaderDrawer, getString(R.string.synchronize_complete));
                        break;
                    case R.id.reader_exit:
                        finish();
                        break;
                }
                return true;
            }
        });
    }

    private void startGoToPage() {
        GoToPageDialog goToPageDialog = new GoToPageDialog();
        goToPageDialog.setCancelable(false);
        goToPageDialog.show(getSupportFragmentManager(), GoToPageDialog.TAG);
    }

    private void fitWidth() {
        mPdfView.zoomTo((mPdfView.getWidth() - 1) / mPdfView.getOptimalPageWidth());
        mPdfView.jumpTo(getCurrentPage() - 1);
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
            case R.id.reader_synchronize:
                saveCurrentPage();
                Utils.messageSnack(mReaderDrawer, getString(R.string.synchronize_complete));
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.book_reader_drawer, menu);
        return true;
    }

    private void viewFromCloud() {
        workAPI.getLastPageFromCloud(new PageCallback() {
            @Override
            public void onSuccess(@NonNull Page.PageData page) {
                if (!pageUtilsDB.isBookCreate()) {
                    pageUtilsDB.createPageData(page.getLastPage(), page.getLastReadData());
                    viewPdf(page.getLastPage() - 1);
                } else {
                    if (Utils.convertStringToDate(page.getLastReadData()).getTime() > Utils.convertStringToDate(pageUtilsDB.readPageDate().getLastRead()).getTime()) {
                        pageUtilsDB.updateLastReadDate(page.getLastReadData());
                        pageUtilsDB.updatePage(page.getLastPage());
                        viewFromDB();
                    } else {
                        viewFromDB();
                    }
                }
            }

            @Override
            public void onError(@NonNull Throwable t) {

            }
        }, bookId);
    }

    private void viewFromDB() {
        if (!pageUtilsDB.isBookCreate())
            pageUtilsDB.createPageData(0, Utils.getCurrentTime());
        viewPdf(pageUtilsDB.readPageDate().getBookPage() - 1);
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveCurrentPage();
    }

    private void saveCurrentPage() {
        pageUtilsDB.updatePage(getCurrentPage());
        pageUtilsDB.updateLastReadDate(Utils.getCurrentTime());
        if (!preferenceUtils.readStatusOffline())
            workAPI.setLastPage(new StatusCallback() {
                @Override
                public void onSuccess(@NonNull int status) {

                }

                @Override
                public void onError(@NonNull Throwable t) {

                }
            }, bookId, getCurrentPage());
    }

    private int getCurrentPage() {
        return mPdfView.getCurrentPage() + 1;
    }

    private void viewPdf(final int currentPage) {
        mPdfView.fromFile(file)
                .onRender(new OnRenderListener() {
                    @Override
                    public void onInitiallyRendered(int nbPages, float pageWidth, float pageHeight) {
                        mPdfView.zoomTo((mPdfView.getWidth() - 1) / mPdfView.getOptimalPageWidth());
                        mPdfView.jumpTo(currentPage);
                        mPdfView.setBackgroundColor(getResources().getColor(R.color.screenLock));
                    }
                }).enableAntialiasing(true).spacing(10).load();
    }

    public static Intent startReaderActivity(Context context) {
        return new Intent(context, BookReaderActivity.class);
    }

    private void hideStatusBar(boolean view) {
        if (view) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    private void hideToolbar(boolean view) {
        if (getSupportActionBar() != null) {
            if (view) {
                getSupportActionBar().hide();
            } else {
                getSupportActionBar().show();
            }
        }
    }

    private void setBookInformation() {
        ImageView photo = (ImageView) mNavigationView.getHeaderView(0).findViewById(R.id.reader_book_photo);
        String url = PlamberAPI.ENDPOINT;
        String currentUrl = url.substring(0, url.length() - 1) + bookPhoto;
        Glide.with(getApplicationContext()).load(currentUrl).into(photo);
        TextView name = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.reader_book_name);
        name.setText(FileUtils.removeType(file.getName()));
        TextView author = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.reader_author_name);
        author.setText(bookAuthor);
    }

    private void setPages(int current, int all) {
        TextView currentText = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.reader_current_page);
        currentText.setText(String.valueOf(current));
        TextView allPageText = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.all_page);
        allPageText.setText(String.valueOf(all));
    }

    private void setHeaderBackground() {
        ImageView headerImage = (ImageView) mNavigationView.getHeaderView(0).findViewById(R.id.nav_reader_header_image);
        Glide.with(getApplicationContext()).load(R.drawable.main_background).apply(new RequestOptions().transform(new CenterCrop())).into(headerImage);
    }

    private SwitchCompat getSwitchHide(int menu) {
        return (SwitchCompat) mNavigationView.getMenu().findItem(menu).getActionView().findViewById(R.id.menu_switch);
    }

    public int getCountPage() {
        return mPdfView.getPageCount();
    }

    public void goToPage(int page) {
        mPdfView.jumpTo(page);
    }
}
