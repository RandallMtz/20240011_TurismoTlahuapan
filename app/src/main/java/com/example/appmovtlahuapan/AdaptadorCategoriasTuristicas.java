package com.example.appmovtlahuapan;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class AdaptadorCategoriasTuristicas extends RecyclerView.Adapter<AdaptadorCategoriasTuristicas.LugarViewHolder> {

    private Context mCtx;
    private List<AdquirirCategoriasTuristicas> CategoriasList;

    public AdaptadorCategoriasTuristicas(Context mCtx, List<AdquirirCategoriasTuristicas> categorias) {
        this.mCtx = mCtx;
        this.CategoriasList = categorias;
    }

    @NonNull
    @Override
    public AdaptadorCategoriasTuristicas.LugarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.custom_lista_categorias,null);
        return new LugarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorCategoriasTuristicas.LugarViewHolder holder, int position) {
        AdquirirCategoriasTuristicas categorias = CategoriasList.get(position);

        holder.Categoria.setText(categorias.getCategoria());

        Glide.with(mCtx)
                .load(categorias.getFotoPromocional())
                .into(holder.Imagen);

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ActivityListadoLugares.class);
                intent.putExtra("Valor", categorias.getCategoria().toString());
                view.getContext().startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return CategoriasList.size();
    }

    class LugarViewHolder extends RecyclerView.ViewHolder {

        ImageView Imagen;
        LinearLayout linearLayout;
        TextView Categoria;
        public LugarViewHolder(@NonNull View itemView) {
            super(itemView);

            Categoria = itemView.findViewById(R.id.TextVNombreListadoCategorias);
            Imagen = itemView.findViewById(R.id.CategoriasImage);
            linearLayout = itemView.findViewById(R.id.LinearLayoutCat);
        }
    }
}
