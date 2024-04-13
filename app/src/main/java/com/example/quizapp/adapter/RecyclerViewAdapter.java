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

// Definerer klassen RecyclerViewAdapter som utvider RecyclerView.Adapter parametrisert med RecyclerViewAdapter.MyViewHolder.
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    // Klassevariabler for kontekst, liste av ImageEntity-objekter og et grensesnitt for håndtering av klikk.
    private final Context context;
    private List<ImageEntity> images;
    private final RecyclerViewInterface recyclerViewInterface;

    // Konstruktør for å initialisere klassevariablene.
    public RecyclerViewAdapter(Context context, List<ImageEntity> images, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.images = images;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    // Oppretter nye visningsobjekter (ViewHolder) for hvert listeelement.
    @NonNull
    @Override
    public RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_row, parent, false);
        return new RecyclerViewAdapter.MyViewHolder(view, recyclerViewInterface);
    }

    // Binder data til en eksisterende visning (holder) ved en gitt posisjon i datalistene.
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.MyViewHolder holder, int position) {
        ImageEntity image = images.get(position);

        holder.textView.setText(image.getImageName());
        holder.imageView.setImageURI(image.getImagePath());

        // Setter en klikklytter på itemView.
        holder.itemView.setOnClickListener(v -> {
            if (position != RecyclerView.NO_POSITION && recyclerViewInterface != null) {
                int imageId = images.get(position).getImageId(); // Hent ID her
                recyclerViewInterface.onItemClick(imageId); // Send ID istedenfor posisjon
            }
        });
    }

    // Returnerer antall elementer i datalistene.
    @Override
    public int getItemCount() {
        return images.size();
    }

    // Oppdaterer listen av ImageEntity-objekter og varsler endringer til adapteret.
    public void setImages(List<ImageEntity> images) {
        this.images = images;
        notifyDataSetChanged(); // Varsler om endring i datasettet
    }

    // Statisk indre klasse MyViewHolder som holder referanser til UI-komponenter.
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;

        // Konstruktør som tar itemView og grensesnittet som parametere.
        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            textView = itemView.findViewById(R.id.image_nameView);
            imageView = itemView.findViewById(R.id.imageView);

            // Setter en klikklytter på itemView.
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && recyclerViewInterface != null) {
                        recyclerViewInterface.onItemClick(position);
                    }
                }
            });
        }
    }
}