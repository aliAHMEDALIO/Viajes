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

package com.project.hotel.main;

import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.appyvet.rangebar.RangeBar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;
import com.project.hotel.R;
import com.project.hotel.data.HotelsRepository;
import com.project.hotel.hoteladd.AddActivity;
import com.project.hotel.hotels.HotelPresenter;
import com.project.hotel.hotels.HotelsFragment;
import com.project.hotel.login.LoginActivity;
import com.project.hotel.map.MapFragment;
import com.project.hotel.model.Hotel;
import com.project.hotel.model.User;
import com.project.hotel.profile.ProfileFragment;
import com.project.hotel.profile.ProfilePresenter;
import com.project.hotel.settings.SettingsFragment;
import com.project.hotel.utils.ActivityUtils;
import com.project.hotel.utils.Constants;
import com.project.hotel.utils.MySearchRecentSuggestions;
import com.project.hotel.utils.SearchSuggestionAdapter;
import com.project.hotel.utils.Utils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    int price = 0;
    private String TAG = MainActivity.class.getSimpleName();
    private SpaceNavigationView spaceNavigationView;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;
    private User mUser;
    private DatabaseReference mDatabase;
    private LinearLayout right_drawer;
    private DrawerLayout mDrawerLayout;
    private ActionMode mActionMode;
    private ImageView iv, wifi, pool, beach, breakfast, spa, pets, gym, restaurant;
    private RangeBar range;
    private boolean wifi_click, pool_click, beach_click, breakfast_click, spa_click, pets_click, gym_click, restaurant_click;
    private Button search;
    private MainActivity.SearchInterface mSearchListener;

    private int x = 0;

    private void getUserData() {
        final String uid = Utils.getUid();
        mDatabase.child(uid).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists())
                            return;
                        mUser = dataSnapshot.getValue(User.class);
                        mUser.setUid(dataSnapshot.getKey());
                        mDatabase.child(uid).removeEventListener(this);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                        // ...
                    }
                }

        );
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl(Constants.FIREBASE_URL_USERS);
        mDatabase.keepSynced(true);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    Log.i(TAG, "User Null");
                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                } else {
                    Log.i(TAG, "User Not Null" + user.getUid());
                    getUserData();

                }
            }
        };

        right_drawer = (LinearLayout) findViewById(R.id.right_drawer);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                try {
                    View view = getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                } catch (Exception c) {

                }
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                View view = getCurrentFocus();
                if (mActionMode != null) {
                    mActionMode.finish();
                }
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        iv = new ImageView(this);
        iv.setImageResource(R.drawable.filter);
        iv.setPadding(10, 10, 10, 10);
        iv.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.white));
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mActionMode != null) {
                    mActionMode.finish();
                }
                View view = getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                if (!mDrawerLayout.isDrawerOpen(right_drawer)) {
                    mDrawerLayout.openDrawer(right_drawer);

                }

            }
        });
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

        range = (RangeBar) findViewById(R.id.rangebar);
        range.setSeekPinByValue(10);
        range.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {

                try {
                    price = Integer.parseInt(rightPinValue);
                } catch (NumberFormatException ex) {

                }
            }
        });

        search = (Button) findViewById(R.id.search_drawer);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Hotel hotel = new Hotel();

                hotel.setPets(pets_click);
                hotel.setPool(pool_click);
                hotel.setGym(gym_click);
                hotel.setResturant(restaurant_click);
                hotel.setWifi(wifi_click);
                hotel.setBeach(beach_click);
                hotel.setBreakfast(breakfast_click);
                hotel.setSpa(spa_click);
                hotel.setPrice(price);

                if (mDrawerLayout.isDrawerOpen(right_drawer)) {
                    mDrawerLayout.closeDrawer(right_drawer);

                }
                mSearchListener.onSearch(hotel);
            }
        });
        spaceNavigationView = (SpaceNavigationView) findViewById(R.id.space);
        spaceNavigationView.addSpaceItem(new SpaceItem("Home", R.drawable.home));
        spaceNavigationView.addSpaceItem(new SpaceItem("Map", R.drawable.map));
        spaceNavigationView.addSpaceItem(new SpaceItem("Favourite", R.drawable.profile));
        spaceNavigationView.addSpaceItem(new SpaceItem("Settings", R.drawable.settings));
        spaceNavigationView.showIconOnly();
        spaceNavigationView.setSpaceOnClickListener(new SpaceOnClickListener() {
            @Override
            public void onCentreButtonClick() {
                //search
                mActionMode = startSupportActionMode(new SearchActionModeCallback());

            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {
                display(itemIndex);

            }

            @Override
            public void onItemReselected(int itemIndex, String itemName) {

            }
        });


        display(0);


    }

    private void display(int position) {
        x = position;
        switch (position) {
            case 0:
                HotelsFragment hotelFragment = HotelsFragment.getInstance();
                HotelPresenter hotelPresenter = HotelPresenter.getInstance(hotelFragment, HotelsRepository.getInstance());
                if (hotelFragment != null) {
                    ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), hotelFragment, R.id.contentFrame);
                    hotelFragment.setPresenter(hotelPresenter);
                    setListener(hotelPresenter);
                }
                break;
            case 1:
                MapFragment mapFragment = MapFragment.getInstance();
                HotelPresenter mapPresenter = HotelPresenter.getInstance(mapFragment, HotelsRepository.getInstance());
                if (mapFragment != null) {
                    ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mapFragment, R.id.contentFrame);
                    mapFragment.setPresenter(mapPresenter);
                    setListener(mapPresenter);
                }
                break;
            case 2:


                ProfileFragment profileFragment = ProfileFragment.getInstance();
                ProfilePresenter profilePresenter = ProfilePresenter.getInstance();
                if (profileFragment != null) {
                    ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), profileFragment, R.id.contentFrame);
                    profileFragment.setPresenter(profilePresenter);

                }

                break;
            case 3:
                SettingsFragment settingsFragment = SettingsFragment.getInstance();
                if (settingsFragment != null) {
                    ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), settingsFragment, R.id.contentFrame);
                }

                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            if (mAuthListener != null) {
                mAuth.removeAuthStateListener(mAuthListener);
            }

        } catch (Exception e) {

        }

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.wifi) {
            if (wifi_click) {
                wifi_click = false;
                if (Build.VERSION.SDK_INT > 16) {
                    wifi.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.nav_filter_back));
                } else {
                    wifi.setBackgroundDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.nav_filter_back));

                }
                wifi.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.trv_juri));

            } else {
                wifi_click = true;
                if (Build.VERSION.SDK_INT > 16) {
                    wifi.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.nav_filter_back_click));
                } else {
                    wifi.setBackgroundDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.nav_filter_back_click));

                }
                wifi.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.colorAccent));

            }
        } else if (v.getId() == R.id.pool) {
            if (pool_click) {
                pool_click = false;
                if (Build.VERSION.SDK_INT > 16) {
                    pool.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.nav_filter_back));
                } else {
                    pool.setBackgroundDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.nav_filter_back));

                }
                pool.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.trv_juri));

            } else {
                pool_click = true;
                if (Build.VERSION.SDK_INT > 16) {
                    pool.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.nav_filter_back_click));
                } else {
                    pool.setBackgroundDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.nav_filter_back_click));

                }
                pool.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.colorAccent));

            }
        } else if (v.getId() == R.id.beach) {
            if (beach_click) {
                beach_click = false;
                if (Build.VERSION.SDK_INT > 16) {
                    beach.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.nav_filter_back));
                } else {
                    beach.setBackgroundDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.nav_filter_back));

                }
                beach.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.trv_juri));

            } else {
                beach_click = true;
                if (Build.VERSION.SDK_INT > 16) {
                    beach.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.nav_filter_back_click));
                } else {
                    beach.setBackgroundDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.nav_filter_back_click));

                }
                beach.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.colorAccent));

            }
        } else if (v.getId() == R.id.breakfast) {
            if (breakfast_click) {
                breakfast_click = false;
                if (Build.VERSION.SDK_INT > 16) {
                    breakfast.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.nav_filter_back));
                } else {
                    breakfast.setBackgroundDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.nav_filter_back));

                }
                breakfast.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.trv_juri));

            } else {
                breakfast_click = true;
                if (Build.VERSION.SDK_INT > 16) {
                    breakfast.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.nav_filter_back_click));
                } else {
                    breakfast.setBackgroundDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.nav_filter_back_click));

                }
                breakfast.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.colorAccent));

            }
        } else if (v.getId() == R.id.spa) {
            if (spa_click) {
                spa_click = false;
                if (Build.VERSION.SDK_INT > 16) {
                    spa.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.nav_filter_back));
                } else {
                    spa.setBackgroundDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.nav_filter_back));

                }
                spa.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.trv_juri));

            } else {
                spa_click = true;
                if (Build.VERSION.SDK_INT > 16) {
                    spa.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.nav_filter_back_click));
                } else {
                    spa.setBackgroundDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.nav_filter_back_click));

                }
                spa.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.colorAccent));

            }
        } else if (v.getId() == R.id.pet) {
            if (pets_click) {
                pets_click = false;
                if (Build.VERSION.SDK_INT > 16) {
                    pets.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.nav_filter_back));
                } else {
                    pets.setBackgroundDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.nav_filter_back));

                }
                pets.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.trv_juri));

            } else {
                pets_click = true;
                if (Build.VERSION.SDK_INT > 16) {
                    pets.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.nav_filter_back_click));
                } else {
                    pets.setBackgroundDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.nav_filter_back_click));

                }
                pets.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.colorAccent));

            }
        } else if (v.getId() == R.id.gym) {
            if (gym_click) {
                gym_click = false;
                if (Build.VERSION.SDK_INT > 16) {
                    gym.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.nav_filter_back));
                } else {
                    gym.setBackgroundDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.nav_filter_back));

                }
                gym.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.trv_juri));

            } else {
                gym_click = true;
                if (Build.VERSION.SDK_INT > 16) {
                    gym.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.nav_filter_back_click));
                } else {
                    gym.setBackgroundDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.nav_filter_back_click));

                }
                gym.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.colorAccent));

            }
        } else if (v.getId() == R.id.restaurant) {
            if (restaurant_click) {
                restaurant_click = false;
                if (Build.VERSION.SDK_INT > 16) {
                    restaurant.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.nav_filter_back));
                } else {
                    restaurant.setBackgroundDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.nav_filter_back));

                }
                restaurant.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.trv_juri));

            } else {
                restaurant_click = true;
                if (Build.VERSION.SDK_INT > 16) {
                    restaurant.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.nav_filter_back_click));
                } else {
                    restaurant.setBackgroundDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.nav_filter_back_click));

                }
                restaurant.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.colorAccent));

            }
        }

    }

    private void setListener(MainActivity.SearchInterface searchInterface) {
        mSearchListener = searchInterface;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add_hotel) {
            startActivity(new Intent(MainActivity.this, AddActivity.class));


        } else if (item.getItemId() == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();

        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(right_drawer)) {
            mDrawerLayout.closeDrawer(right_drawer);

        } else {
            if (x != 0) {
                display(0);

                spaceNavigationView.changeCurrentItem(0);
            } else {
                finish();
            }
        }
    }

    public interface SearchInterface {

        void onSearchName(String name);

        void onSearch(Hotel hotel);
    }

    private class SearchActionModeCallback implements ActionMode.Callback {
        private SearchView mSearchView;
        private SearchSuggestionAdapter mSearchSuggestionAdapter;


        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {


//            mSearchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.search_menu));
            mSearchView = new SearchView(getSupportActionBar().getThemedContext());
            ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
            mSearchView.setLayoutParams(params);
            mSearchView.setLayoutParams(new ActionBar.LayoutParams(Gravity.LEFT));
            mSearchView.setMaxWidth(Integer.MAX_VALUE);


//            mSearchView = new SearchView();


            setupSearchView(mSearchView);

            // search menu item
            MenuItem searchMenuItem = menu.add(Menu.NONE, Menu.NONE, 2, getString(R.string.menu_search));
            searchMenuItem.setIcon(R.drawable.search);
            searchMenuItem.expandActionView();
            MenuItemCompat.setActionView(searchMenuItem, mSearchView);
            MenuItemCompat.setShowAsAction(searchMenuItem, MenuItem.SHOW_AS_ACTION_ALWAYS);


            MenuItem searchMenuItem3 = menu.add(Menu.NONE, Menu.NONE, 1, getString(R.string.menu_search));
            searchMenuItem3.setIcon(R.drawable.search);
            MenuItemCompat.setActionView(searchMenuItem3, iv);
            MenuItemCompat.setShowAsAction(searchMenuItem3, MenuItem.SHOW_AS_ACTION_ALWAYS);


            return true;
        }


        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {

            return true;
        }


        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            return false;
        }


        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
            if (mDrawerLayout.isDrawerOpen(right_drawer)) {
                mDrawerLayout.closeDrawer(right_drawer);

            }
        }


        private void setupSearchView(SearchView searchView) {
            searchView.setIconifiedByDefault(true);
            searchView.setIconified(false);
            searchView.onActionViewExpanded();
            searchView.setQueryHint(getString(R.string.menu_search_hint));
            AutoCompleteTextView searchText = (AutoCompleteTextView) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
            searchText.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white));
            searchText.setHintTextColor(ContextCompat.getColor(MainActivity.this, R.color.color_two));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    mSearchListener.onSearchName(query);
                    SearchRecentSuggestions suggestions = new SearchRecentSuggestions(MainActivity.this, MySearchRecentSuggestions.AUTHORITY, MySearchRecentSuggestions.MODE);
                    suggestions.saveRecentQuery(query, null);
                    mActionMode.finish();
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String query) {
                    if (query.length() > 2) {
                        updateSearchSuggestion(query);
                    } else if (query.length() == 0) {
                        mSearchListener.onSearchName("");
                    }
                    return true;
                }
            });
            searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
                @Override
                public boolean onSuggestionSelect(int position) {
                    return false;
                }

                @Override
                public boolean onSuggestionClick(int position) {
                    Cursor cursor = (Cursor) mSearchSuggestionAdapter.getItem(position);
                    String title = cursor.getString(cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1));
                    mSearchListener.onSearchName(title);
                    mActionMode.finish();
                    return true;
                }
            });
        }


        private void updateSearchSuggestion(String query) {
            // cursor
            ContentResolver contentResolver = getApplicationContext().getContentResolver();
            String contentUri = "content://" + MySearchRecentSuggestions.AUTHORITY + '/' + SearchManager.SUGGEST_URI_PATH_QUERY;
            Uri uri = Uri.parse(contentUri);
            Cursor cursor = contentResolver.query(uri, null, null, new String[]{query}, null);
            // searchview content
            if (mSearchSuggestionAdapter == null) {
                // create adapter
                mSearchSuggestionAdapter = new SearchSuggestionAdapter(MainActivity.this, cursor);
                // set adapter
                mSearchView.setSuggestionsAdapter(mSearchSuggestionAdapter);
            } else {
                // refill adapter
                mSearchSuggestionAdapter.refill(MainActivity.this, cursor);
                // set adapter
                mSearchView.setSuggestionsAdapter(mSearchSuggestionAdapter);
            }
        }
    }

}
