package com.example.quizapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.R;
import com.example.quizapp.model.ImageEntity;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    private final Context context;
    private List<ImageEntity> images;
    private final RecyclerViewInterface recyclerViewInterface;

    public RecyclerViewAdapter(Context context, List<ImageEntity> images, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.images = images;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_row, parent, false);
        return new RecyclerViewAdapter.MyViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.MyViewHolder holder, int position) {
        ImageEntity image = images.get(position);

        holder.textView.setText(image.getImageName());
        holder.imageView.setImageURI(image.getImagePath());
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public void setImages(List<ImageEntity> images) {
        this.images = images;
        notifyDataSetChanged(); // Varsler om endring i datasettet
    }

    public List<ImageEntity> getImages() {
        return images;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;

        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            textView = itemView.findViewById(R.id.image_nameView);
            imageView = itemView.findViewById(R.id.imageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recyclerViewInterface != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            recyclerViewInterface.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}