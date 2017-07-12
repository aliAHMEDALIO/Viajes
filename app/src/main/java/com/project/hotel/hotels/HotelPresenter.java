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


package com.project.hotel.hotels;

import android.content.Context;

import com.project.hotel.data.HotelsDataSource;
import com.project.hotel.data.HotelsRepository;
import com.project.hotel.main.MainActivity;
import com.project.hotel.model.Hotel;

import java.util.List;

/**
 * Created by Elbehiry on 6/7/17.
 */

public class HotelPresenter implements HotelContractor.Presenter, MainActivity.SearchInterface {
//    private static HotelPresenter Instance = null;
    private HotelsRepository mHotelsRepository;
    private HotelContractor.View mView;
    public static HotelPresenter getInstance (HotelContractor.View view,HotelsRepository hotelsRepository){
//        if (Instance == null){
//            Instance = new HotelPresenter(view,hotelsRepository);
//        }
        return new HotelPresenter(view,hotelsRepository);
    }

    private HotelPresenter(HotelContractor.View view,HotelsRepository hotelsRepository){
        mView = view;
        mHotelsRepository = hotelsRepository;
    }
    @Override
    public void start() {

    }

    @Override
    public void loadHotels() {
        mHotelsRepository.getHotels(new HotelsDataSource.LoadHotelCallback() {
            @Override
            public void onHotelsLoaded(List<Hotel> hotels) {
                mView.showHotels(hotels);
            }

            @Override
            public void onDataNotAvailable() {
                mView.clearData();
                mView.showErrorMessage("No Hotels");
            }
        });
    }

    @Override
    public void onSearch(Hotel hotel) {
        mHotelsRepository.searchData(hotel, new HotelsDataSource.LoadHotelCallback() {
            @Override
            public void onHotelsLoaded(List<Hotel> hotels) {
                mView.showHotels(hotels);
            }

            @Override
            public void onDataNotAvailable() {
                mView.clearData();
                mView.showErrorMessage("No Hotels");

            }
        });
    }

    @Override
    public void onSearchName(String name) {
        if (!name.isEmpty()){
            mHotelsRepository.seachName(name, new HotelsDataSource.LoadHotelCallback() {
                @Override
                public void onHotelsLoaded(List<Hotel> hotels) {
                    mView.showHotels(hotels);
                }

                @Override
                public void onDataNotAvailable() {
                    mView.clearData();
                    mView.showErrorMessage("No Hotels");

                }
            });
        }else {
            mView.showErrorMessage("Type Any Names");
        }
    }

    @Override
    public void addNewHotel() {

    }

    @Override
    public void setLiked(boolean liked, Hotel hotel) {
        //mHotelsRepository.setLiked(liked,hotel);
    }


}
