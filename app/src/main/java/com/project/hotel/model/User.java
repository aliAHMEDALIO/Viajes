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


package com.project.hotel.model;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Elbehiry on 6/7/17.
 */

public class User implements Serializable {
    private String name;
    private String nameCaseIgnore;
    private String email;
    private String profileImageUrl;
    private HashMap<String, Object> timestampJoined;
    private String uid;
    private String country;




    /**
     * Required public constructor
     */
    public User() {
    }

    /**
     * Use this constructor to create new User.
     * Takes user name, email and profileImageUrl as params
     *
     * @param name
     * @param email
     * @param profileImageUrl
     */
    public User(String name, String email, String profileImageUrl) {
        this.name = name;
        //name == null in login
        if(name != null)
            nameCaseIgnore = name.toLowerCase();
        this.email = email;
        this.profileImageUrl = profileImageUrl;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Exclude
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNameCaseIgnore() {
        return nameCaseIgnore;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setName(String name) {
        this.name = name;
        nameCaseIgnore = name.toLowerCase();
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public void setTimestampJoined(HashMap<String, Object> timestampJoined) {
        this.timestampJoined = timestampJoined;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public HashMap<String, Object> getTimestampJoined() {
        return timestampJoined;
    }


}