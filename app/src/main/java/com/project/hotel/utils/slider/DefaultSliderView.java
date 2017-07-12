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

/**
 * Created by Elbehiry on 21/12/16.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.project.hotel.R;

public class DefaultSliderView extends BaseSliderView {
    public DefaultSliderView(Context context) {
        super(context);
    }

    @Override
    public View getView() {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.render_type_default,null);
        SimpleDraweeView target = (SimpleDraweeView)v.findViewById(R.id.daimajia_slider_image);
//        TextView description = (TextView)v.findViewById(R.id.description);
//        description.setText(getDescription());
        bindEventAndShow(v, target);

        return v;
    }



}