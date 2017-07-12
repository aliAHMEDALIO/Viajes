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


package com.project.hotel.settings;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.project.hotel.R;

/**
 * Created by Elbehiry on 6/30/17.
 */

public class SettingsFragment extends Fragment {
    private static SettingsFragment Instance = null;

    public static SettingsFragment getInstance(){
        if (Instance == null){
            Instance = new SettingsFragment();
        }
        return Instance;
    }


    private Button  mCancelButton;
    private LinearLayout mLinearLayout;
    private LinearLayout mShowLinearLayout;
    boolean isAppear = false;
    private int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
    private int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
    private ValueAnimator mAnimator;
    private int mViewHigh = 0;
    private ImageView mArrowImageView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mRootView = inflater.inflate(R.layout.fragment_settings,container,false);

        mCancelButton = (Button) mRootView.findViewById(R.id.change_password_button_cancel);
        mLinearLayout = (LinearLayout) mRootView.findViewById(R.id.linear_layout_password);
        mShowLinearLayout = (LinearLayout) mRootView.findViewById(R.id.show_passwd_container);
        mArrowImageView = (ImageView) mRootView.findViewById(R.id.arrow_show);

        mLinearLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {

                mViewHigh = mLinearLayout.getHeight();
                //Toast.makeText(SettingsActivity.this, "" + mViewHigh, Toast.LENGTH_SHORT).show();
                mLinearLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mLinearLayout.setVisibility(View.GONE);

            }
        });


        mShowLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isAppear) {
                    expand(mLinearLayout);
                    rotateArrow(1);
                } else {
                    collapse(mLinearLayout);
                    rotateArrow(2);
                }
            }
        });

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                collapse(mLinearLayout);
                rotateArrow(2);

            }
        });
        return mRootView;
    }

    private void expand(final LinearLayout layout) {
        layout.measure(widthSpec, heightSpec);
        layout.setVisibility(View.VISIBLE);
        int height = mViewHigh;
        mAnimator = slideAnimator(layout, 0, height);
        if (mAnimator != null) {
            isAppear = true;
            mAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {

                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            mAnimator.start();
        }
    }

    private void collapse(final LinearLayout layout) {
        // int finalHeight = layout.getHeight();
        int finalHeight = mViewHigh;
        ValueAnimator mAnimator = slideAnimator(layout, finalHeight, 0);
        if (mAnimator != null) {
            mAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationEnd(Animator animator) {
                    isAppear = false;
                    layout.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationStart(Animator animator) {
                }

                @Override
                public void onAnimationCancel(Animator animator) {
                }

                @Override
                public void onAnimationRepeat(Animator animator) {
                }
            });
            mAnimator.start();
        }
    }
    private void rotateArrow(int i) {
        mArrowImageView.animate().rotation(i * 180).setDuration(500).start();
    }



    private ValueAnimator slideAnimator(final View view, int start, int end) {
//        if(end != 0) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end).setDuration(500);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.height = value;
                view.setLayoutParams(layoutParams);
                view.requestLayout();
            }
        });
        return animator;
    }

}
