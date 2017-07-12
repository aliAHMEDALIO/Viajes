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

import com.project.hotel.BasePresenter;
import com.project.hotel.BaseView;
import com.project.hotel.hotels.HotelContractor;
import com.project.hotel.model.Hotel;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Elbehiry on 6/27/17.
 */

public class DetailContractor {
    interface View extends BaseView<DetailContractor.Presenter> {


        void showHotelImages(HashMap<String, String> file_maps);

        void setHotelName(@Nullable String name);

        void setHotelInformation(@Nullable long price,@Nullable String website,@Nullable String phone);

        void setHotelExtra(@Nullable boolean wifi,@Nullable boolean pool,@Nullable boolean beach,
        @Nullable boolean breakfast,@Nullable boolean spa,@Nullable boolean pets,@Nullable boolean gym,@Nullable boolean restuarnt);

    }


    interface Presenter extends BasePresenter {


        void handleData(@Nullable Hotel hotel);



    }
}
