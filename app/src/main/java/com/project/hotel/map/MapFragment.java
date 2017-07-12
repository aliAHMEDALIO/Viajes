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


package com.project.hotel.map;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.project.hotel.R;
import com.project.hotel.hotels.HotelContractor;
import com.project.hotel.hotels.HotelsFragment;
import com.project.hotel.model.Hotel;
import com.project.hotel.utils.BitmapScaler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elbehiry on 6/27/17.
 */

public class MapFragment extends Fragment implements HotelContractor.View,OnMapReadyCallback, GoogleMap.OnCameraIdleListener, GoogleMap.OnCameraMoveListener, CardViewAdapter.HotelAction {
    private static MapFragment Instance = null;
    private HotelContractor.Presenter mPresenter;
    private ArrayList<Hotel> hotels;
    private static final int MAP_ZOOM = 14;
//    private double mLatitude = 0.0;
//    private double mLongitude = 0.0;
    private ClusterManager<Hotel> mClusterManager;
    private GoogleMap mMap;
    public final static int MAP_PERMISSION_REQUEST = 1;
    private MapView mMapView;
    private MarkerOptions options = new MarkerOptions();
//    private ArrayList<LatLng> latlngs = new ArrayList<>();
    private RecyclerView recyclerView;
    private CardViewAdapter mAdapter;

    public static MapFragment getInstance(){
        if (Instance == null){
            Instance = new MapFragment();
        }
        return Instance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mRootView = inflater.inflate(R.layout.fragment_map,container,false);
        hotels = new ArrayList<>();


        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_item);
        mapFragment.getMapAsync(this);

         recyclerView = (RecyclerView) mRootView.findViewById(R.id.map_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayout.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new CardViewAdapter(hotels);
        mAdapter.setActionListner(this);
        recyclerView.setAdapter(mAdapter);

        if (mPresenter != null)
            mPresenter.loadHotels();


        return mRootView;
    }


    @Override
    public void setPresenter(HotelContractor.Presenter presenter) {
        mPresenter = presenter;

    }

    @Override
    public void showHotels(List<Hotel> tasks) {
        hotels.clear();
        hotels.addAll(tasks);
        if (tasks.size() >0 ) {
            setupMap();
            mAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void showAddHotel() {

    }

    @Override
    public void showHotelDetailsUi(Hotel hotel) {

    }

    @Override
    public void showErrorMessage(@Nullable String message) {
        if (isAdded())
            Toast.makeText(getContext(),""+message, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void clearData() {
        hotels.clear();
        mMap.clear();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if(mMapView!=null) mMapView.onLowMemory();
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        setUserVisibleHint(true);
        if(mMapView!=null) mMapView.onSaveInstanceState(outState);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //setupMap();

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MAP_PERMISSION_REQUEST) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
               // setupMap();
            } else {
                Toast.makeText(getActivity(), "App can't Open Map Without Location Access Permission", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(MAP_PERMISSION_REQUEST, permissions, grantResults);
        }
    }
    @Override
    public void onCameraIdle() {
    }

    @Override
    public void onCameraMove() {
    }

    private void setupMap() {
        if (mMap != null) {
            UiSettings settings = mMap.getUiSettings();
            settings.setAllGesturesEnabled(true);
            settings.setMyLocationButtonEnabled(true);
            settings.setZoomControlsEnabled(true);

           // LatLng latLng = null;
            if (hotels.size() >0) {
//                Hotel hotel = hotels.get(0);
//                double mLatitude = hotel.getLat();
//                double mLongitude = hotel.getLon();

//                if (mLatitude == 0.0 && mLongitude == 0.0) {
//                    LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
//                    Location location = getLastKnownLocation(locationManager);
//                    if (location != null)
//                        latLng = new LatLng(location.getLatitude(), location.getLongitude());
//                } else {
//                    latLng = new LatLng(mLatitude, mLongitude);
//                }
                //int size = getActivity().getResources().getDimensionPixelSize(R.dimen.fragment_map_marker_size);
                Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.placeholder);
                Bitmap bitmap = BitmapScaler.scaleToFill(originalBitmap, 60, 60);
                if(originalBitmap!=bitmap){
                    originalBitmap.recycle();
                }
                BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap);
                for (int i = 0; i < hotels.size(); i++) {
                    Hotel hotel = hotels.get(i);
                    LatLng latLng = new LatLng(hotel.getLat(),hotel.getLon());
                    options.position(latLng);
                    options.title(hotel.getName());
                    options.snippet("$ "+hotel.getPrice());
                    options.icon(bitmapDescriptor);
                    mMap.addMarker(options);
                    if (i == 0) {
                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(latLng)
                                .zoom(MAP_ZOOM)
                                .bearing(0)
                                .tilt(0)
                                .build();
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                    }
                }
//                if (latLng != null) {
//                    CameraPosition cameraPosition = new CameraPosition.Builder()
//                            .target(latLng)
//                            .zoom(MAP_ZOOM)
//                            .bearing(0)
//                            .tilt(0)
//                            .build();
//                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//                }
                //setupClusterManager();
            }
        }
    }
    private void setupClusterManager() {
        // reference

        // clustering
        if(mMap!=null) {
            mClusterManager = new ClusterManager<>(getActivity(), mMap);
            mClusterManager.setRenderer(new DefaultClusterRenderer<Hotel>(getActivity(), mMap, mClusterManager) {
                @Override
                protected void onBeforeClusterItemRendered(Hotel item, MarkerOptions markerOptions) {
                    int size = getActivity().getResources().getDimensionPixelSize(R.dimen.fragment_map_marker_size);
                    Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.markerfield);
                    Bitmap bitmap = BitmapScaler.scaleToFill(originalBitmap, size, size);
                    if(originalBitmap!=bitmap){
                        originalBitmap.recycle();
                    }

                    BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap);
                    markerOptions.title(item.getName());
                    markerOptions.icon(bitmapDescriptor);
                    markerOptions.snippet(""+item.getPrice());

                    super.onBeforeClusterItemRendered(item, markerOptions);
                }
            });
            mClusterManager.setOnClusterItemInfoWindowClickListener(new ClusterManager.OnClusterItemInfoWindowClickListener<Hotel>() {
                @Override
                public void onClusterItemInfoWindowClick(Hotel i)
                {
                    getActivity().finish();
//                    Toast.makeText(getActivity(), "Action", Toast.LENGTH_SHORT).show();
                }
            });
//            mMap.setOnCameraMoveCanceledListener(m);
            //setOnCameraChangeListener(mClusterManager);
            mMap.setOnInfoWindowClickListener(mClusterManager);
            renderView();
        }
    }
    private void renderView() {
        // reference

        // map
        if (mMap != null) {

            mMap.clear();
            if (hotels.size() > 0) {
                for (Hotel hotel :hotels) {
                    Hotel mitem = hotel;
                    if (mitem != null) {
                        mClusterManager.clearItems();
                        mClusterManager.addItem(mitem);
                        mClusterManager.cluster();
                    }
                }
            }
        }


    }

    private Location getLastKnownLocation(LocationManager locationManager) {

        Location locationNet = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        Location locationGps = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        long timeNet = 0l;
        long timeGps = 0l;

        if(locationNet!=null)
        {
            timeNet = locationNet.getTime();
        }

        if(locationGps!=null)
        {
            timeGps = locationGps.getTime();
        }

        if(timeNet>timeGps) return locationNet;
        else return locationGps;
    }


    @Override
    public void onHotelClick(Hotel hotel) {
        LatLng latLng = new LatLng(hotel.getLat(),hotel.getLon());
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)
                .zoom(MAP_ZOOM)
                .bearing(0)
                .tilt(0)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }
}
