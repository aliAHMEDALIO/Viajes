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


package com.project.hotel.hotels.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.google.firebase.database.FirebaseDatabase;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.project.hotel.R;
import com.project.hotel.model.Hotel;
import com.project.hotel.utils.Utils;

import java.util.ArrayList;
import java.util.Date;



public class HotelDataAdapter extends RecyclerView.Adapter<HotelDataAdapter.ItemViewHolder> {
    ArrayList<Hotel> itemData;
    HotelDataAdapter.Action liAction;
    Context c;
    public void setItemData(ArrayList<Hotel> itemData) {
        this.itemData = itemData;
    }



    public HotelDataAdapter(Context context, ArrayList<Hotel> itemData) {
        this.itemData = itemData;
        this.c = context;
    }

    public void setActionListner(HotelDataAdapter.Action actionListner) {
        this.liAction = actionListner;


    }

    @Override
    public HotelDataAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home, parent, false);
        return new ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(HotelDataAdapter.ItemViewHolder holder, final int position) {
        final Hotel i = itemData.get(position);
        String imageUrl = i.getImagesUrls().get("image1");
        String name = i.getName();

        holder.hotelName.setText(""+name);
        holder.hotelPrice.setText(""+i.getPrice()+" $");
        holder.hotelFavourite.setLiked(i.isFavorite());

        if (imageUrl != null) {
            int width = 300, height = 200;
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(imageUrl))
                    .setResizeOptions(new ResizeOptions(width, height))
                    .build();
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(request)
                    .setTapToRetryEnabled(true)
                    .setOldController(holder.hotelImage.getController())
                    .build();
            holder.hotelImage.setController(controller);
            holder.hotelImage.getHierarchy().setProgressBarImage(new ProgressBarDrawable());
        }

        holder.hotelImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                liAction.onItem(i);
            }
        });
//        holder.cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                liAction.onItem(i);
//
//            }
//        });
        holder.hotelFavourite.setLiked(i.isFavorite());
        holder.hotelFavourite.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                liAction.onFavourite(true, position);

            }

            @Override
            public void unLiked(LikeButton likeButton) {
                liAction.onFavourite(false, position);
            }
        });
    }


    @Override
    public int getItemCount() {
        return itemData.size();
    }

//
//    private void setAnimation(final View view, int position) {
//        if (mAnimationEnabled && position > mAnimationPosition) {
//            view.setScaleX(0f);
//            view.setScaleY(0f);
//            view.animate()
//                    .scaleX(1f)
//                    .scaleY(1f)
//                    .setDuration(300)
//                    .setInterpolator(new DecelerateInterpolator());
//
//            mAnimationPosition = position;
//        }
//    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView hotelName;
        private TextView hotelPrice;
        private SimpleDraweeView hotelImage;
        private LikeButton hotelFavourite;
//        private CardView cardView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            hotelName = (TextView) itemView.findViewById(R.id.hotel_name);
            hotelPrice = (TextView) itemView.findViewById(R.id.price_text);
            hotelImage = (SimpleDraweeView) itemView.findViewById(R.id.hotel_image);
            hotelFavourite = (LikeButton) itemView.findViewById(R.id.post_Favourite);
//            cardView = (CardView) itemView.findViewById(R.id.card_item);
        }
    }


    public interface Action {
        public void onItem(Hotel item);

        public void onFavourite(boolean liked, int position);

    }


}
