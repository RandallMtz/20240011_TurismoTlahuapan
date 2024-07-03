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

import java.util.List;

public class AdaptadorLugaresNombres extends RecyclerView.Adapter<AdaptadorLugaresNombres.LugarViewHolder> {

    private Context mCtx;
    private List<AdquirirLugaresNombres> LugaresList;

    public AdaptadorLugaresNombres(Context mCtx, List<AdquirirLugaresNombres> lugares) {
        this.mCtx = mCtx;
        this.LugaresList = lugares;
    }

    @NonNull
    @Override
    public AdaptadorLugaresNombres.LugarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.custom_lista_lugares_nombres,null);
        return new LugarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorLugaresNombres.LugarViewHolder holder, int position) {
        AdquirirLugaresNombres Lugar = LugaresList.get(position);

        holder.Nombre.setText(Lugar.getNombre());

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ActivitySubirImagenes.class);
                intent.putExtra("IdLugar", Lugar.getId());
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
        TextView Nombre;
        public LugarViewHolder(@NonNull View itemView) {
            super(itemView);
            Nombre = itemView.findViewById(R.id.TextVNombreListadoLugaresSoloNombre);
            linearLayout = itemView.findViewById(R.id.LinearLayout);

        }
    }
}
