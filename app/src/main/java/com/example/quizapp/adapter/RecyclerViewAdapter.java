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

/**
 * Adapterklasse for RecyclerView som håndterer visning av bilder og tekst i en liste.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    /**
     * 'final' i variabler indikerer at de ikke kan re-tilordnes etter initialisering. Dette gir sikkerhet
     * i flertrådede miljøer og garanterer at disse variablene forblir konstante gjennom klassens levetid.
     */
    private final Context context;
    private List<ImageEntity> images;
    private final RecyclerViewInterface recyclerViewInterface;

    /**
     * Konstruktør som initialiserer adapteren med kontekst, en liste av bilder, og et grensesnitt for håndtering av klikkhendelser.
     *
     * @param context Applikasjonskontekst som gir tilgang til ressurser og systemtjenester.
     * @param images Liste av ImageEntity-objekter som inneholder dataene som skal vises.
     * @param recyclerViewInterface Grensesnitt for å håndtere klikkhendelser på elementene i listen.
     */
    public RecyclerViewAdapter(Context context, List<ImageEntity> images, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.images = images;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    /**
     * Oppretter nye ViewHolder-objekter for hvert element i listen, som inneholder oppsettet for visningen.
     * 'LayoutInflater' brukes til å opprette View-objekter fra XML.
     *
     * @param parent Foreldregruppen som den nye visningen skal legges til i.
     * @param viewType Typen av visning som skal opprettes.
     * @return En ny instans av MyViewHolder som holder på oppsettet for en enkelt rad.
     */
    @NonNull
    @Override
    public RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_row, parent, false);
        return new RecyclerViewAdapter.MyViewHolder(view, recyclerViewInterface);
    }

    /**
     * Binder data til visninger (ViewHolders) på bestemte posisjoner i datalisten.
     *
     * @param holder ViewHolder som skal oppdateres med nye data.
     * @param position Posisjonen til dataelementet i listen.
     */
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

    /**
     * Returnerer totalt antall elementer i listen.
     *
     * @return Antall elementer i datalisten.
     */
    @Override
    public int getItemCount() {
        return images.size();
    }

    /**
     * Oppdaterer listen av bilder og varsler RecyclerView at datasettet har endret seg.
     *
     * @param images Den nye listen av ImageEntity-objekter som skal vises.
     */
    public void setImages(List<ImageEntity> images) {
        this.images = images;
        notifyDataSetChanged(); // Varsler om endring i datasettet
    }

    /**
     * Indre klasse som definerer en ViewHolder.
     * ViewHolder-klasser inneholder referanser til UI-komponenter.
     * 'static' betyr at klassen ikke har tilgang til medlemsvariablene i den omsluttende instansen,
     * noe som reduserer minnelekkasje og gir en klarere separasjon av ansvar.
     * Når du har en statisk indre klasse, som MyViewHolder i din RecyclerViewAdapter, så er "omsluttende klasse" i dette tilfellet RecyclerViewAdapter.
     */
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;

        /**
         * Konstruktør for MyViewHolder som initialiserer visningskomponentene og setter opp en klikklytter.
         *
         * @param itemView Rotvisningen for ViewHolder.
         * @param recyclerViewInterface Grensesnitt for å håndtere klikkhendelser.
         */
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