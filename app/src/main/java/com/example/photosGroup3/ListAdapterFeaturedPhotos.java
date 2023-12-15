package com.example.photosGroup3;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import me.xdrop.fuzzywuzzy.FuzzySearch;

public class ListAdapterFeaturedPhotos extends ListAdapter{

    public ListAdapterFeaturedPhotos(ListAdapter listAdapter) {
        super(listAdapter.imageDisplay, listAdapter.imagePhotos, listAdapter.isGrid, listAdapter.context);
    }
    public ListAdapterFeaturedPhotos(ImageDisplay imageDisplay, ArrayList<String> imagePhotos, boolean isGrid, Context context) {
        super(imageDisplay, imagePhotos, isGrid, context);
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.column_item, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends ListAdapter.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnLongClickListener(null);
        }
    }



}

