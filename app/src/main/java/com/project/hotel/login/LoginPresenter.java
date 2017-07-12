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

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.project.hotel.model.User;
import com.project.hotel.utils.Constants;

import java.util.HashMap;

/**
 * Created by Elbehiry on 2/23/17.
 */

public class LoginPresenter implements LoginContractor.Presenter, GoogleApiClient.OnConnectionFailedListener {

    public static final int RC_GOOGLE_LOGIN = 1;
    private FirebaseAuth mAuth;
    private GoogleApiClient mGoogleApiClientn;
    private Activity mContext;
    private boolean mIsParent = false;
    private String mPhone = "";


    @Nullable
    private final LoginContractor.View mLoginView;


    public LoginPresenter(@Nullable LoginContractor.View mview, Activity context){
        mLoginView = mview;
        mContext = context;
    }



    @Override
    public void login() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClientn);
        mContext.startActivityForResult(signInIntent, RC_GOOGLE_LOGIN);
    }

    @Override
    public void setGoogleApiClient(GoogleApiClient mGoogleApiClient) {
        mGoogleApiClientn = mGoogleApiClient;
    }


    @Override
    public void firebaseAuthWithGoogle(final GoogleSignInAccount account) {
        final AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(mContext, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        mLoginView.dismissProgress();
                        if (!task.isSuccessful()) {
                            mLoginView.showErrorMessage("Error In Create User");
                        } else {
                            FirebaseAuth.getInstance().getCurrentUser().linkWithCredential(credential);
                            String uid = task.getResult().getUser().getUid();
                            String name = account.getDisplayName();
                            String email = account.getEmail();
                            String imageProfileUrl = getHighQualityImage(account.getPhotoUrl());
                            createUserInFirebaseHelper(uid, new User(name, email, imageProfileUrl));
                            mLoginView.showMain();
                        }
                    }
                });
    }


    @Override
    public String getHighQualityImage(Uri puri) {
        Uri hq = new Uri.Builder().scheme(puri.getScheme()).authority(puri.getAuthority()).build();


        for (int i = 0; i < puri.getPathSegments().size(); i++) {
            if (i != 4)
                hq = hq.buildUpon().appendPath(puri.getPathSegments().get(i)).build();
            else
                hq = hq.buildUpon().appendPath("s400-c").build();
        }
        return hq.toString();
    }

    @Override
    public void createUserInFirebaseHelper(String uid, final User user) {
        final DatabaseReference userLocation =
                FirebaseDatabase.getInstance().getReferenceFromUrl(Constants.FIREBASE_URL_USERS).child(uid);

        /**
         * See if there is already a user (for example, if they already logged in with an associated
         * Google account.
         */
        userLocation.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                /* If there is no user, make one */
                if (dataSnapshot.getValue() == null) {
                 /* Set raw version of date to the ServerValue.TIMESTAMP value and save into dateCreatedMap */
                    HashMap<String, Object> timestampJoined = new HashMap<>();
                    timestampJoined.put(Constants.FIREBASE_PROPERTY_TIMESTAMP, ServerValue.TIMESTAMP);
                    user.setTimestampJoined(timestampJoined);
                    userLocation.setValue(user);
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("error",databaseError.getMessage());
                mLoginView.showUserCreateErrorMessage();

            }


        });
    }


    @Override
    public void start() {
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        mLoginView.showErrorMessage("Google Play Services error");
    }
}
