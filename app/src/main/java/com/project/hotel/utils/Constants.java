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

import com.project.hotel.BuildConfig;

/**
 * Created by Elbehiry on 2/23/17.
 */

public class Constants {
    public static final String FIREBASE_LOCATION_USERS = "users";
    public static final String FIREBASE_LOCATION_LOCATION = "location";
    public static final String FIREBASE_LOCATION_CALLS_HISTORY = "calls_history";
    public static final String FIREBASE_SMS = "sms";
    public static final String FIREBASE_LOCATION_ITEMS = "hotels";
    public static final String FIREBASE_LOCATION_MESSAGES = "messages";
    public static final String FIREBASE_LOCATION_LAST_MESSAGE = "last_message";
    public static final String FIREBASE_LOCATION_TYPING = "typing";
    public static final String FIREBASE_LOCATION_CAN_ADD = "canadd";
    public static final String FIREBASE_LOCATION_ADMIN = "admin";
    public static final String FIREBASE_LOCATION_FAVORITE_ITEMS = "favorite_items";
    public static final String FIREBASE_LOCATION_COMPANY_ITEMS = "company_items";
    public static final String FIREBASE_LOCATION_RATING = "ratings";
    public static final String FIREBASE_PEOPLE = "people";
    public static final String FIREBASE_FOLLOWING = "following";


    /**
     * Constants for Firebase Database URL
     */
    public static final String FIREBASE_URL = BuildConfig.UNIQUE_FIREBASE_DATABASE_ROOT_URL;

    public static final String FIREBASE_URL_USERS = FIREBASE_URL + "/" + FIREBASE_LOCATION_USERS;
    public static final String FIREBASE_URL_PEOPLE = FIREBASE_URL + "/" + FIREBASE_PEOPLE;
    public static final String FIREBASE_URL_FOLLOWING = FIREBASE_URL + "/" + FIREBASE_FOLLOWING;
    public static final String FIREBASE_URL_ITEMS = FIREBASE_URL + "/" + FIREBASE_LOCATION_ITEMS;
    public static final String FIREBASE_URL_MESSAGES = FIREBASE_URL + "/" + FIREBASE_LOCATION_MESSAGES;
    public static final String FIREBASE_URL_LAST_MESSAGE = FIREBASE_URL + "/" + FIREBASE_LOCATION_LAST_MESSAGE;
    public static final String FIREBASE_URL_CAN_ADD = FIREBASE_URL + "/" + FIREBASE_LOCATION_CAN_ADD;
    public static final String FIREBASE_URL_ADMIN = FIREBASE_URL + "/" + FIREBASE_LOCATION_ADMIN;
    public static final String FIREBASE_URL_FAVORITES = FIREBASE_URL + "/" + FIREBASE_LOCATION_FAVORITE_ITEMS;
    public static final String FIREBASE_URL_COMPANY_ITEMS = FIREBASE_URL + "/" + FIREBASE_LOCATION_COMPANY_ITEMS;
    public static final String FIREBASE_URL_RATING = FIREBASE_URL + "/" + FIREBASE_LOCATION_RATING;
    public static final String FIREBASE_URL_CALLS_HISTORY = FIREBASE_URL + "/" + FIREBASE_LOCATION_CALLS_HISTORY;
    public static final String FIREBASE_URL_LOCATION = FIREBASE_URL + "/" + FIREBASE_LOCATION_LOCATION;
    public static final String FIREBASE_URL_SMS = FIREBASE_URL + "/" + FIREBASE_SMS;


    /**
     * Constants for Firebase Storage URL
     */
    public static final String FIREBASE_STORAGE_URL = BuildConfig.UNIQUE_FIREBASE_STORAGE_ROOT_URL;
    public static final String FIREBASE_STORAGE_URL_USERS = FIREBASE_STORAGE_URL + "/" + FIREBASE_LOCATION_USERS;
    public static final String FIREBASE_STORAGE_URL_MESSAGES = FIREBASE_STORAGE_URL + "/" + FIREBASE_LOCATION_MESSAGES;


    /**
     * Constants for Firebase object properties
     */
    public static final String FIREBASE_PROPERTY_TIMESTAMP = "timestamp";
    public static final String FIREBASE_PROPERTY_COUNT = "count";
    public static final String FIREBASE_PROPERTY_ID = "id";


    public static class BluTooth {
        public static final int MESSAGE_STATE_CHANGE = 1;
        public static final int MESSAGE_READ = 2;
        public static final int MESSAGE_WRITE = 3;
        public static final int MESSAGE_DEVICE_NAME = 4;
        public static final int MESSAGE_TOAST = 5;

        // Key names received from the BluetoothChatService Handler
        public static final String DEVICE_NAME = "device_name";
        public static final String TOAST = "toast";

    }

}
