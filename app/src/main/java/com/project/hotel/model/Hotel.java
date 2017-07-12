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

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.Exclude;
import com.google.maps.android.clustering.ClusterItem;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Elbehiry on 6/11/17.
 */

public class Hotel implements Serializable, ClusterItem {
    private String name;
    private HashMap<String, String> imagesUrls = new HashMap<>();
    private long price;
    private String uid;
    private Object time;
    private long rate;
    private double lat = 0.0;
    private double lon = 0.0;
    private boolean wifi;
    private boolean pool;
    private boolean spa;
    private boolean pets;
    private boolean resturant;
    private boolean gym;
    private boolean breakfast;
    private boolean beach;
    private String website;
    private String phone;

    @Exclude
    private boolean isFavorite=false;


    public Hotel() {
    }


    @Exclude
    public boolean isFavorite() {
        return isFavorite;
    }

    @Exclude
    public void setIsFavorite(boolean isfavorite) {
        this.isFavorite = isfavorite;
    }
    @Exclude
    public String getItemId() {
        return uid;
    }

    @Exclude
    public void setItemId(String itemId) {
        this.uid = itemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<String, String> getImagesUrls() {
        return imagesUrls;
    }

    public void setImagesUrls(HashMap<String, String> imagesUrls) {
        this.imagesUrls = imagesUrls;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Object getTime() {
        return time;
    }

    public void setTime(Object time) {
        this.time = time;
    }

    public long getRate() {
        return rate;
    }

    public void setRate(long rate) {
        this.rate = rate;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public boolean isWifi() {
        return wifi;
    }

    public void setWifi(boolean wifi) {
        this.wifi = wifi;
    }

    public boolean isPool() {
        return pool;
    }

    public void setPool(boolean pool) {
        this.pool = pool;
    }

    public boolean isSpa() {
        return spa;
    }

    public void setSpa(boolean spa) {
        this.spa = spa;
    }

    public boolean isPets() {
        return pets;
    }

    public void setPets(boolean pets) {
        this.pets = pets;
    }

    public boolean isResturant() {
        return resturant;
    }

    public void setResturant(boolean resturant) {
        this.resturant = resturant;
    }

    public boolean isGym() {
        return gym;
    }

    public void setGym(boolean gym) {
        this.gym = gym;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isBeach() {
        return beach;
    }

    public boolean isBreakfast() {
        return breakfast;
    }

    public void setBreakfast(boolean breakfast) {
        this.breakfast = breakfast;
    }

    public void setBeach(boolean beach) {
        this.beach = beach;
    }

    @Override
    public LatLng getPosition() {
        return null;
    }
}
