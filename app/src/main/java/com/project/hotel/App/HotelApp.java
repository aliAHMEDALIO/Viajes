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
package com.project.hotel.App;

import android.app.Application;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.firebase.database.FirebaseDatabase;
import com.project.hotel.utils.ImagePipelineConfigFactory;

/**
 * Created by Elbehiry on 6/7/17.
 */

public class HotelApp extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        //init firebase
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        Fresco.initialize(this, ImagePipelineConfigFactory.getImagePipelineConfig(this));
    }
}
