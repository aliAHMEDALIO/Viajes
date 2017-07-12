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


package com.project.hotel.hoteladd;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appyvet.rangebar.RangeBar;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nguyenhoanglam.imagepicker.activity.ImagePicker;
import com.nguyenhoanglam.imagepicker.activity.ImagePickerActivity;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.project.hotel.R;
import com.project.hotel.main.MainActivity;
import com.project.hotel.model.Hotel;
import com.project.hotel.model.User;
import com.project.hotel.utils.Constants;
import com.project.hotel.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddActivity extends AppCompatActivity implements View.OnClickListener, AddImageAdapter.FileAction {
    private static final String TAG = AddActivity.class.getSimpleName();
    private int PLACE_PICKER_REQUEST = 1;
    private final static int READ_PERMISSION_REQUEST = 123;
    private int REQUEST_CODE_PICKER = 3;
    private EditText name,phone,website;
    private ImageView add_image,get_location;
    private RecyclerView images_recycler;
    private TextView location_name;
    private ImageView wifi,pool,beach,breakfast,spa,pets,gym,restaurant;
    private RangeBar range;
    private boolean wifi_click,pool_click,beach_click,breakfast_click,spa_click,pets_click,gym_click,restaurant_click;
    private Button Add;
    private int price = 10;
    private int Image_Limit = 4;
    private double latitude = 0.0;
    private double longitude = 0.0;
    private ArrayList<Image> uploadedImages = new ArrayList<>();
    private HashMap<String, String> mUploadedImages = new HashMap<>();

    private ArrayList<Image> images;
    private AddImageAdapter mAdapter;
    private StorageReference mStorageRef;
    private User mUser;
    private ProgressDialog mUploadProgressDialog;
    private String Uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private CardView add_card;


    private void getUserData() {
        String uid = Utils.getUid();
        FirebaseDatabase.getInstance().getReferenceFromUrl(Constants.FIREBASE_URL_USERS)
                .child(uid).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        mUser = dataSnapshot.getValue(User.class);
                        mUser.setUid(dataSnapshot.getKey());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("MainActivity", "getUser:onCancelled", databaseError.toException());
                        // ...
                    }
                }

        );
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            writePermission();

    }
    @TargetApi(Build.VERSION_CODES.M)
    private void writePermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "External Storage READ Required to send photo", Toast.LENGTH_SHORT).show();
            }

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE

            }, READ_PERMISSION_REQUEST);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == READ_PERMISSION_REQUEST) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                Toast.makeText(this, "App can't save photo without Media access Permission", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(READ_PERMISSION_REQUEST, permissions, grantResults);
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        images = new ArrayList<Image>();

        Add = (Button) findViewById(R.id.btnAddItem);
        name = (EditText) findViewById(R.id.hotel_name);
        phone = (EditText) findViewById(R.id.hotel_phone);
        website = (EditText) findViewById(R.id.hotel_website);

        add_image = (ImageView) findViewById(R.id.add_item_image);
        get_location = (ImageView) findViewById(R.id.get_location);

        location_name = (TextView) findViewById(R.id.location_name);
        wifi = (ImageView) findViewById(R.id.wifi);
        pool = (ImageView) findViewById(R.id.pool);
        beach = (ImageView) findViewById(R.id.beach);
        breakfast = (ImageView) findViewById(R.id.breakfast);
        spa = (ImageView) findViewById(R.id.spa);
        pets = (ImageView) findViewById(R.id.pet);
        gym = (ImageView) findViewById(R.id.gym);
        restaurant = (ImageView) findViewById(R.id.restaurant);
        wifi.setOnClickListener(this);
        pool.setOnClickListener(this);
        beach.setOnClickListener(this);
        breakfast.setOnClickListener(this);
        spa.setOnClickListener(this);
        pets.setOnClickListener(this);
        gym.setOnClickListener(this);
        restaurant.setOnClickListener(this);

        add_card = (CardView) findViewById(R.id.card_img);


        range = (RangeBar) findViewById(R.id.rangebar);
        range.setSeekPinByValue(10);
        range.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {

                try {
                    price = Integer.parseInt(rightPinValue);
                }catch (NumberFormatException ex){

                }
            }
        });

        images_recycler = (RecyclerView) findViewById(R.id.addimage_recycler_view);
        images_recycler.setHasFixedSize(true);
        mAdapter = new AddImageAdapter(AddActivity.this, images);
        mAdapter.setActionListner(AddActivity.this);
        images_recycler.setAdapter(mAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AddActivity.this);
        linearLayoutManager.setOrientation(LinearLayout.HORIZONTAL);
        images_recycler.setLayoutManager(linearLayoutManager);
        images_recycler.setItemAnimator(new DefaultItemAnimator());
        mAdapter.notifyDataSetChanged();



        add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.create(AddActivity.this)
                        .folderMode(true) // folder mode (false by default)
                        .folderTitle("Select Images") // folder selection title
                        .imageTitle("Tap to select") // image selection title
                        .single() // single mode
                        .multi() // multi mode (default mode)
                        .limit(Image_Limit) // max images can be selected (99 by default)
                        .showCamera(false) // show camera or not (true by default)
                        .imageDirectory("Camera") // directory name for captured image  ("Camera" folder by default)
                        .origin(images) // original selected images, used in multi mode
                        .start(REQUEST_CODE_PICKER); // start image picker activity with request code
            }
        });


        get_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
                    if (latitude != 0.0 || longitude != 0.0) {
                        intentBuilder.setLatLngBounds(new LatLngBounds(new LatLng(latitude, longitude)
                                , new LatLng(latitude, longitude)));
                    }
                    startActivityForResult(intentBuilder.build(AddActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }

            }
        });

//        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        mStorageRef = storage.getReferenceFromUrl(Constants.FIREBASE_STORAGE_URL_USERS + "/" + Uid);
        getUserData();

        mUploadProgressDialog = new ProgressDialog(this);
        mUploadProgressDialog.setTitle(getResources().getString(R.string.progress_dialog_loading));
        mUploadProgressDialog.setMessage(getResources().getString(R.string.progress_dialog_authenticating_with_firebase));
        mUploadProgressDialog.setCancelable(false);


        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Hotel_Name = name.getText().toString();
                String Hotel_Phone = phone.getText().toString();
                String Hotel_Website = website.getText().toString();

                if(!Hotel_Name.isEmpty() && !Hotel_Phone.isEmpty() && !Hotel_Website.isEmpty()){

                    mUploadProgressDialog.show();
                    new ImageCompressor().execute(images);

                }
                else {
                    Toast.makeText(AddActivity.this, "Please Complete Basic Information", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == REQUEST_CODE_PICKER && resultCode == RESULT_OK && data != null) {
                ArrayList<Image> dat = data.getParcelableArrayListExtra(ImagePickerActivity.INTENT_EXTRA_SELECTED_IMAGES);
                renderViewImage(dat);
            }
            else if (requestCode == PLACE_PICKER_REQUEST) {
                if (resultCode == RESULT_OK) {
                    Place place = PlacePicker.getPlace(AddActivity.this, data);
                    String placename = String.format("%s", place.getName());
                    latitude = place.getLatLng().latitude;
                    longitude = place.getLatLng().longitude;
                    location_name.setText("" + placename);
                    Toast.makeText(AddActivity.this, "You Select: " + placename, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "You Haven't Select Any Places", Toast.LENGTH_LONG).show();

                }
            }


        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
            Log.d("Additem_Activity", "onActivityResult: " + e);
        }


    }

    @Override
    public void onRemoveClick(int position) {
        long id = images.get(position).getId();
        images.remove(position);
        if (images.size() == Image_Limit) {
            add_card.setVisibility(View.GONE);
        } else {
            add_card.setVisibility(View.VISIBLE);

        }
        for (int i = 0; i < uploadedImages.size(); i++) {
            if (uploadedImages.get(i).getId() == id) {
                uploadedImages.remove(i);
                break;
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    private class ImageCompressor extends AsyncTask<List<Image>, Void, List<Uri>> {

        @Override
        protected List<Uri> doInBackground(List<Image>... lists) {
            List<Image> images = lists[0];
            List<Uri> imagesUris = new ArrayList<>();
            for (int i = 0; i < images.size(); i++) {

                if (!images.get(i).getPath().contains("firebasestorage")) {
                    File file = Utils.CompressImage(620, getBaseContext(), images.get(i).getPath());
                    imagesUris.add(Uri.fromFile(file));
                } else {
                    images.remove(i--);
                }
            }
            return imagesUris;
        }

        @Override
        protected void onPostExecute(List<Uri> list) {
            uploadFilesAndImages(list);
        }
    }

    private void renderViewImage(ArrayList<Image> m) {

        images.clear();
        images = m;
        for (int i = 0; i < uploadedImages.size(); i++)
            images.add(uploadedImages.get(i));
        if (images.size() == Image_Limit) {
            add_card.setVisibility(View.GONE);
        } else {
            add_card.setVisibility(View.VISIBLE);

        }
        mAdapter = new AddImageAdapter(AddActivity.this, images);
        mAdapter.setActionListner(AddActivity.this);
        images_recycler.setAdapter(mAdapter);


    }

    private void uploadFilesAndImages(List<Uri> Images) {
        for (int i = 0; i < uploadedImages.size(); i++) {
            mUploadedImages.put("image" + (mUploadedImages.size() + 1), uploadedImages.get(i).getPath());
        }


        if ((images.size()) == 0) {
            addItem(mUploadedImages, getItemData());
            return;
        }
        for (int i = 0; i < Images.size(); i++) {
            uploadSingleFile(Images.get(i), i + 1 + uploadedImages.size());
        }


    }

    private void uploadSingleFile(final Uri file, final int num) {
        UploadTask task = mStorageRef.child(file.getLastPathSegment()).putFile(file);
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mUploadProgressDialog.dismiss();
                Toast.makeText(getBaseContext(), "error occurred during uploading Image:" +
                        file.getLastPathSegment(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: " + e);
            }
        });

        task.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                mUploadedImages.put("image" + num, taskSnapshot.getDownloadUrl().toString());
                int totalfiles = images.size() + uploadedImages.size();
                int curTotal = mUploadedImages.size();
                Log.d(TAG, "onSuccess: " + images.size() +" " + uploadedImages.size() + " ");
                if (totalfiles == curTotal) {
                    addItem(mUploadedImages, getItemData());
                }

            }
        });
    }


    private Hotel getItemData() {
        Hotel hotel = new Hotel();

        try {
            String Hotel_Name = name.getText().toString();
            String Hotel_Phone = phone.getText().toString();
            String Hotel_Website = website.getText().toString();

            hotel.setName(Hotel_Name);
            hotel.setPhone(Hotel_Phone);
            hotel.setWebsite(Hotel_Website);
            hotel.setPrice(price);
            hotel.setTime(ServerValue.TIMESTAMP);
            hotel.setLat(latitude);
            hotel.setLon(longitude);
            hotel.setPets(pets_click);
            hotel.setPool(pool_click);
            hotel.setGym(gym_click);
            hotel.setResturant(restaurant_click);
            hotel.setWifi(wifi_click);
            hotel.setBeach(beach_click);
            hotel.setBreakfast(breakfast_click);
            hotel.setSpa(spa_click);

        }
        catch (Exception ex){
            Toast.makeText(this, "error in price num max 300$", Toast.LENGTH_SHORT).show();
        }
        return hotel;
    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.wifi){
            if (wifi_click){
                wifi_click = false;
                if(Build.VERSION.SDK_INT > 16 ){
                    wifi.setBackground(ContextCompat.getDrawable(AddActivity.this, R.drawable.nav_filter_back));
                }
                else{
                    wifi.setBackgroundDrawable(ContextCompat.getDrawable(AddActivity.this, R.drawable.nav_filter_back));

                }
                wifi.setColorFilter(ContextCompat.getColor(AddActivity.this,R.color.trv_juri));

            }
            else {
                wifi_click = true;
                if(Build.VERSION.SDK_INT > 16 ){
                    wifi.setBackground(ContextCompat.getDrawable(AddActivity.this, R.drawable.nav_filter_back_click));
                }
                else{
                    wifi.setBackgroundDrawable(ContextCompat.getDrawable(AddActivity.this, R.drawable.nav_filter_back_click));

                }
                wifi.setColorFilter(ContextCompat.getColor(AddActivity.this,R.color.colorPrimary));

            }
        }
        else if(v.getId() == R.id.pool){
            if (pool_click){
                pool_click = false;
                if(Build.VERSION.SDK_INT > 16 ){
                    pool.setBackground(ContextCompat.getDrawable(AddActivity.this, R.drawable.nav_filter_back));
                }
                else{
                    pool.setBackgroundDrawable(ContextCompat.getDrawable(AddActivity.this, R.drawable.nav_filter_back));

                }
                pool.setColorFilter(ContextCompat.getColor(AddActivity.this,R.color.trv_juri));

            }
            else {
                pool_click = true;
                if(Build.VERSION.SDK_INT > 16 ){
                    pool.setBackground(ContextCompat.getDrawable(AddActivity.this, R.drawable.nav_filter_back_click));
                }
                else{
                    pool.setBackgroundDrawable(ContextCompat.getDrawable(AddActivity.this, R.drawable.nav_filter_back_click));

                }
                pool.setColorFilter(ContextCompat.getColor(AddActivity.this,R.color.colorPrimary));

            }
        }
        else if(v.getId() == R.id.beach){
            if (beach_click){
                beach_click = false;
                if(Build.VERSION.SDK_INT > 16 ){
                    beach.setBackground(ContextCompat.getDrawable(AddActivity.this, R.drawable.nav_filter_back));
                }
                else{
                    beach.setBackgroundDrawable(ContextCompat.getDrawable(AddActivity.this, R.drawable.nav_filter_back));

                }
                beach.setColorFilter(ContextCompat.getColor(AddActivity.this,R.color.trv_juri));

            }
            else {
                beach_click = true;
                if(Build.VERSION.SDK_INT > 16 ){
                    beach.setBackground(ContextCompat.getDrawable(AddActivity.this, R.drawable.nav_filter_back_click));
                }
                else{
                    beach.setBackgroundDrawable(ContextCompat.getDrawable(AddActivity.this, R.drawable.nav_filter_back_click));

                }
                beach.setColorFilter(ContextCompat.getColor(AddActivity.this,R.color.colorPrimary));

            }
        }
        else if(v.getId() == R.id.breakfast){
            if (breakfast_click){
                breakfast_click = false;
                if(Build.VERSION.SDK_INT > 16 ){
                    breakfast.setBackground(ContextCompat.getDrawable(AddActivity.this, R.drawable.nav_filter_back));
                }
                else{
                    breakfast.setBackgroundDrawable(ContextCompat.getDrawable(AddActivity.this, R.drawable.nav_filter_back));

                }
                breakfast.setColorFilter(ContextCompat.getColor(AddActivity.this,R.color.trv_juri));

            }
            else {
                breakfast_click = true;
                if(Build.VERSION.SDK_INT > 16 ){
                    breakfast.setBackground(ContextCompat.getDrawable(AddActivity.this, R.drawable.nav_filter_back_click));
                }
                else{
                    breakfast.setBackgroundDrawable(ContextCompat.getDrawable(AddActivity.this, R.drawable.nav_filter_back_click));

                }
                breakfast.setColorFilter(ContextCompat.getColor(AddActivity.this,R.color.colorPrimary));

            }
        }
        else if(v.getId() == R.id.spa){
            if (spa_click){
                spa_click = false;
                if(Build.VERSION.SDK_INT > 16 ){
                    spa.setBackground(ContextCompat.getDrawable(AddActivity.this, R.drawable.nav_filter_back));
                }
                else{
                    spa.setBackgroundDrawable(ContextCompat.getDrawable(AddActivity.this, R.drawable.nav_filter_back));

                }
                spa.setColorFilter(ContextCompat.getColor(AddActivity.this,R.color.trv_juri));

            }
            else {
                spa_click = true;
                if(Build.VERSION.SDK_INT > 16 ){
                    spa.setBackground(ContextCompat.getDrawable(AddActivity.this, R.drawable.nav_filter_back_click));
                }
                else{
                    spa.setBackgroundDrawable(ContextCompat.getDrawable(AddActivity.this, R.drawable.nav_filter_back_click));

                }
                spa.setColorFilter(ContextCompat.getColor(AddActivity.this,R.color.colorPrimary));

            }
        }
        else if(v.getId() == R.id.pet){
            if (pets_click){
                pets_click = false;
                if(Build.VERSION.SDK_INT > 16 ){
                    pets.setBackground(ContextCompat.getDrawable(AddActivity.this, R.drawable.nav_filter_back));
                }
                else{
                    pets.setBackgroundDrawable(ContextCompat.getDrawable(AddActivity.this, R.drawable.nav_filter_back));

                }
                pets.setColorFilter(ContextCompat.getColor(AddActivity.this,R.color.trv_juri));

            }
            else {
                pets_click = true;
                if(Build.VERSION.SDK_INT > 16 ){
                    pets.setBackground(ContextCompat.getDrawable(AddActivity.this, R.drawable.nav_filter_back_click));
                }
                else{
                    pets.setBackgroundDrawable(ContextCompat.getDrawable(AddActivity.this, R.drawable.nav_filter_back_click));

                }
                pets.setColorFilter(ContextCompat.getColor(AddActivity.this,R.color.colorPrimary));

            }
        }
        else if(v.getId() == R.id.gym){
            if (gym_click){
                gym_click = false;
                if(Build.VERSION.SDK_INT > 16 ){
                    gym.setBackground(ContextCompat.getDrawable(AddActivity.this, R.drawable.nav_filter_back));
                }
                else{
                    gym.setBackgroundDrawable(ContextCompat.getDrawable(AddActivity.this, R.drawable.nav_filter_back));

                }
                gym.setColorFilter(ContextCompat.getColor(AddActivity.this,R.color.trv_juri));

            }
            else {
                gym_click = true;
                if(Build.VERSION.SDK_INT > 16 ){
                    gym.setBackground(ContextCompat.getDrawable(AddActivity.this, R.drawable.nav_filter_back_click));
                }
                else{
                    gym.setBackgroundDrawable(ContextCompat.getDrawable(AddActivity.this, R.drawable.nav_filter_back_click));

                }
                gym.setColorFilter(ContextCompat.getColor(AddActivity.this,R.color.colorPrimary));

            }
        }
        else if(v.getId() == R.id.restaurant){
            if (restaurant_click){
                restaurant_click = false;
                if(Build.VERSION.SDK_INT > 16 ){
                    restaurant.setBackground(ContextCompat.getDrawable(AddActivity.this, R.drawable.nav_filter_back));
                }
                else{
                    restaurant.setBackgroundDrawable(ContextCompat.getDrawable(AddActivity.this, R.drawable.nav_filter_back));

                }
                restaurant.setColorFilter(ContextCompat.getColor(AddActivity.this,R.color.trv_juri));

            }
            else {
                restaurant_click = true;
                if(Build.VERSION.SDK_INT > 16 ){
                    restaurant.setBackground(ContextCompat.getDrawable(AddActivity.this, R.drawable.nav_filter_back_click));
                }
                else{
                    restaurant.setBackgroundDrawable(ContextCompat.getDrawable(AddActivity.this, R.drawable.nav_filter_back_click));

                }
                restaurant.setColorFilter(ContextCompat.getColor(AddActivity.this,R.color.colorPrimary));

            }
        }

    }

    private void addItem(HashMap<String, String> images, final Hotel item) {
        DatabaseReference itemsRef = FirebaseDatabase.getInstance().getReferenceFromUrl(Constants.FIREBASE_URL_ITEMS);
        itemsRef.keepSynced(true);
        item.setImagesUrls(images);
        itemsRef = itemsRef.push().getRef();

        itemsRef.setValue(item, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                mUploadProgressDialog.dismiss();
                if (databaseError == null) {
                    Toast.makeText(AddActivity.this, "Item uploaded Successfully.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AddActivity.this, MainActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                } else {
                    Toast.makeText(AddActivity.this, "unable to add.", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }



}
