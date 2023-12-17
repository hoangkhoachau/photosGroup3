package com.example.photosGroup3;

import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.photosGroup3.Callback.tagAdapterCallback;
import com.example.photosGroup3.Utils.DatabaseManager;

import java.io.File;
import java.util.ArrayList;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.ViewHolder> implements tagAdapterCallback {

    DatabaseManager dbManager;
    Cursor cursor;
    ArrayList<String> labels;

    public TagAdapter() {
        dbManager = new DatabaseManager(MainActivity.mainActivity);
        cursor = dbManager.getAllLabels();
        labels = new ArrayList<>();
        while (cursor.moveToNext()) {
            labels.add(cursor.getString(1));
        }
        dbManager.addCallback(this);
    }

    public void update() {
        cursor = dbManager.getAllLabels();
        labels = new ArrayList<>();
        while (cursor.moveToNext()) {
            labels.add(cursor.getString(1));
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(MainActivity.mainActivity).inflate(R.layout.circle_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(holder.getBindingAdapterPosition());
    }

    @Override
    public int getItemCount() {
        return labels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.textView);
        }

        public void bind(int position) {
            Cursor cursor = dbManager.getImagesForLabel(labels.get(position));
            cursor.moveToLast();
            String path = cursor.getString(1);
            Glide.with(MainActivity.mainActivity).load(new File(path)).into(imageView);
            textView.setText(labels.get(position));
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<String> images = new ArrayList<>();
                    Cursor cursor = dbManager.getImagesForLabel(labels.get(position));
                    while (cursor.moveToNext()) {
                        images.add(cursor.getString(1));
                    }
                    Album album = new Album("", labels.get(position), images);
                    int pos = getBindingAdapterPosition();
                    Fragment previous = SearchHostingFragment.getInstance().getChildFragmentManager().findFragmentById(R.id.album_hosting_fragment);
                    SearchHostingFragment.getInstance().getChildFragmentManager().beginTransaction()
                            .add(R.id.album_hosting_fragment, AlbumDisplayFragment.newInstance(album), null)
                            .hide(previous)
                            .setReorderingAllowed(true)
                            .addToBackStack(null)
                            .commit();
                }
            });
        }


    }

}
