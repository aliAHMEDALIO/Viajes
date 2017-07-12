/*

    Copyright 2017, The Android Open Source Project

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

    IF there any question ,Please contact me at :
    m.elbehiry44@gmail.com

*/


package com.project.hotel.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.Spanned;
import android.text.format.DateUtils;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import id.zelory.compressor.Compressor;

/**
 * Created by Elbehiry on 2/27/17.
 */

public final class Utils {
    public static File mFile;

    public static File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);

        return mFile = new File(storageDir, timeStamp);
    }

    /**
     * Format the date with SimpleDateFormat
     */
    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("dd-MM-yyy");
    public static final SimpleDateFormat SIMPLE_TIME_FORMAT = new SimpleDateFormat("hh:mm a");

    public static List<String> getCountriesList() {
        List<String> countriesList = new ArrayList<>();

        String[] locales = Locale.getISOCountries();

        for (String countryCode : locales) {

            Locale obj = new Locale("", countryCode);
            countriesList.add(obj.getDisplayCountry(Locale.ENGLISH));

        }
        Collections.sort(countriesList);
        return countriesList;
    }


    public static Uri getImageFileCompressedUri(Uri uri, int w, Context c) {
        String path = getPath(c.getContentResolver(), uri);
        File file = CompressImage(w, c, path);
        return Uri.fromFile(file);
    }

    public static String getPath(ContentResolver cr, Uri uri) {
        // just some safety built in
        if (uri == null) {
            // TODO perform some logging or show user feedback
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = cr.query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        // this is our fallback here
        return uri.getPath();
    }


    public static File CompressImage(int w, Context context, String path) {

        File f = new File(path);
        if(f.exists()) {




            Pair<Integer, Integer> p = getDimentions(/*mFile.getAbsolutePath()*/ path, w);
            return new Compressor.Builder(context)
                    .setMaxWidth(p.first)
                    .setMaxHeight(p.second)
                    .setQuality(75)
                    .setCompressFormat(Bitmap.CompressFormat.JPEG)
                    .build()
                    .compressToFile(/*ImageConverter.mFile*/new File(path));

        }
        else return null;
    }


    public static Pair<Integer, Integer> getDimentions(String path, float destWidth) {
        // read in the dimensions of the image on disk
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;
        float destHeight;
        if (destWidth > (int) Math.min(srcHeight, srcHeight))
            return new Pair<>(new Integer((int) srcWidth), new Integer((int) srcHeight));


        if (srcWidth < srcHeight) {
            destHeight = (destWidth / srcWidth) * srcHeight;
        } else {
            destHeight = (destWidth / srcHeight) * -srcWidth;
        }


        if (destHeight >= 0)
            return new Pair<>(new Integer((int) destWidth), new Integer((int) destHeight));
        else
            return new Pair<>(new Integer((int) -destHeight), new Integer((int) destWidth));
    }

    // list view


    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public static String getUid() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            return user.getUid();
            // User is signed in
        } else {
            return null;
            // No user is signed in
        }
    }
    public static String getName() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            return user.getDisplayName();
            // User is signed in
        } else {
            return null;
            // No user is signed in
        }
    }

 public static Uri getPhoto() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            return user.getPhotoUrl();
            // User is signed in
        } else {
            return null;
            // No user is signed in
        }
    }


    public static String getRoomName(String uid1, String uid2) {
        if (uid1.compareTo(uid2) < 0) {
            return uid1 + uid2;
        } else {
            return uid2 + uid1;
        }
    }


    private String getExtension(@NonNull File file) {
        String path = file.getPath();
        int i = path.lastIndexOf(".");
        if (i < 0) {
            return null;
        } else {
            return path.substring(i);
        }
    }

    public static String getRelativeDate(long date) {
/*
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date);

        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(System.currentTimeMillis());

        if (cal.get(Calendar.YEAR) == now.get(Calendar.YEAR)) {
            if (cal.get(Calendar.DAY_OF_YEAR) == now.get(Calendar.DAY_OF_YEAR)) {
                return SIMPLE_TIME_FORMAT.format(date).replace(".", "");
            }else if(cal.get(Calendar.DAY_OF_YEAR) == now.get(Calendar.DAY_OF_YEAR)-1){

            }
        }
        cal.get(Calendar.DAY_OF_MONTH);*/
        long now = System.currentTimeMillis();
        return DateUtils.getRelativeTimeSpanString(date, now, DateUtils.DAY_IN_MILLIS).toString();
    }


    public static String getRelativeTime(long date) {
        String str = getRelativeDate(date);
        if (str.equals("Today")) return SIMPLE_TIME_FORMAT.format(date).replace(".", "");
        else
            return str;
    }

    public static ImageRequest getImageRequest(Uri uri) {
        ImageRequest request = ImageRequestBuilder
                .newBuilderWithSource(uri)
                .setLocalThumbnailPreviewsEnabled(true)
                .setLowestPermittedRequestLevel(ImageRequest.RequestLevel.FULL_FETCH)
                .setProgressiveRenderingEnabled(false)
                .build();
        return request;
    }


    public static Spanned getSpannedString(String str, String sub) {
        String lowerCase = str.toLowerCase();
        String stag = "<font color='#2196F3'>", etag = "</font>";

        int atIndx = 0;
        int ind = -1;
        int curInc = 0;
        while ((ind = lowerCase.indexOf(sub, atIndx)) != -1) {
            Log.d("Utils", "getSpannableString: =" + ind);
            str = str.substring(0, ind + curInc)
                    + stag
                    + str.substring(ind + curInc, ind + curInc + sub.length())
                    + etag
                    + str.substring(ind + curInc + sub.length(), str.length());

            atIndx = ind + sub.length();
            curInc += stag.length() + etag.length();
        }

        return Html.fromHtml(str);
    }
}
