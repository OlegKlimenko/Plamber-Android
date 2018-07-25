package com.ua.plamber_android.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.shockwave.pdfium.PdfDocument;
import com.shockwave.pdfium.PdfiumCore;
import com.ua.plamber_android.R;
import com.ua.plamber_android.database.model.LocalBookDB;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

public class Utils {

    private Context context;
    private static final String TAG = "Utils";

    public Utils(Context context) {
        this.context = context;
    }

    public void initBackgroundImage(ImageView background) {
        Glide.with(context).load(R.drawable.main_background)
                .apply(new RequestOptions().transform(new CenterCrop())).into(background);
    }

    public String getStoragePdfPath() {
            File file = new File(context.getFilesDir() + File.separator + "pdf");
            if (!file.exists())
                file.mkdir();
        return file.getAbsolutePath() + File.separator;

    }

    public static String generateId() {
        return UUID.randomUUID().toString();
    }

    public String getStorageImagesPath() {
        File file = new File(context.getFilesDir() + File.separator + "images");
        if (!file.exists())
            file.mkdir();
        return file.getAbsolutePath() + File.separator;

    }

    public void deleteAllPdfFiles() {
        File[] allFiles = new File(getStoragePdfPath()).listFiles();
        for (File file : allFiles) {
            file.delete();
        }
    }

    public void deleteAllImageFiles() {
        File[] allFiles = new File(getStorageImagesPath()).listFiles();
        for (File file : allFiles) {
            file.delete();
        }
    }

    public File getUsersDirectory() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return Environment.getExternalStorageDirectory();
        } else {
            return null;
        }
    }

    public String getPdfFileWithPath(String bookId) {
        return getStoragePdfPath() + getNameWithPDF(bookId);
    }

    public String getPngFileWithPath(String bookId) {
        return getStorageImagesPath() + getNameWithPNG(bookId);
    }

    public void removeImage(String imageName) {
        File file = new File(getPngFileWithPath(imageName));
        if (file.exists())
            file.delete();
    }

    public String getNameWithPDF(String bookId) {
        return bookId + ".pdf";
    }

    public String getNameWithPNG(String bookId) {
        return bookId + ".png";
    }

    public float getHeightDeviceDP() {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.heightPixels / displayMetrics.density;
    }

    public float getWidthDeviceDP() {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.widthPixels / displayMetrics.density;
    }

    public static void messageSnack(View view, String mes) {
        final Snackbar snackbar = Snackbar.make(view, mes, Snackbar.LENGTH_LONG);
        snackbar.setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    public static String dateUpload(String date) {
        if (date != null) {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            DateFormat plamberFormat = new SimpleDateFormat("dd.MM.yy", Locale.US);
            Date newDate = null;
            try {
                newDate = format.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return plamberFormat.format(newDate);
        } else {
            return null;
        }
    }

    public static void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager)view.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public byte[] getFirstPageAsByte(File file) {
        PdfiumCore pdfiumCore = null;
        PdfDocument pdfDocument = null;
        try {
            ParcelFileDescriptor fd = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
            pdfiumCore = new PdfiumCore(context);
            pdfDocument = pdfiumCore.newDocument(fd);
        } catch (IOException e) {
            Log.i(TAG, e.getLocalizedMessage());
        }
        pdfiumCore.openPage(pdfDocument, 0);

        int width = pdfiumCore.getPageWidthPoint(pdfDocument, 0);
        int height = pdfiumCore.getPageHeightPoint(pdfDocument, 0);

        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.RGB_565);
        pdfiumCore.renderPageBitmap(pdfDocument, bitmap, 0, 0, 0,
                width, height);
        pdfiumCore.closeDocument(pdfDocument);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    public static String getTimeMillis() {
        return String.valueOf(System.currentTimeMillis());
    }
}
