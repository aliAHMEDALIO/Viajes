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


package com.project.hotel.utils.slider;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;


/**
 * Created by Elbehiry on 16/11/16.
 */

public class FrescoSliderView extends DefaultSliderView {
    FrescoSliderView.SliderViewEvent action;

    public void setAction(FrescoSliderView.SliderViewEvent ac) {
        this.action = ac;
    }

    public FrescoSliderView(Context context) {
        super(context);
    }

    protected void bindEventAndShow(final View v, ImageView targetImageView) {
        View progressBar = v.findViewById(com.daimajia.slider.library.R.id.loading_bar);
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
        Log.d("targetImageView",getUrl());

        targetImageView.setImageURI(Uri.parse(getUrl()));
        targetImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                action.onSliderClick();
            }
        });


    }
    public interface SliderViewEvent{
        public void onSliderClick();
    }
}