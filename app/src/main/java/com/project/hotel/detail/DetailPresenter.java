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

import android.net.Uri;
import android.support.annotation.Nullable;

import com.project.hotel.data.HotelsRepository;
import com.project.hotel.hotels.HotelContractor;
import com.project.hotel.hotels.HotelPresenter;
import com.project.hotel.model.Hotel;

import java.util.HashMap;

/**
 * Created by Elbehiry on 6/27/17.
 */

public class DetailPresenter implements DetailContractor.Presenter {
    private static DetailPresenter Instance = null;
    private DetailContractor.View mView;
    private Hotel hotel;

    public static DetailPresenter getInstance (DetailContractor.View view){
        if (Instance == null){
            Instance = new DetailPresenter(view);
        }
        return Instance;
    }

    private DetailPresenter(DetailContractor.View view){
        mView = view;
    }
    @Override
    public void start() {

    }

    @Override
    public void handleData(@Nullable Hotel hotel) {
        HashMap<String, String> file_maps = hotel.getImagesUrls();
        mView.showHotelImages(file_maps);
        mView.setHotelName(hotel.getName());
        mView.setHotelInformation(hotel.getPrice(),hotel.getWebsite(),hotel.getPhone());
        mView.setHotelExtra(hotel.isWifi(),hotel.isPool(),hotel.isBeach(),hotel.isBreakfast(),hotel.isSpa(),hotel.isPets(),hotel.isGym(),hotel.isResturant());

    }
}
