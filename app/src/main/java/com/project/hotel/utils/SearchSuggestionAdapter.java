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

import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.project.hotel.R;

//search suggest adapter
public class SearchSuggestionAdapter extends CursorAdapter {
    public SearchSuggestionAdapter(Context context, Cursor cursor)
    {
        super(context, cursor, 0);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.search_suggestion_item, parent, false);
        return view;
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // reference
        LinearLayout root = (LinearLayout) view;
        TextView titleTextView = (TextView) root.findViewById(R.id.search_suggestion_item_title);

        // content
        final int index = cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1);
        titleTextView.setText(cursor.getString(index));
    }


    public void refill(Context context, Cursor cursor)
    {
        changeCursor(cursor);
    }
}
