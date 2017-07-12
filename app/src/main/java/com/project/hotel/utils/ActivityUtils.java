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


package com.project.hotel.utils;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;


import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Elbehiry on 2/23/17.
 */

public class ActivityUtils {
    public static String EXTRA_USER = "extra_user";

    public static void addFragmentToActivity(@Nullable FragmentManager manager
            , @Nullable Fragment fragment, int containerId) {
        checkNotNull(manager);
        checkNotNull(fragment);
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(containerId, fragment);
        transaction.commit();
    }


}
