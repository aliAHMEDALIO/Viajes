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

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.project.hotel.R;
import com.project.hotel.model.Hotel;

import java.util.ArrayList;


public class CardViewAdapter extends RecyclerView.Adapter<CardViewAdapter.ViewHolder> {
    CardViewAdapter.HotelAction liAction;

    private static ArrayList<Hotel> dataSet;

    public CardViewAdapter(ArrayList<Hotel> files) {

        dataSet = files;
    }


    @Override
    public CardViewAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
// create a new view
        View itemLayoutView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.map_card, null);

        // create ViewHolder

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CardViewAdapter.ViewHolder viewHolder, int i) {

        final Hotel fp = dataSet.get(i);
        String imageUrl = fp.getImagesUrls().get("image1");

        viewHolder.tvVersionName.setText(fp.getName());
        if (imageUrl != null) {
            int width = 120, height = 120;
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(imageUrl))
                    .setResizeOptions(new ResizeOptions(width, height))
                    .build();
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(request)
                    .setTapToRetryEnabled(true)
                    .setOldController(viewHolder.img.getController())
                    .build();
            viewHolder.img.setController(controller);
            viewHolder.img.getHierarchy().setProgressBarImage(new ProgressBarDrawable());
        }
        viewHolder.lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                liAction.onHotelClick(fp);
            }
        });
//        viewHolder.explor.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                liAction.onHotelClick(fp);
//            }
//        });
//        viewHolder.iconView.setImageResource(fp.getThumbnail());
       // viewHolder.iconView.setBackgroundResource(fp.getThumbnail());

    }
    public void setActionListner(CardViewAdapter.HotelAction actionListner){
        this.liAction = actionListner;

    }




    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvVersionName;
        SimpleDraweeView img;
        public LinearLayout lay;
//        public TextView explor;
//        public ProgressBar bar;
//        public ImageView iconView;


        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            tvVersionName = (TextView) itemLayoutView.findViewById(R.id.fileName);
            lay = (LinearLayout) itemLayoutView.findViewById(R.id.card_lin);
//            explor = (TextView) itemLayoutView.findViewById(R.id.card_text);
            img = (SimpleDraweeView) itemLayoutView.findViewById(R.id.map_img);
//            bar = (ProgressBar) itemLayoutView.findViewById(R.id.prog_do);


//            iconView = (ImageView) itemLayoutView.findViewById(R.id.iconId);


        }

    }

    public interface HotelAction{
        public void onHotelClick(Hotel hotel);
    }
}