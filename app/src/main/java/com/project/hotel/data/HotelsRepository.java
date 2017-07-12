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

package com.project.hotel.data;

import android.support.annotation.Nullable;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.hotel.model.Hotel;
import com.project.hotel.utils.Constants;
import com.project.hotel.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Elbehiry on 6/11/17.
 */

public class HotelsRepository implements HotelsDataSource {
    final List<Hotel> list = new ArrayList<>();

    private static HotelsRepository INSTANCE = null;
    private static DatabaseReference categoryRef,mFavoriteRef;

    public static HotelsRepository getInstance(){
        if(INSTANCE == null){
            INSTANCE = new HotelsRepository();
            categoryRef = FirebaseDatabase.getInstance().getReferenceFromUrl(Constants.FIREBASE_URL_ITEMS);
            categoryRef.keepSynced(true);
            mFavoriteRef = FirebaseDatabase.getInstance().getReferenceFromUrl(Constants.FIREBASE_URL_FAVORITES);
            mFavoriteRef.keepSynced(true);

        }
        return INSTANCE;

    }


    @Override
    public void getHotels(@Nullable final LoadHotelCallback callback) {
        list.clear();
        categoryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()){
                    callback.onDataNotAvailable();
                }
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    final Hotel item = ds.getValue(Hotel.class);
                    item.setItemId(ds.getKey());
                    list.add(item);
                }
                callback.onHotelsLoaded(list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onDataNotAvailable();

            }
        });
    }

    @Override
    public void saveHotel(@Nullable Hotel hotel) {

    }

    @Override
    public void seachName(@Nullable String name, @Nullable LoadHotelCallback callback) {
        if (list != null && list.size() >0){
            name = name.toLowerCase();
            ArrayList<Hotel> filteredList =
                    Lists.newArrayList(Collections2.filter(list, nameEqualsTo(name)));
            if (filteredList.size() > 0){
                callback.onHotelsLoaded(filteredList);
            }
            else {
                callback.onDataNotAvailable();
            }
        }

        else {
            callback.onDataNotAvailable();
        }
    }

    @Override
    public void searchData(@Nullable Hotel mHotel, @Nullable LoadHotelCallback callback) {
        if (list != null && list.size() >0) {

            ArrayList<Hotel> filteredList4 = Lists.newArrayList
                    (Collections2.filter(list, hotelEqualsTo(mHotel)));
            if (filteredList4.size() > 0){
                callback.onHotelsLoaded(filteredList4);
            }
            else {
                callback.onDataNotAvailable();
            }
        }
        else {
            callback.onDataNotAvailable();
        }
    }

    @Override
    public void setLiked(boolean liked, Hotel hotel) {
        if (liked){
            mFavoriteRef.child(Utils.getUid() + "/" + hotel.getItemId()).setValue(hotel);

        }
        else {
            mFavoriteRef.child(Utils.getUid() + "/" + hotel.getItemId()).removeValue();

        }
    }


    //guava
    private Predicate<Hotel> hotelEqualsTo
            (final Hotel mHotel) {
        return new Predicate<Hotel>() {
            public boolean apply(Hotel hotel) {
                try {
                    if (hotel.getPrice() < mHotel.getPrice() && hotel.isWifi() == mHotel.isWifi() && hotel.isPool() == mHotel.isPool()
                            && hotel.isSpa() == mHotel.isSpa() && hotel.isPets() == mHotel.isPets() && hotel.isGym() == mHotel.isGym() &&
                            hotel.isResturant() == mHotel.isResturant() && hotel.isBreakfast() == mHotel.isBreakfast()
                             && hotel.isBeach() == mHotel.isBeach()) {
                    return true;
                    }
                    else {
                        return false;
                    }
                }
                catch (Exception e) {
                    return false;
                }
            }
        };
    }

    //guava
    private Predicate<Hotel> nameEqualsTo(final String name) {
        return new Predicate<Hotel>() {

            public boolean apply(Hotel item) {

                return item.getName().toLowerCase().contains(name);
            }
        };
    }


}
