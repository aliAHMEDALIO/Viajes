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

import android.graphics.Bitmap;


public final class BitmapScaler{
	private BitmapScaler() {}

	
	// scale and keep aspect ratio
	public static Bitmap scaleToFitWidth(Bitmap b, int width)
	{
		float factor = width / (float) b.getWidth();
		return Bitmap.createScaledBitmap(b, width, (int) (b.getHeight() * factor), true);
	}


	// scale and keep aspect ratio
	public static Bitmap scaleToFitHeight(Bitmap b, int height)
	{
		float factor = height / (float) b.getHeight();
		return Bitmap.createScaledBitmap(b, (int) (b.getWidth() * factor), height, true);
	}


	// scale and keep aspect ratio
	public static Bitmap scaleToFill(Bitmap b, int width, int height)
	{
		float factorH = height / (float) b.getWidth();
		float factorW = width / (float) b.getWidth();
		float factorToUse = (factorH > factorW) ? factorW : factorH;
		return Bitmap.createScaledBitmap(b, (int) (b.getWidth() * factorToUse), (int) (b.getHeight() * factorToUse), true);
	}


	// scale and don't keep aspect ratio
	public static Bitmap strechToFill(Bitmap b, int width, int height)
	{
		float factorH = height / (float) b.getHeight();
		float factorW = width / (float) b.getWidth();
		return Bitmap.createScaledBitmap(b, (int) (b.getWidth() * factorW), (int) (b.getHeight() * factorH), true);
	}
}
