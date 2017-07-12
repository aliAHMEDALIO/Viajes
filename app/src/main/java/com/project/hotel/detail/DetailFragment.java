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
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.facebook.drawee.view.SimpleDraweeView;
import com.project.hotel.R;
import com.project.hotel.hotels.HotelPresenter;
import com.project.hotel.hotels.HotelsFragment;
import com.project.hotel.main.MainActivity;
import com.project.hotel.model.Hotel;
import com.project.hotel.utils.slider.FrescoSliderView;
import com.project.hotel.utils.slider.SliderLayout;

import java.util.HashMap;

/**
 * Created by Elbehiry on 6/26/17.
 */

public class DetailFragment extends Fragment implements DetailContractor.View, FrescoSliderView.SliderViewEvent, ViewPagerEx.OnPageChangeListener {
    private static DetailFragment Instance = null;
    private Bundle data_bun;
    private Hotel data;
    private SliderLayout mDemoSlider;
    private TextView hotelName, hotelPrice, hotelWebsite, location;
    private ImageView wifi, pool, beach, breakfast, spa, pets, gym, restaurant,back;

    private DetailContractor.Presenter mPresenter;

    public static DetailFragment getInstance() {
        if (Instance == null) {
            Instance = new DetailFragment();
        }
        return Instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data_bun = getArguments();
        if (data_bun != null) {
            data = (Hotel) data_bun.getSerializable("data");
        }

    }

    @Override
    public void setPresenter(DetailContractor.Presenter presenter) {

        //init presenter
        mPresenter = presenter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mRootView = inflater.inflate(R.layout.fragment_detail, container, false);
        mDemoSlider = (SliderLayout) mRootView.findViewById(R.id.slider);
        hotelName = (TextView) mRootView.findViewById(R.id.title_name);
        hotelPrice = (TextView) mRootView.findViewById(R.id.item_price);
        hotelWebsite = (TextView) mRootView.findViewById(R.id.item_website);
        location = (TextView) mRootView.findViewById(R.id.location);
        wifi = (ImageView) mRootView.findViewById(R.id.wifi);
        pool = (ImageView) mRootView.findViewById(R.id.pool);
        beach = (ImageView) mRootView.findViewById(R.id.beach);
        breakfast = (ImageView) mRootView.findViewById(R.id.breakfast);
        spa = (ImageView) mRootView.findViewById(R.id.spa);
        pets = (ImageView) mRootView.findViewById(R.id.pet);
        gym = (ImageView) mRootView.findViewById(R.id.gym);
        restaurant = (ImageView) mRootView.findViewById(R.id.restaurant);
        back = (ImageView) mRootView.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });


        if (data != null) {
            mPresenter.handleData(data);
        }


        return mRootView;
    }

    @Override
    public void showHotelImages(HashMap<String, String> file_maps) {
        for (String name : file_maps.keySet()) {
            FrescoSliderView textSliderView = new FrescoSliderView(getActivity());
            textSliderView
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.CenterCrop);
            textSliderView.bundle(new Bundle());
            textSliderView.setAction(DetailFragment.this);
            mDemoSlider.addSlider(textSliderView);
        }
//        mDemoSlider.stopAutoCycle();
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.addOnPageChangeListener(this);
    }

    @Override
    public void setHotelName(@Nullable String name) {
        hotelName.setText("" + name);
    }

    @Override
    public void setHotelInformation(@Nullable long price, @Nullable String website, @Nullable String phone) {

        hotelPrice.setText("" + price);
        hotelWebsite.setText("" + website);
        location.setText("Egypt");
    }

    @Override
    public void setHotelExtra(@Nullable boolean mwifi, @Nullable boolean mpool, @Nullable boolean mbeach, @Nullable boolean mbreakfast, @Nullable boolean mspa, @Nullable boolean mpets, @Nullable boolean mgym, @Nullable boolean mrestuarnt) {

        if (mwifi) {
            if (Build.VERSION.SDK_INT > 16) {
                wifi.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.nav_filter_back_click));
            } else {
                wifi.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.nav_filter_back_click));

            }
            wifi.setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorAccent));

        } else {
            if (Build.VERSION.SDK_INT > 16) {
                wifi.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.nav_filter_back));
            } else {
                wifi.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.nav_filter_back));

            }
            wifi.setColorFilter(ContextCompat.getColor(getActivity(), R.color.trv_juri));

        }
        if (mpool) {
            if (Build.VERSION.SDK_INT > 16) {
                pool.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.nav_filter_back_click));
            } else {
                pool.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.nav_filter_back_click));

            }
            pool.setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorAccent));

        } else {
            if (Build.VERSION.SDK_INT > 16) {
                pool.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.nav_filter_back));
            } else {
                pool.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.nav_filter_back));

            }
            pool.setColorFilter(ContextCompat.getColor(getActivity(), R.color.trv_juri));

        }
        if (mbeach) {
            if (Build.VERSION.SDK_INT > 16) {
                beach.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.nav_filter_back_click));
            } else {
                beach.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.nav_filter_back_click));

            }
            beach.setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorAccent));

        } else {
            if (Build.VERSION.SDK_INT > 16) {
                beach.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.nav_filter_back));
            } else {
                beach.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.nav_filter_back));

            }
            beach.setColorFilter(ContextCompat.getColor(getActivity(), R.color.trv_juri));

        }
        if (mbreakfast) {
            if (Build.VERSION.SDK_INT > 16) {
                breakfast.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.nav_filter_back_click));
            } else {
                breakfast.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.nav_filter_back_click));

            }
            breakfast.setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorAccent));

        } else {
            if (Build.VERSION.SDK_INT > 16) {
                breakfast.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.nav_filter_back));
            } else {
                breakfast.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.nav_filter_back));

            }
            breakfast.setColorFilter(ContextCompat.getColor(getActivity(), R.color.trv_juri));

        }
        if (mspa) {
            if (Build.VERSION.SDK_INT > 16) {
                spa.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.nav_filter_back_click));
            } else {
                spa.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.nav_filter_back_click));

            }
            spa.setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorAccent));

        } else {
            if (Build.VERSION.SDK_INT > 16) {
                spa.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.nav_filter_back));
            } else {
                spa.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.nav_filter_back));

            }
            spa.setColorFilter(ContextCompat.getColor(getActivity(), R.color.trv_juri));

        }
        if (mpets) {
            if (Build.VERSION.SDK_INT > 16) {
                pets.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.nav_filter_back_click));
            } else {
                pets.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.nav_filter_back_click));

            }
            pets.setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorAccent));

        } else {
            if (Build.VERSION.SDK_INT > 16) {
                pets.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.nav_filter_back));
            } else {
                pets.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.nav_filter_back));

            }
            pets.setColorFilter(ContextCompat.getColor(getActivity(), R.color.trv_juri));

        }
        if (mgym) {
            if (Build.VERSION.SDK_INT > 16) {
                gym.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.nav_filter_back_click));
            } else {
                gym.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.nav_filter_back_click));

            }
            gym.setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorAccent));

        } else {
            if (Build.VERSION.SDK_INT > 16) {
                gym.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.nav_filter_back));
            } else {
                gym.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.nav_filter_back));

            }
            gym.setColorFilter(ContextCompat.getColor(getActivity(), R.color.trv_juri));

        }
        if (mrestuarnt) {
            if (Build.VERSION.SDK_INT > 16) {
                restaurant.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.nav_filter_back_click));
            } else {
                restaurant.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.nav_filter_back_click));

            }
            restaurant.setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorAccent));

        } else {
            if (Build.VERSION.SDK_INT > 16) {
                restaurant.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.nav_filter_back));
            } else {
                restaurant.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.nav_filter_back));

            }
            restaurant.setColorFilter(ContextCompat.getColor(getActivity(), R.color.trv_juri));

        }

    }



    @Override
    public void onSliderClick() {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
