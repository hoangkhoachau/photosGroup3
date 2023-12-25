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
    int numItems;
    int randomFactor;
    ListAdapter listAdapter;
    public ListAdapterFeaturedPhotos(ListAdapter listAdapter, int _numItems) {
        super(listAdapter.imageDisplay, new ArrayList<String>(), listAdapter.isGrid, listAdapter.context);
        this.listAdapter = listAdapter;
        numItems = _numItems;
        randomize();
    }

    void randomize() {
        regenerateRandomFactor();
        imagePhotos.clear();
        int size = listAdapter.imagePhotos.size();
        if (size != 0) {
            for (int i = 0; i < numItems; i++) {
                int idx = (i*randomFactor*100) % size;
                if (idx >= 0 && idx < size) {
                    imagePhotos.add(listAdapter.imagePhotos.get(idx));
                }
            }
        }
        notifyDataSetChangedNotify();
    }
    void regenerateRandomFactor() {
        randomFactor = (int)(Math.random()*100);
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

