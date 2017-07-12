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


package com.project.hotel.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.project.hotel.R;
import com.project.hotel.hotels.HotelsFragment;
import com.project.hotel.model.User;
import com.project.hotel.utils.Utils;

/**
 * Created by Elbehiry on 7/1/17.
 */

public class ProfileFragment extends Fragment implements ProfileContractor.View{
    private SimpleDraweeView ProfileImage;
    private TextView userName;
    private static ProfileFragment Instance = null;
    public static ProfileFragment getInstance(){
        if (Instance == null){
            Instance = new ProfileFragment();
        }
        return Instance;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile,container,false);
        ProfileImage = (SimpleDraweeView) rootView.findViewById(R.id.Profile_picture);
        userName = (TextView) rootView.findViewById(R.id.name);
        ProfileImage.setImageURI(Utils.getPhoto());
        userName.setText(Utils.getName());

        return rootView;
    }


    @Override
    public void setPresenter(ProfileContractor.Presenter presenter) {


    }
}
