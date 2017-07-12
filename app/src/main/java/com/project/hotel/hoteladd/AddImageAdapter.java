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

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.project.hotel.R;
import com.project.hotel.utils.Utils;

import java.util.ArrayList;


public class AddImageAdapter extends RecyclerView.Adapter<AddImageAdapter.ViewHolder> {
    AddImageAdapter.FileAction liAction;

    private static ArrayList<Image> dataSet;
    private Context mContext;
    private int mAnimationPosition = -1;

    public AddImageAdapter(Context c, ArrayList<Image> files) {

        this.dataSet = files;
        this.mContext = c;
    }


    @Override
    public AddImageAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
// create a new view
        View itemLayoutView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.image_add_item, null);

        // create ViewHolder

        AddImageAdapter.ViewHolder viewHolder = new AddImageAdapter.ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AddImageAdapter.ViewHolder viewHolder, final int i) {
        Image img = dataSet.get(i);

        Uri newUri;
        if (!img.isSelected()) {
            newUri = Utils.getImageFileCompressedUri(Uri.parse(img.getPath()), 720, mContext);
        } else {
            newUri = Uri.parse(img.getPath());
        }
        if (newUri != null) {
//            Toast.makeText(mContext, "not null", Toast.LENGTH_SHORT).show();
            viewHolder.main_image.setImageURI(newUri);
            viewHolder.delete_view.setVisibility(View.VISIBLE);
            viewHolder.delete_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    liAction.onRemoveClick(i);
                }
            });

        } else {
            Toast.makeText(mContext, "Image Not working!", Toast.LENGTH_SHORT).show();

        }

        setAnimation(viewHolder.item, i);
    }

    public void setActionListner(AddImageAdapter.FileAction actionListner) {
        this.liAction = actionListner;

    }

    private void setAnimation(final View view, int position) {
        if (position > mAnimationPosition) {
            view.setScaleX(0f);
            view.setScaleY(0f);
            view.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(300)
                    .setInterpolator(new DecelerateInterpolator());

            mAnimationPosition = position;
        }
    }


    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public SimpleDraweeView main_image;
        public RelativeLayout delete_view;
        public ImageView delete_main;
        public ProgressBar bar;
        public View item;
//        public ImageView iconView;


        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            item = itemLayoutView;
            main_image = (SimpleDraweeView) itemLayoutView.findViewById(R.id.item_image);
            delete_view = (RelativeLayout) itemLayoutView.findViewById(R.id.delete_view);
            delete_main = (ImageView) itemLayoutView.findViewById(R.id.delete_image);
            bar = (ProgressBar) itemLayoutView.findViewById(R.id.Add_item_progress);


//            iconView = (ImageView) itemLayoutView.findViewById(R.id.iconId);


        }

    }

    public interface FileAction {
        public void onRemoveClick(int position);
    }
}
