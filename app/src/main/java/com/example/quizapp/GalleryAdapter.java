package com.example.quizapp;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {
    private List<GalleryItem> galleryItems;
    private Context context;

    public GalleryAdapter(List<GalleryItem> galleryItems, Context context) {
        this.galleryItems = galleryItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.gallery_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GalleryItem item = galleryItems.get(position);
        holder.nameTextView.setText(item.getName());

        if (item.isDrawableResource()) {
            holder.imageView.setImageResource(item.getImageResId());
        } else {
            Uri imageUri = Uri.parse(item.getImagePath());
            Glide.with(context).load(imageUri).into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return galleryItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView nameTextView;

        public ViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.imageView);
            nameTextView = view.findViewById(R.id.nameTextView);
        }
    }
}
