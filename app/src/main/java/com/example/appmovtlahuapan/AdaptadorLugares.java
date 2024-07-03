package com.example.appmovtlahuapan;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class AdaptadorLugares extends RecyclerView.Adapter<AdaptadorLugares.LugarViewHolder> {

    private Context mCtx;
    private List<AdquirirLugares> LugaresList;

    public AdaptadorLugares(Context mCtx, List<AdquirirLugares> lugares) {
        this.mCtx = mCtx;
        this.LugaresList = lugares;
    }

    @NonNull
    @Override
    public AdaptadorLugares.LugarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.custom_lista_lugares,null);
        return new LugarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorLugares.LugarViewHolder holder, int position) {
        AdquirirLugares Lugar = LugaresList.get(position);

        holder.Calle.setText(Lugar.getCalle());
        holder.Nombre.setText(Lugar.getNombre());
        holder.NumeroInterior.setText(Lugar.getNoInt());
        holder.NumeroExterior.setText(Lugar.getNoExt());
        holder.Colonia.setText(Lugar.getColonia());

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ActivityDetallesLugar.class);
                intent.putExtra("Nombre", LugaresList.get(position).getNombre());
                view.getContext().startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return LugaresList.size();
    }

    class LugarViewHolder extends RecyclerView.ViewHolder {

        LinearLayout linearLayout;
        TextView  Calle, Colonia, Nombre, NumeroExterior, NumeroInterior;
        public LugarViewHolder(@NonNull View itemView) {
            super(itemView);

            Nombre = itemView.findViewById(R.id.TextVNombreListadoLugares);
            Calle = itemView.findViewById(R.id.TextVCalleListadoLugares);
            NumeroInterior = itemView.findViewById(R.id.TextVNoIntListadoLugares);
            NumeroExterior = itemView.findViewById(R.id.TextVNoExtListadoLugares);
            Colonia = itemView.findViewById(R.id.TextVColoniaListadoLugares);
            linearLayout = itemView.findViewById(R.id.LinearLayout);
        }
    }
}
