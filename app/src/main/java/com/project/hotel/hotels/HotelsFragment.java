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

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.project.hotel.R;
import com.project.hotel.detail.HotelDetailActivity;
import com.project.hotel.hotels.adapter.HotelDataAdapter;
import com.project.hotel.model.Hotel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elbehiry on 6/7/17.
 */

public class HotelsFragment extends Fragment implements HotelContractor.View,HotelDataAdapter.Action{
    private static HotelsFragment Instance = null;
    private RecyclerView mHotelsList;
    private HotelDataAdapter mAdapter;
    private ArrayList<Hotel> hotels;
    private HotelContractor.Presenter mPresenter;
    public static HotelsFragment getInstance(){
        if (Instance == null){
            Instance = new HotelsFragment();
        }
        return Instance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mRootView = inflater.inflate(R.layout.fragment_hotels,container,false);
        hotels = new ArrayList<>();

        mHotelsList = (RecyclerView) mRootView.findViewById(R.id.hotels_recycler);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mHotelsList.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        }
        else {
            mHotelsList.setLayoutManager(new GridLayoutManager(getActivity(), 1));

        }
        mAdapter = new HotelDataAdapter(getActivity(),hotels);
        mAdapter.setActionListner(this);
        mHotelsList.setAdapter(mAdapter);

        if (mPresenter != null)
        mPresenter.loadHotels();


        return mRootView;
    }

    @Override
    public void setPresenter(HotelContractor.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showHotels(List<Hotel> tasks) {
        hotels.clear();
        hotels.addAll(tasks);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showAddHotel() {

    }

    @Override
    public void showHotelDetailsUi(Hotel hotel) {

    }

    @Override
    public void showErrorMessage(@Nullable String message) {
        if (isAdded())
        Toast.makeText(getContext(),""+message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void clearData() {
        hotels.clear();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItem(Hotel item) {

        Intent i = new Intent(getActivity(), HotelDetailActivity.class);
        i.putExtra("data",item);
        startActivity(i);
    }

    @Override
    public void onFavourite(boolean liked, int position) {

        Hotel hotel = hotels.get(position);
        hotel.setIsFavorite(liked);

        if (liked) {
            mPresenter.setLiked(true,hotel);

        } else {
            mPresenter.setLiked(false,hotel);

        }
        mAdapter.notifyDataSetChanged();


    }
}
