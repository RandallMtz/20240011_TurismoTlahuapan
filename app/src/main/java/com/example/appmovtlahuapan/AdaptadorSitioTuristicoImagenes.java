package com.example.appmovtlahuapan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class AdaptadorSitioTuristicoImagenes extends RecyclerView.Adapter<AdaptadorSitioTuristicoImagenes.EventosViewHolder> {

    private Context mCtx;
    private List<AdquirirSitioTuristicoImagenes> ImagenList;

    public AdaptadorSitioTuristicoImagenes(Context mCtx, List<AdquirirSitioTuristicoImagenes> imagenes) {
        this.mCtx = mCtx;
        this.ImagenList = imagenes;
    }

    @NonNull
    @Override
    public AdaptadorSitioTuristicoImagenes.EventosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.custom_lista_sitio_turistico_imagenes,null);
        return new EventosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorSitioTuristicoImagenes.EventosViewHolder holder, int position) {
        AdquirirSitioTuristicoImagenes imagen = ImagenList.get(position);

        holder.Titulo.setText(imagen.getDescripcion());

        Glide.with(mCtx)
                .load(imagen.getFoto() + "?timestamp=" + System.currentTimeMillis())
                .into(holder.Imagen);

    }

    @Override
    public int getItemCount() {
        return ImagenList.size();
    }

    class EventosViewHolder extends RecyclerView.ViewHolder {

        ImageView Imagen;
        TextView Titulo;
        public EventosViewHolder(@NonNull View itemView) {
            super(itemView);

            Imagen = itemView.findViewById(R.id.ImagenListado);
            Titulo = itemView.findViewById(R.id.TextVTituloImagenDetallesLugar);
        }
    }
}
