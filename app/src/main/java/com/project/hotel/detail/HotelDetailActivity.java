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


package com.project.hotel.detail;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.project.hotel.R;
import com.project.hotel.data.HotelsRepository;
import com.project.hotel.hotels.HotelPresenter;
import com.project.hotel.hotels.HotelsFragment;
import com.project.hotel.utils.ActivityUtils;

public class HotelDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_detail);
        Bundle data = getIntent().getExtras();

//        setupActionBar();
        DetailFragment detailFragment = DetailFragment.getInstance();
        detailFragment.setArguments(data);
        DetailPresenter hotelPresenter = DetailPresenter.getInstance(detailFragment);
        if (detailFragment != null) {
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), detailFragment, R.id.contentFrame);
            detailFragment.setPresenter(hotelPresenter);
        }
    }
//    private void setupActionBar() {
//        Toolbar toolbar = (Toolbar) findViewById(R.id.avatar_toolbar);
//        setSupportActionBar(toolbar);
//        ActionBar bar = getSupportActionBar();
//        bar.setDisplayUseLogoEnabled(false);
//        bar.setDisplayShowTitleEnabled(true);
//        bar.setDisplayShowHomeEnabled(true);
//        bar.setDisplayHomeAsUpEnabled(true);
//        bar.setHomeButtonEnabled(true);
//    }
}
