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

public class AdaptadorEventosProximos extends RecyclerView.Adapter<AdaptadorEventosProximos.EventosViewHolder> {

    private Context mCtx;
    private List<AdquirirEventosProximos> EventosList;

    public AdaptadorEventosProximos(Context mCtx, List<AdquirirEventosProximos> eventos) {
        this.mCtx = mCtx;
        this.EventosList = eventos;
    }

    @NonNull
    @Override
    public AdaptadorEventosProximos.EventosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.custom_lista_eventos_proximos,null);
        return new EventosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorEventosProximos.EventosViewHolder holder, int position) {
        AdquirirEventosProximos eventos = EventosList.get(position);

        holder.Nombre.setText(eventos.getNombre());
        holder.Descripcion.setText(eventos.getDescripcion());
        holder.Fecha.setText(eventos.getFecha());
        holder.FechaFinal.setText(eventos.getFechaFinal());
        holder.Hora.setText(eventos.getHora());
        holder.HoraFinal.setText(eventos.getHoraFinal());
        holder.Lugar.setText(eventos.getLugar());
        Glide.with(mCtx)
                .load(eventos.getFoto() + "?timestamp=" + System.currentTimeMillis())
                .into(holder.Imagen);

    }

    @Override
    public int getItemCount() {
        return EventosList.size();
    }

    class EventosViewHolder extends RecyclerView.ViewHolder {

        ImageView Imagen;
        TextView Descripcion, Fecha, FechaFinal, Hora, HoraFinal, Lugar, Nombre;
        public EventosViewHolder(@NonNull View itemView) {
            super(itemView);

            Imagen = itemView.findViewById(R.id.imageView2);
            Nombre = itemView.findViewById(R.id.TextVNombreListadoEventos);
            Descripcion = itemView.findViewById(R.id.TextVDescripcionListadoEventos);
            Fecha = itemView.findViewById(R.id.TextVFechaListadoEventos);
            FechaFinal = itemView.findViewById(R.id.TextVFechaFinalListadoEventos);
            Hora = itemView.findViewById(R.id.TextVHoraListadoEventos);
            HoraFinal = itemView.findViewById(R.id.TextVHoraFinalListadoEventos);
            Lugar = itemView.findViewById(R.id.TextVLugarListadoEventos);
        }
    }
}
