package com.example.appmovtlahuapan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdaptadorSitioTuristicoActividades extends RecyclerView.Adapter<AdaptadorSitioTuristicoActividades.ActividadViewHolder> {

    private Context mCtx;
    private List<AdquirirSitioTuristicoActividades> ActividadesList;

    public AdaptadorSitioTuristicoActividades(Context mCtx, List<AdquirirSitioTuristicoActividades> actividades) {
        this.mCtx = mCtx;
        this.ActividadesList = actividades;
    }

    @NonNull
    @Override
    public AdaptadorSitioTuristicoActividades.ActividadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.custom_lista_sitio_turistico_actividades,null);
        return new ActividadViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorSitioTuristicoActividades.ActividadViewHolder holder, int position) {
        AdquirirSitioTuristicoActividades actividad = ActividadesList.get(position);

        holder.Nombre.setText(actividad.getNombre());
        holder.Descripcion.setText(actividad.getCosto());
        holder.Costo.setText(actividad.getDescripcion());

    }

    @Override
    public int getItemCount() {
        return ActividadesList.size();
    }

    class ActividadViewHolder extends RecyclerView.ViewHolder {

        TextView  Costo, Descripcion, Nombre;
        public ActividadViewHolder(@NonNull View itemView) {
            super(itemView);

            Nombre = itemView.findViewById(R.id.TextVNombreListadoActividades);
            Descripcion = itemView.findViewById(R.id.TextVDescripcionListadoActividades);
            Costo = itemView.findViewById(R.id.TextVCostoListadoActividades);
        }
    }
}
