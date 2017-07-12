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

import android.support.annotation.Nullable;

import com.project.hotel.BasePresenter;
import com.project.hotel.BaseView;
import com.project.hotel.hotels.adapter.HotelDataAdapter;
import com.project.hotel.model.Hotel;

import java.util.List;

/**
 * Created by Elbehiry on 6/7/17.
 */

public class HotelContractor  {

    public interface View extends BaseView<Presenter>{
        void showHotels(List<Hotel> tasks);

        void showAddHotel();

        void showHotelDetailsUi(Hotel hotel);

        void showErrorMessage(@Nullable String message);

        void clearData();

    }


    public interface Presenter extends BasePresenter {

        void loadHotels();

        void onSearch(Hotel hotel);

        void onSearchName(String name);

        void addNewHotel();

        void setLiked(boolean liked,Hotel hotel);


    }

}
