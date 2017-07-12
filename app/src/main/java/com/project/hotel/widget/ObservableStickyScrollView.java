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


package com.project.hotel.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.emilsjolander.components.StickyScrollViewItems.StickyScrollView;


// code taken from: http://stackoverflow.com/questions/3948934/synchronise-scrollview-scroll-positions-android
public class ObservableStickyScrollView extends StickyScrollView {
    private ScrollViewListener scrollViewListener = null;


    public interface ScrollViewListener
    {
        void onScrollChanged(ObservableStickyScrollView scrollView, int x, int y, int oldx, int oldy);
    }


    public ObservableStickyScrollView(Context context)
    {
        super(context);
    }


    public ObservableStickyScrollView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }


    public ObservableStickyScrollView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }


    public void setOnScrollViewListener(ScrollViewListener scrollViewListener)
    {
        this.scrollViewListener = scrollViewListener;
    }


    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy)
    {
        super.onScrollChanged(x, y, oldx, oldy);
        if(scrollViewListener != null)
        {
            scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
        }
    }
}
