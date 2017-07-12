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


package com.project.hotel.login;

import android.net.Uri;
import android.support.annotation.Nullable;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.GoogleApiClient;
import com.project.hotel.BasePresenter;
import com.project.hotel.BaseView;
import com.project.hotel.model.User;

/**
 * Created by Elbehiry on 6/7/17.
 */

public interface LoginContractor {
    interface View extends BaseView<Presenter> {

        void showErrorMessage(String message);

        void showUserCreateErrorMessage();

        void showProgress();

        void dismissProgress();

        void showMain();

        boolean isActive(boolean active);



    }

    interface Presenter extends BasePresenter{

        void login();

        void setGoogleApiClient(GoogleApiClient mGoogleApiClient);

        void firebaseAuthWithGoogle(GoogleSignInAccount account);

        String getHighQualityImage(Uri puri);

        void createUserInFirebaseHelper(String uid, final User user);


    }
}
